package service;

import model.User;
import model.Vehicle;
import jakarta.servlet.ServletContext;
import util.DatabaseUtil;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class UserService {
    private final FileHandler fileHandler;
    private List<User> users;
    private final ServletContext context;

    public UserService(ServletContext context) {
        this.fileHandler = new FileHandler(context);
        this.users = new ArrayList<>();
        this.context = context;
        loadUsers();
    }

    private void loadUsers() {
        users.clear();
        System.out.println("=== Loading Users ===");
        List<String> lines = fileHandler.readUsers();
        System.out.println("Total lines read from users.txt: " + lines.size());
        VehicleService vehicleService = new VehicleService(context);
        for (String line : lines) {
            System.out.println("Processing user line: [" + line + "]");
            String[] parts = line.split(",");
            System.out.println("Parts after split: " + parts.length);
            if (parts.length >= 5) {
                int userId = Integer.parseInt(parts[0]);
                String username = parts[1];
                String password = parts[2];
                String email = parts[3];
                boolean admin = Boolean.parseBoolean(parts[4]);
                User user = new User(userId, username, password, email, admin);
                // Set vehicles for this user
                List<Vehicle> userVehicles = vehicleService.getUserVehicles(userId);
                System.out.println("Found " + userVehicles.size() + " vehicles for user " + username);
                user.setVehicles(userVehicles);
                users.add(user);
                System.out.println("Added user: " + username + " with " + userVehicles.size() + " vehicles");
            } else {
                System.out.println("Skipping malformed user line: [" + line + "]");
            }
        }
        System.out.println("Total users loaded: " + users.size());
    }

    public User authenticateUser(String username, String password) {
        System.out.println("=== Authentication Attempt ===");
        System.out.println("Username: [" + username + "]");
        System.out.println("Password: [" + password + "]");
        
        List<String> users = fileHandler.readUsers();
        System.out.println("Total users found: " + users.size());
        
        for (String userLine : users) {
            System.out.println("\nChecking user line: [" + userLine + "]");
            String[] parts = userLine.split(",");
            System.out.println("Parts after split: " + parts.length);
            
            if (parts.length >= 5) {
                int userId = Integer.parseInt(parts[0]);
                String storedUsername = parts[1].trim();
                String storedPassword = parts[2].trim();
                String email = parts[3].trim();
                boolean admin = Boolean.parseBoolean(parts[4].trim());
                
                System.out.println("Stored username: [" + storedUsername + "]");
                System.out.println("Stored password: [" + storedPassword + "]");
                System.out.println("Stored email: [" + email + "]");
                System.out.println("Stored admin: [" + admin + "]");
                
                boolean usernameMatch = storedUsername.equals(username.trim());
                boolean passwordMatch = storedPassword.equals(password.trim());
                
                System.out.println("Username match: " + usernameMatch);
                System.out.println("Password match: " + passwordMatch);
                
                if (usernameMatch && passwordMatch) {
                    System.out.println("Authentication successful!");
                    return new User(userId, storedUsername, storedPassword, email, admin);
                }
            }
        }
        
        System.out.println("Authentication failed - no matching credentials found");
        return null;
    }

    public int getTotalUsers() {
        return users.size();
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public User getUserById(int userId) {
        return users.stream()
            .filter(u -> u.getUserId() == userId)
            .findFirst()
            .orElse(null);
    }

    public User getUserByUsername(String username) {
        return users.stream()
            .filter(u -> u.getUsername().equals(username))
            .findFirst()
            .orElse(null);
    }

    public void registerUser(String username, String password, String email, boolean isAdmin) {
        // Check if username already exists
        if (getUserByUsername(username) != null) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Generate new user ID
        int newId = users.isEmpty() ? 1 : users.get(users.size() - 1).getUserId() + 1;

        // Create new user
        User newUser = new User(newId, username, password, email, isAdmin);
        users.add(newUser);

        // Save to file
        fileHandler.writeUser(newUser.toString(), true);
    }

    public boolean validateUser(String username, String password) {
        User user = getUserByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    public void updateUser(User user) {
        User existingUser = getUserById(user.getUserId());
        if (existingUser != null) {
            users.remove(existingUser);
                    users.add(user);
            
            // Update file
            List<String> lines = fileHandler.readUsers();
            List<String> updatedLines = lines.stream()
                .map(line -> {
                    String[] parts = line.split(",");
                    if (parts.length >= 1 && Integer.parseInt(parts[0]) == user.getUserId()) {
                        return user.toString();
                    }
                    return line;
                })
                .collect(Collectors.toList());
            fileHandler.writeUser(String.join("\n", updatedLines), false);
        }
    }

    public void deleteUser(int userId) {
        users.removeIf(u -> u.getUserId() == userId);
        
        // Update file
        List<String> lines = fileHandler.readUsers();
        List<String> updatedLines = lines.stream()
            .filter(line -> {
                String[] parts = line.split(",");
                return parts.length >= 1 && Integer.parseInt(parts[0]) != userId;
            })
            .collect(Collectors.toList());
        fileHandler.writeUser(String.join("\n", updatedLines), false);
    }

    public void reloadUsers() {
        users.clear();
        loadUsers();
    }
}