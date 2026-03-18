# P2P-Parking

### Folder structure
```
P2P-Parking/
├── src/
│   ├── main/
│   │   ├── java/parking/project/
│   │   │   ├── config/                   # Security & App configuration
│   │   │   ├── controller/               # MVC Controllers (Logic Routing) 
│   │   │   │   ├── AuthController.java   # Registration & Login
│   │   │   │   ├── DriverController.java # Search & Booking UI logic
│   │   │   │   ├── OwnerController.java  # Listing management UI logic
│   │   │   │   └── AdminController.java  # User management & Reports
│   │   │   ├── model/                    # Entities (Database Tables) 
│   │   │   │   ├── User.java             # Abstract Base Class
│   │   │   │   ├── Driver.java           # Extends User
│   │   │   │   ├── SpaceOwner.java       # Extends User
│   │   │   │   ├── Admin.java            # Extends User
│   │   │   │   ├── ParkingSpot.java      # Spot details & Availability
│   │   │   │   ├── Booking.java          # Reservation details 
│   │   │   │   └── Payment.java          # Transaction & Earnings
│   │   │   ├── repository/               # Spring Data JPA Interfaces 
│   │   │   ├── service/                  # Business Logic Layer
│   │   │   │   ├── BookingService.java   # Handles availability verification 
│   │   │   │   └── UserService.java      # Handles authentication logic
│   │   │   └── patterns/                 # Manual Pattern Implementations
│   │   │       ├── factory/              # Creational: SpotFactory.java
│   │   │       ├── facade/               # Structural: BookingFacade.java
│   │   │       ├── strategy/             # Behavioral: PricingStrategy.java
│   │   │       └── observer/             # Behavioral: BookingObserver.java
│   │   ├── resources/
│   │   │   ├── static/                   # CSS, Images, JS (Visual Styling)
│   │   │   ├── templates/                # View Layer (Thymeleaf/HTML) 
│   │   │   │   ├── driver/               # Search and Booking pages
│   │   │   │   ├── owner/                # Listing and Earnings pages
│   │   │   │   └── admin/                # User and Report pages
│   │   │   └── application.properties    # DB and Framework settings 
│   └── test/                             # Unit and Integration tests
├── documentation/                        # Submission Requirements
│   ├── diagrams/                         # UML (Use Case, Class, Activity, State)
│   ├── reports/                          # Final PDF Report
│   └── screenshots/                      # UI proof (White background)
├── pom.xml                               # Maven Dependencies (Spring Boot) 
└── README.md                             # GitHub Project Documentation
```
