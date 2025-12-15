package main.airline;

import main.airline.models.Flight;
import main.airline.models.Passenger;
import main.airline.models.Reservation;
import main.airline.services.FlightService;
import main.airline.services.ReservationService;
import main.airline.utils.DataStorage;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final FlightService flightService = new FlightService();
    private static final ReservationService reservationService = new ReservationService(flightService);
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Initialize sample files if missing
        DataStorage.initializeSampleData();

        // ---- Load persisted data ----
        for (Flight f : DataStorage.loadFlights()) {
            if (f != null) flightService.addFlight(f);
        }
        for (Reservation r : DataStorage.loadReservations()) {
            if (r != null) reservationService.addReservationFromStorage(r);
        }
        // --------------------------------

        boolean running = true;
        while (running) {
            System.out.println("\nAirline Reservation System");
            System.out.println("1. Search Flights");
            System.out.println("2. Book a Flight");
            System.out.println("3. View Reservations");
            System.out.println("4. Cancel Reservation");
            System.out.println("5. Admin Menu");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = safeNextInt();
            switch (choice) {
                case 1 -> searchFlights();
                case 2 -> bookFlight();
                case 3 -> viewReservations();
                case 4 -> cancelReservation();
                case 5 -> adminMenu();
                case 6 -> {
                    // ---- Save before exit ----
                    List<Flight> flightsToSave = flightService.getAllFlights();
                    List<Reservation> reservationsToSave = reservationService.getAllReservations();
                    List<Passenger> passengersToSave = extractPassengers(reservationsToSave);
                    DataStorage.saveAllData(flightsToSave, reservationsToSave, passengersToSave);
                    // ----------------------------
                    System.out.println("Thank you for using our system. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static int safeNextInt() {
        while (true) {
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }

    private static void searchFlights() {
        System.out.print("Enter origin: ");
        String origin = scanner.nextLine().trim();
        System.out.print("Enter destination: ");
        String destination = scanner.nextLine().trim();

        List<Flight> flights = flightService.searchFlights(origin, destination);
        if (flights.isEmpty()) {
            System.out.println("No flights found for the given route.");
        } else {
            System.out.println("\nAvailable Flights:");
            flights.forEach(System.out::println);
        }
    }

    private static void bookFlight() {
        System.out.print("Enter flight number: ");
        String flightNumber = scanner.nextLine().trim();
        Flight flight = flightService.findFlightByNumber(flightNumber);

        if (flight == null) {
            System.out.println("Flight not found.");
            return;
        }

        System.out.println("Flight found: " + flight);
        System.out.print("Enter number of seats to book: ");
        int seats = safeNextInt();

        if (seats <= 0) {
            System.out.println("Seats must be greater than 0.");
            return;
        }
        if (flight.getAvailableSeats() < seats) {
            System.out.println("Not enough seats available. Only " + flight.getAvailableSeats() + " left.");
            return;
        }

        // Passenger details
        System.out.println("\nEnter Passenger Details:");
        System.out.print("Passenger ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Full Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Phone: ");
        String phone = scanner.nextLine().trim();

        Passenger passenger = new Passenger(id, name, email, phone);
        Reservation reservation = reservationService.createReservation(flight, passenger, seats);

        if (reservation != null) {
            System.out.println("\nReservation created successfully!");
            System.out.println(reservation);
        } else {
            System.out.println("Failed to create reservation.");
        }
    }

    private static void viewReservations() {
        System.out.print("Enter passenger ID: ");
        String passengerId = scanner.nextLine().trim();

        List<Reservation> reservations = reservationService.getReservationsByPassenger(passengerId);
        if (reservations.isEmpty()) {
            System.out.println("No reservations found for this passenger.");
        } else {
            System.out.println("\nYour Reservations:");
            reservations.forEach(System.out::println);
        }
    }

    private static void cancelReservation() {
        System.out.print("Enter reservation ID to cancel: ");
        String reservationId = scanner.nextLine().trim();

        if (reservationService.cancelReservation(reservationId)) {
            System.out.println("Reservation cancelled successfully.");
        } else {
            System.out.println("Reservation not found or already cancelled.");
        }
    }

    private static void adminMenu() {
        boolean inAdminMenu = true;
        while (inAdminMenu) {
            System.out.println("\nAdmin Menu");
            System.out.println("1. Add Flight");
            System.out.println("2. View All Flights");
            System.out.println("3. Update Flight");
            System.out.println("4. Delete Flight");
            System.out.println("5. View All Reservations");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = safeNextInt();
            switch (choice) {
                case 1 -> addFlight();
                case 2 -> viewAllFlights();
                case 3 -> updateFlight();
                case 4 -> deleteFlight();
                case 5 -> viewAllReservations();
                case 6 -> inAdminMenu = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addFlight() {
        System.out.println("\nEnter Flight Details:");
        System.out.print("Flight Number: ");
        String flightNumber = scanner.nextLine().trim();
        System.out.print("Origin: ");
        String origin = scanner.nextLine().trim();
        System.out.print("Destination: ");
        String destination = scanner.nextLine().trim();
        System.out.print("Departure Date (YYYY-MM-DD): ");
        String depDate = scanner.nextLine().trim();
        System.out.print("Departure Time (HH:MM): ");
        String depTime = scanner.nextLine().trim();
        System.out.print("Arrival Date (YYYY-MM-DD): ");
        String arrDate = scanner.nextLine().trim();
        System.out.print("Arrival Time (HH:MM): ");
        String arrTime = scanner.nextLine().trim();
        System.out.print("Total Seats: ");
        int totalSeats = safeNextInt();
        System.out.print("Price: ");
        double price = Double.parseDouble(scanner.nextLine().trim());

        LocalDateTime departure = LocalDateTime.parse(depDate + "T" + depTime);
        LocalDateTime arrival = LocalDateTime.parse(arrDate + "T" + arrTime);

        Flight flight = new Flight(flightNumber, origin, destination, departure, arrival, totalSeats, price);
        flightService.addFlight(flight);
        System.out.println("Flight added successfully!");
    }

    private static void viewAllFlights() {
        List<Flight> flights = flightService.getAllFlights();
        if (flights.isEmpty()) {
            System.out.println("No flights available.");
        } else {
            System.out.println("\nAll Flights:");
            flights.forEach(System.out::println);
        }
    }

    private static void updateFlight() {
        System.out.print("Enter flight number to update: ");
        String flightNumber = scanner.nextLine().trim();
        Flight existingFlight = flightService.findFlightByNumber(flightNumber);

        if (existingFlight == null) {
            System.out.println("Flight not found.");
            return;
        }

        System.out.println("Current flight details: " + existingFlight);
        System.out.println("\nEnter new flight details (leave blank to keep current value):");

        System.out.print("Origin [" + existingFlight.getOrigin() + "]: ");
        String origin = scanner.nextLine().trim();
        System.out.print("Destination [" + existingFlight.getDestination() + "]: ");
        String destination = scanner.nextLine().trim();

        Flight updatedFlight = new Flight(
                flightNumber,
                origin.isEmpty() ? existingFlight.getOrigin() : origin,
                destination.isEmpty() ? existingFlight.getDestination() : destination,
                existingFlight.getDepartureTime(),
                existingFlight.getArrivalTime(),
                existingFlight.getTotalSeats(),
                existingFlight.getPrice()
        );

        if (flightService.updateFlight(updatedFlight)) {
            System.out.println("Flight updated successfully!");
        } else {
            System.out.println("Failed to update flight.");
        }
    }

    private static void deleteFlight() {
        System.out.print("Enter flight number to delete: ");
        String flightNumber = scanner.nextLine().trim();

        if (flightService.deleteFlight(flightNumber)) {
            System.out.println("Flight deleted successfully.");
        } else {
            System.out.println("Flight not found.");
        }
    }

    private static void viewAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            System.out.println("\nAll Reservations:");
            reservations.forEach(System.out::println);
        }
    }

    private static List<Passenger> extractPassengers(List<Reservation> reservations) {
        Map<String, Passenger> map = new LinkedHashMap<>();
        for (Reservation r : reservations) {
            if (r != null && r.getPassenger() != null) {
                map.put(r.getPassenger().getId(), r.getPassenger());
            }
        }
        return new ArrayList<>(map.values());
    }
}
