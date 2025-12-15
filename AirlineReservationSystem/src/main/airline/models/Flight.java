package main.airline.models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Flight implements Serializable {
    private static final long serialVersionUID = 1L;

    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private int totalSeats;
    private int availableSeats;
    private double price;

    public Flight(String flightNumber, String origin, String destination,
                  LocalDateTime departureTime, LocalDateTime arrivalTime,
                  int totalSeats, double price) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.price = price;
    }

    public String getFlightNumber() { return flightNumber; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public int getTotalSeats() { return totalSeats; }
    public int getAvailableSeats() { return availableSeats; }
    public double getPrice() { return price; }

    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }

    public boolean bookSeat() {
        if (availableSeats > 0) { availableSeats--; return true; }
        return false;
    }

    public void cancelSeat() {
        if (availableSeats < totalSeats) { availableSeats++; }
    }

    @Override
    public String toString() {
        return String.format("Flight %s: %s to %s | Dep: %s | Arr: %s | Seats: %d/%d | Price: $%.2f",
                flightNumber, origin, destination, departureTime, arrivalTime,
                availableSeats, totalSeats, price);
    }
}
