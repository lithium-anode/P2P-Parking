package parking.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import parking.project.model.*;
import parking.project.model.enums.BookingStatus;
import parking.project.model.enums.PaymentMethod;
import parking.project.model.enums.SpotType;
import parking.project.model.enums.VehicleType;
import parking.project.patterns.behavioral.state.AvailableState;
import parking.project.patterns.behavioral.strategy.PricingStrategy;
import parking.project.repository.BookingRepository;
import parking.project.repository.ParkingSpotRepository;

import java.time.Duration;
import java.time.LocalDateTime;

/*
    [GRASP: Information Expert]
    Co-ordinates data from Driver, ParkingSpot, and Payment services to manage
    the booking lifecycle and ensure a seamless parking experience.
    [GRASP: Creator]
    Responsible for creating and managing the lifecycle of Booking objects.
    [GRASP: High Cohesion]
    Centralizes all logic related to the parking lifecycle, including 
    compatibility checks, availability verification, and cost calculation.
*/
@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ParkingSpotRepository parkingSpotRepository;
    private final PaymentService paymentService;

    @Autowired
    public BookingService(BookingRepository bookingRepository, ParkingSpotRepository parkingSpotRepository, PaymentService paymentService) {
        this.bookingRepository = bookingRepository;
        this.parkingSpotRepository = parkingSpotRepository;
        this.paymentService = paymentService;
    }

    // Goal: Double-Booking Prevention
    public Booking createBooking(Driver driver, ParkingSpot spot, LocalDateTime start, LocalDateTime end, PricingStrategy strategy) {
        // Check Vehicle Compatibility
        if (!isCompatible(driver, spot)) {
            throw new IllegalArgumentException("Incompatible vehicle type");
        }

        // [Design Pattern: Behavioral - State]
        // Explicitly check availability to provide the specific error string for the UI
        if (!(spot.getCurrentState() instanceof AvailableState)) {
            throw new IllegalStateException("Spot is not available for booking");
        }

        // [Design Pattern: Behavioral - State]
        // Transitioning to Reserved ensures double-bookings do not occur.
        spot.getCurrentState().handleReserve(spot);

        // Calculate cost using the Strategy Pattern
        int hours = (int) Math.ceil(Duration.between(start, end).toMinutes() / 60.0);
        double totalCost = spot.calculateCost(hours, strategy);

        Booking booking = new Booking(driver, spot, start, end, totalCost);
        booking.setStatus(BookingStatus.CONFIRMED);

        parkingSpotRepository.save(spot);
        return bookingRepository.save(booking);
    }

    // Goal: Checkout Logic
    // [Design Pattern: State - Logic]
    // Updates the spot availability during checkout by vacating the spot.
    @Transactional
    public void processCheckout(Booking booking, PaymentMethod paymentMethod) {
        booking.setStatus(BookingStatus.COMPLETED);
        
        ParkingSpot spot = booking.getParkingSpot();
        spot.getCurrentState().handleVacate(spot); // Transition back to Available

        paymentService.processPayment(booking, paymentMethod);
        parkingSpotRepository.save(spot);
        bookingRepository.save(booking);
    }

    private boolean isCompatible(Driver driver, ParkingSpot spot) {
        if (spot.getSpotType() == SpotType.ELECTRIC_CHARGING) {
            return driver.getVehicleTypes().contains(VehicleType.ELECTRIC);
        }
        return true; // Simplified for basic types
    }

    public void cancelBooking(Booking booking) {
        if (booking.getStatus() != BookingStatus.PENDING && booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Only pending or confirmed bookings can be cancelled.");
        }
        ParkingSpot spot = booking.getParkingSpot();
        spot.setCurrentState(new AvailableState());  // Free the spot
        bookingRepository.delete(booking);
    }
}