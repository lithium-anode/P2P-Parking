package parking.project.patterns.observer;

import parking.project.model.Booking;

public interface BookingObserver {
    // Triggered whenever a booking status changes
    void onBookingStatusChange(Booking booking);
}