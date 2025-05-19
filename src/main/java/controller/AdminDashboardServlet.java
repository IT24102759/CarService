package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import model.Vehicle;
import service.UserService;
import service.VehicleService;
import service.ServiceManager;
import service.MaintenanceService;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private UserService userService;
    private VehicleService vehicleService;
    private ServiceManager serviceManager;
    private MaintenanceService maintenanceService;

    @Override
    public void init() throws ServletException {
        userService = new UserService(getServletContext());
        vehicleService = new VehicleService(getServletContext());
        serviceManager = new ServiceManager(getServletContext());
        maintenanceService = new MaintenanceService(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Check if user is admin
        if (user == null || !user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/pages/login.jsp");
            return;
        }

        vehicleService.reloadVehicles();

        try {
            System.out.println("=== Loading Admin Dashboard ===");
            
            // Get statistics
            int totalUsers = userService.getTotalUsers();
            int totalVehicles = vehicleService.getTotalVehicles();
            int totalServices = serviceManager.getTotalServices();
            double totalRevenue = serviceManager.getTotalRevenue();
            
            System.out.println("Total users: " + totalUsers);
            System.out.println("Total vehicles: " + totalVehicles);
            System.out.println("Total services: " + totalServices);
            System.out.println("Total revenue: " + totalRevenue);

            // Get recent activities
            List<Map<String, String>> recentActivities = getRecentActivities();

            // Get user list with their statistics
            List<Map<String, Object>> users = getUserStatistics();
            System.out.println("User statistics: " + users.size() + " users");

            // Get all users and vehicles
            List<User> userList = userService.getAllUsers();
            List<Vehicle> vehicleList = vehicleService.getAllVehicles();
            
            System.out.println("User list size: " + userList.size());
            for (User u : userList) {
                System.out.println("User: " + u.getUsername() + " (ID: " + u.getUserId() + ")");
            }
            
            System.out.println("Vehicle list size: " + vehicleList.size());
            for (Vehicle v : vehicleList) {
                System.out.println("Vehicle: " + v.getMake() + " " + v.getModel() + " (ID: " + v.getVehicleId() + ", User: " + v.getUserId() + ")");
            }

            // Set attributes
            request.setAttribute("totalUsers", totalUsers);
            request.setAttribute("totalVehicles", totalVehicles);
            request.setAttribute("totalServiceRecords", serviceManager.getAllServiceRecords().size());
            request.setAttribute("totalRevenue", totalRevenue);
            request.setAttribute("recentActivities", recentActivities);
            request.setAttribute("users", users);
            request.setAttribute("vehicles", vehicleList);
            request.setAttribute("userList", userList);
            request.setAttribute("vehicleList", vehicleList);
            request.setAttribute("services", serviceManager.getAllServices());
            request.setAttribute("serviceRecords", serviceManager.getAllServiceRecords());

            // Forward to admin dashboard
            request.getRequestDispatcher("/pages/adminDashboard.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("Error loading dashboard: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("error", "Error loading dashboard: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/pages/error.jsp");
        }
    }

    private List<Map<String, String>> getRecentActivities() {
        List<Map<String, String>> activities = new ArrayList<>();
        
        // Add some sample activities (replace with actual data from your services)
        Map<String, String> activity1 = new HashMap<>();
        activity1.put("icon", "fa-car");
        activity1.put("description", "New vehicle registered: Toyota Camry 2023");
        activity1.put("timestamp", "2 minutes ago");
        activity1.put("status", "Completed");
        activity1.put("statusClass", "bg-green-100 text-green-800");
        activities.add(activity1);

        Map<String, String> activity2 = new HashMap<>();
        activity2.put("icon", "fa-tools");
        activity2.put("description", "Service completed: Oil Change");
        activity2.put("timestamp", "15 minutes ago");
        activity2.put("status", "Completed");
        activity2.put("statusClass", "bg-green-100 text-green-800");
        activities.add(activity2);

        Map<String, String> activity3 = new HashMap<>();
        activity3.put("icon", "fa-user");
        activity3.put("description", "New user registration: John Doe");
        activity3.put("timestamp", "1 hour ago");
        activity3.put("status", "Pending");
        activity3.put("statusClass", "bg-yellow-100 text-yellow-800");
        activities.add(activity3);

        return activities;
    }

    private List<Map<String, Object>> getUserStatistics() {
        List<Map<String, Object>> users = new ArrayList<>();
        List<User> allUsers = userService.getAllUsers();

        for (User user : allUsers) {
            Map<String, Object> userStats = new HashMap<>();
            userStats.put("name", user.getUsername());
            userStats.put("email", user.getEmail());
            userStats.put("vehicleCount", vehicleService.getUserVehicleCount(user.getUserId()));
            userStats.put("serviceCount", serviceManager.getUserServiceCount(user.getUserId()));
            userStats.put("status", "Active");
            userStats.put("statusClass", "bg-green-100 text-green-800");
            users.add(userStats);
        }

        return users;
    }
} 