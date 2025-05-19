package service;

import jakarta.servlet.ServletContext;
import model.Vehicle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class VehicleService {
    private List<Vehicle> vehicles;
    private final FileHandler fileHandler;

    public VehicleService() {
        this.vehicles = new ArrayList<>();
        this.fileHandler = null;
    }

    public VehicleService(ServletContext context) {
        this.vehicles = new ArrayList<>();
        this.fileHandler = new FileHandler(context);
        loadVehicles();
    }

    private void loadVehicles() {
        if (fileHandler == null) {
            System.err.println("FileHandler is not initialized. Cannot load vehicles.");
            return;
        }
        System.out.println("=== Loading Vehicles ===");
        List<String> vehicleLines = fileHandler.readVehicles();
        System.out.println("Total lines read from vehicles.txt: " + vehicleLines.size());
        for (String line : vehicleLines) {
            System.out.println("Processing vehicle line: [" + line + "]");
            String[] parts = line.split(",");
            if (parts.length >= 7) {
                int vehicleId = Integer.parseInt(parts[0]);
                int userId = Integer.parseInt(parts[1]);
                String make = parts[2];
                String model = parts[3];
                int year = Integer.parseInt(parts[4]);
                String licensePlate = parts[5];
                int mileage = Integer.parseInt(parts[6]);
                Vehicle vehicle = new Vehicle(vehicleId, userId, make, model, year, licensePlate, mileage);
                vehicles.add(vehicle);
                System.out.println("Added vehicle: " + make + " " + model);
            }
        }
        System.out.println("Total vehicles loaded: " + vehicles.size());
    }

    public Vehicle getVehicleById(int vehicleId) {
        return vehicles.stream()
            .filter(v -> v.getVehicleId() == vehicleId)
            .findFirst()
            .orElse(null);
    }

    public List<Vehicle> getUserVehicles(int userId) {
        if (fileHandler == null) {
            System.err.println("FileHandler is not initialized. Cannot get user vehicles.");
            return new ArrayList<>();
        }
        List<String> lines = fileHandler.readVehicles();
        return lines.stream()
            .map(line -> {
                String[] parts = line.split(",");
                if (parts.length >= 7 && Integer.parseInt(parts[1]) == userId) {
                    return new Vehicle(
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1]),
                        parts[2],
                        parts[3],
                        Integer.parseInt(parts[4]),
                        parts[5],
                        Integer.parseInt(parts[6])
                    );
                }
                return null;
            })
            .filter(vehicle -> vehicle != null)
            .collect(Collectors.toList());
    }

    public List<Vehicle> getAllVehicles() {
        if (fileHandler == null) {
            System.err.println("FileHandler is not initialized. Cannot get all vehicles.");
            return new ArrayList<>();
        }
        List<String> lines = fileHandler.readVehicles();
        return lines.stream()
            .map(line -> {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    return new Vehicle(
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1]),
                        parts[2],
                        parts[3],
                        Integer.parseInt(parts[4]),
                        parts[5],
                        Integer.parseInt(parts[6])
                    );
                }
                return null;
            })
            .filter(vehicle -> vehicle != null)
            .collect(Collectors.toList());
    }

    public void addVehicle(Vehicle vehicle) {
        if (fileHandler == null) {
            System.err.println("FileHandler is not initialized. Cannot add vehicle.");
            return;
        }
        vehicles.add(vehicle);
        
        // Save to file
        fileHandler.writeVehicle(vehicle.toString(), true);
    }

    public void updateVehicle(Vehicle vehicle) {
        if (fileHandler == null) {
            System.err.println("FileHandler is not initialized. Cannot update vehicle.");
            return;
        }
        Vehicle existingVehicle = vehicles.stream()
            .filter(v -> v.getVehicleId() == vehicle.getVehicleId())
            .findFirst()
            .orElse(null);
            
        if (existingVehicle != null) {
            vehicles.remove(existingVehicle);
            vehicles.add(vehicle);
            
            // Update file
            List<String> lines = fileHandler.readVehicles();
            List<String> updatedLines = lines.stream()
                .map(line -> {
                    String[] parts = line.split(",");
                    if (parts.length >= 1 && Integer.parseInt(parts[0]) == vehicle.getVehicleId()) {
                        return vehicle.toString();
                    }
                    return line;
                })
                .collect(Collectors.toList());
            fileHandler.writeVehicle(String.join("\n", updatedLines), false);
        }
    }

    public void deleteVehicle(int vehicleId) {
        if (fileHandler == null) {
            System.err.println("FileHandler is not initialized. Cannot delete vehicle.");
            return;
        }
        vehicles.removeIf(v -> v.getVehicleId() == vehicleId);
        
        // Update file
        List<String> lines = fileHandler.readVehicles();
        List<String> updatedLines = lines.stream()
            .filter(line -> {
                String[] parts = line.split(",");
                return parts.length >= 1 && Integer.parseInt(parts[0]) != vehicleId;
            })
            .collect(Collectors.toList());
        fileHandler.writeVehicle(String.join("\n", updatedLines), false);
    }

    public int getTotalVehicles() {
        return vehicles.size();
    }

    public int getUserVehicleCount(int userId) {
        return (int) vehicles.stream()
            .filter(v -> v.getUserId() == userId)
            .count();
    }

    public void reloadVehicles() {
        vehicles.clear();
        loadVehicles();
    }
}