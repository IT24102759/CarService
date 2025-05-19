package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int userId;
    private String username;
    private String password;
    private String email;
    private boolean admin;
    private List<Vehicle> vehicles;

    public User() {
        this.vehicles = new ArrayList<>();
    }

    public User(int userId, String username, String password, String email, boolean admin) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.admin = admin;
        this.vehicles = new ArrayList<>();
    }

    // Getters
    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public boolean isAdmin() {
        return admin;
    }

    public List<Vehicle> getVehicles() {
        return new ArrayList<>(vehicles);
    }

    // Setters
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
        }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    // Vehicle management
    public void addVehicle(Vehicle vehicle) {
        if (vehicle != null && !vehicles.contains(vehicle)) {
            vehicles.add(vehicle);
        }
    }

    public void removeVehicle(Vehicle vehicle) {
        vehicles.remove(vehicle);
    }

    @Override
    public String toString() {
        return userId + "," + username + "," + password + "," + email + "," + admin;
    }
}