package main.airline.utils;

import main.airline.models.Flight;
import main.airline.models.Passenger;
import main.airline.models.Reservation;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {
    private static final String FLIGHTS_FILE = "flights.dat";
    private static final String RESERVATIONS_FILE = "reservations.dat";
    private static final String PASSENGERS_FILE = "passengers.dat";

    // Save all data
    public static void saveAllData(List<Flight> flights, List<Reservation> reservations, List<Passenger> passengers) {
        saveFlights(flights);
        saveReservations(reservations);
        savePassengers(passengers);
    }

    // Load all data
    public static void loadAllData(List<Flight> flights, List<Reservation> reservations, List<Passenger> passengers) {
        List<Flight> loadedFlights = loadFlights();
        List<Reservation> loadedReservations = loadReservations();
        List<Passenger> loadedPassengers = loadPassengers();

        flights.clear();
        reservations.clear();
        passengers.clear();

        flights.addAll(loadedFlights);
        reservations.addAll(loadedReservations);
        passengers.addAll(loadedPassengers);
    }

    // Flight operations
    public static List<Flight> loadFlights() {
        return loadData(FLIGHTS_FILE);
    }

    private static void saveFlights(List<Flight> flights) {
        saveData(FLIGHTS_FILE, flights);
    }

    // Reservation operations
    public static List<Reservation> loadReservations() {
        return loadData(RESERVATIONS_FILE);
    }

    private static void saveReservations(List<Reservation> reservations) {
        saveData(RESERVATIONS_FILE, reservations);
    }

    // Passenger operations
    public static List<Passenger> loadPassengers() {
        return loadData(PASSENGERS_FILE);
    }

    private static void savePassengers(List<Passenger> passengers) {
        saveData(PASSENGERS_FILE, passengers);
    }

    // Generic save/load methods
    @SuppressWarnings("unchecked")
    private static <T> List<T> loadData(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data from " + filename + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private static <T> void saveData(String filename, List<T> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("Error saving data to " + filename + ": " + e.getMessage());
        }
    }

    // Initialize sample data if no files exist
    public static void initializeSampleData() {
        if (new File(FLIGHTS_FILE).exists() && 
            new File(RESERVATIONS_FILE).exists() && 
            new File(PASSENGERS_FILE).exists()) {
            return;
        }

        List<Flight> sampleFlights = createSampleFlights();
        List<Passenger> samplePassengers = new ArrayList<>();
        List<Reservation> sampleReservations = new ArrayList<>();

        saveFlights(sampleFlights);
        savePassengers(samplePassengers);
        saveReservations(sampleReservations);
    }

    private static List<Flight> createSampleFlights() {
        List<Flight> flights = new ArrayList<>();
        
        // Sample flight 1
        flights.add(new Flight("AA123", "New York", "Los Angeles", 
            LocalDateTime.now().plusDays(1).withHour(8).withMinute(0), 
            LocalDateTime.now().plusDays(1).withHour(11).withMinute(0), 
            150, 299.99));
        
        // Sample flight 2
        flights.add(new Flight("DL456", "Chicago", "Miami", 
            LocalDateTime.now().plusDays(2).withHour(10).withMinute(30), 
            LocalDateTime.now().plusDays(2).withHour(13).withMinute(45), 
            200, 249.99));
        
        // Sample flight 3
        flights.add(new Flight("UA789", "San Francisco", "Seattle", 
            LocalDateTime.now().plusDays(3).withHour(7).withMinute(15), 
            LocalDateTime.now().plusDays(3).withHour(9).withMinute(45), 
            180, 199.99));
            
        return flights;
    }

    // Backup and restore functionality
    public static void backupData(String backupFolder) {
        File folder = new File(backupFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        copyFile(FLIGHTS_FILE, backupFolder + "/flights_backup.dat");
        copyFile(RESERVATIONS_FILE, backupFolder + "/reservations_backup.dat");
        copyFile(PASSENGERS_FILE, backupFolder + "/passengers_backup.dat");
    }

    private static void copyFile(String sourcePath, String destPath) {
        try (InputStream in = new FileInputStream(sourcePath);
             OutputStream out = new FileOutputStream(destPath)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        } catch (IOException e) {
            System.err.println("Error creating backup: " + e.getMessage());
        }
    }
}