package main.airline.services;

import main.airline.models.Flight;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FlightService {
    private List<Flight> flights;

    public FlightService() {
        this.flights = new ArrayList<>();
        // Initialize with some sample flights
        initializeSampleFlights();
    }

    private void initializeSampleFlights() {
        flights.add(new Flight("AA123", "New York", "Los Angeles", 
                LocalDateTime.of(2023, 12, 15, 8, 0), 
                LocalDateTime.of(2023, 12, 15, 11, 0), 
                150, 299.99));
        flights.add(new Flight("DL456", "Chicago", "Miami", 
                LocalDateTime.of(2023, 12, 16, 10, 30), 
                LocalDateTime.of(2023, 12, 16, 13, 45), 
                200, 249.99));
        // Add more sample flights as needed
    }

    public void addFlight(Flight flight) {
        flights.add(flight);
    }

    public List<Flight> getAllFlights() {
        return new ArrayList<>(flights);
    }

    public Flight findFlightByNumber(String flightNumber) {
        return flights.stream()
                .filter(f -> f.getFlightNumber().equalsIgnoreCase(flightNumber))
                .findFirst()
                .orElse(null);
    }

    public List<Flight> searchFlights(String origin, String destination) {
        return flights.stream()
                .filter(f -> f.getOrigin().equalsIgnoreCase(origin) && 
                           f.getDestination().equalsIgnoreCase(destination))
                .collect(Collectors.toList());
    }

    public boolean updateFlight(Flight updatedFlight) {
        for (int i = 0; i < flights.size(); i++) {
            if (flights.get(i).getFlightNumber().equalsIgnoreCase(updatedFlight.getFlightNumber())) {
                flights.set(i, updatedFlight);
                return true;
            }
        }
        return false;
    }

    public boolean deleteFlight(String flightNumber) {
        return flights.removeIf(f -> f.getFlightNumber().equalsIgnoreCase(flightNumber));
    }
}