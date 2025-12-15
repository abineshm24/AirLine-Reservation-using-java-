package main.airline.models;

import java.io.Serializable;

public class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private Flight flight;
    private Passenger passenger;
    private int seats;
    private boolean confirmed;
    private boolean cancelled;

    public Reservation(String reservationId, Flight flight, Passenger passenger, int seats) {
        this.reservationId = reservationId;
        this.flight = flight;
        this.passenger = passenger;
        this.seats = seats;
        this.confirmed = false;
        this.cancelled = false;
    }

    public String getReservationId() { return reservationId; }
    public Flight getFlight() { return flight; }
    public Passenger getPassenger() { return passenger; }
    public int getSeats() { return seats; }

    public boolean isConfirmed() { return confirmed; }
    public boolean isCancelled() { return cancelled; }

    public void confirmReservation() { this.confirmed = true; }
    public void cancelReservation() { this.cancelled = true; }

    @Override
    public String toString() {
        return "Reservation{id='" + reservationId + "', flight=" +
                (flight != null ? flight.getFlightNumber() : "N/A") +
                ", passenger=" + (passenger != null ? passenger.getName() : "N/A") +
                ", seats=" + seats +
                ", confirmed=" + confirmed +
                ", cancelled=" + cancelled + "}";
    }
}
