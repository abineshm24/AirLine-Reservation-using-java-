package main.airline.services;

import main.airline.models.Flight;
import main.airline.models.Passenger;
import main.airline.models.Reservation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ReservationService {
    private final List<Reservation> reservations;
    private final FlightService flightService;

    public ReservationService(FlightService flightService) {
        this.reservations = new ArrayList<>();
        this.flightService = flightService;
    }

    public Reservation createReservation(Flight flight, Passenger passenger, int seats) {
        if (flight == null || passenger == null) return null;
        if (seats <= 0) return null;

        Flight managedFlight = flightService.findFlightByNumber(flight.getFlightNumber());
        if (managedFlight == null) return null;

        if (managedFlight.getAvailableSeats() < seats) return null;

        String reservationId = "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Reservation reservation = new Reservation(reservationId, managedFlight, passenger, seats);

        for (int i = 0; i < seats; i++) managedFlight.bookSeat();

        reservations.add(reservation);
        return reservation;
    }

    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations);
    }

    public Reservation findReservationById(String reservationId) {
        if (reservationId == null) return null;
        return reservations.stream()
                .filter(r -> reservationId.equalsIgnoreCase(r.getReservationId()))
                .findFirst()
                .orElse(null);
    }

    public boolean cancelReservation(String reservationId) {
        Reservation reservation = findReservationById(reservationId);
        if (reservation == null) return false;

        if (!reservation.isCancelled()) {
            reservation.cancelReservation();
            return true;
        }
        return false;
    }

    public List<Reservation> getReservationsByPassenger(String passengerId) {
        if (passengerId == null) return Collections.emptyList();
        return reservations.stream()
                .filter(r -> r.getPassenger() != null &&
                        passengerId.equalsIgnoreCase(r.getPassenger().getId()))
                .collect(Collectors.toList());
    }

    /** Needed by Main.java when loading from file */
    public void addReservationFromStorage(Reservation reservation) {
        if (reservation != null) reservations.add(reservation);
    }
}
