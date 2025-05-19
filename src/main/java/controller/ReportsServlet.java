package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import model.Vehicle;
import model.ServiceRecord;
import model.MaintenanceReminder;
import service.ServiceManager;
import service.VehicleService;
import service.MaintenanceService;
import service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet("/reports")
public class ReportsServlet extends HttpServlet {
    private ServiceManager serviceManager;
    private VehicleService vehicleService;
    private MaintenanceService maintenanceService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        serviceManager = new ServiceManager(getServletContext());
        vehicleService = new VehicleService(getServletContext());
        maintenanceService = new MaintenanceService(getServletContext());
        userService = new UserService(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/pages/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        try {
            // Get user's vehicles
            List<Vehicle> vehicles = vehicleService.getUserVehicles(user.getUserId());
            request.setAttribute("vehicles", vehicles);

            // Get service history
            List<ServiceRecord> serviceHistory = serviceManager.getUserServiceHistory(user.getUserId());
            request.setAttribute("serviceHistory", serviceHistory);

            // Generate user history report
            List<String> userHistoryReport = new ArrayList<>();
            for (ServiceRecord record : serviceHistory) {
                String serviceName = serviceManager.getServiceById(record.getServiceId()).getName();
                userHistoryReport.add(String.format("Service: %s, Date: %s, Cost: $%.2f", 
                    serviceName, record.getServiceDate(), record.getCost()));
            }
            request.setAttribute("userHistoryReport", userHistoryReport);

            // Generate vehicle summary
            List<String> vehicleSummary = new ArrayList<>();
            for (Vehicle vehicle : vehicles) {
                double vehicleTotalCost = serviceHistory.stream()
                    .filter(record -> record.getUserId() == user.getUserId())
                    .mapToDouble(ServiceRecord::getCost)
                    .sum();
                vehicleSummary.add(String.format("Vehicle: %s %s (%s) - Total Service Cost: $%.2f", 
                    vehicle.getMake(), vehicle.getModel(), vehicle.getLicensePlate(), vehicleTotalCost));
            }
            request.setAttribute("vehicleSummary", vehicleSummary);

            // Calculate total service cost
            double totalCost = serviceHistory.stream()
                    .mapToDouble(ServiceRecord::getCost)
                    .sum();
            request.setAttribute("totalCost", totalCost);

            // Get service distribution
            Map<String, Long> serviceDistribution = serviceHistory.stream()
                    .collect(Collectors.groupingBy(
                            record -> serviceManager.getServiceById(record.getServiceId()).getName(),
                            Collectors.counting()
                    ));
            request.setAttribute("serviceDistribution", serviceDistribution);

            // Get upcoming maintenance
            List<MaintenanceReminder> reminders = maintenanceService.getMaintenanceReminders(user.getUserId());
            request.setAttribute("reminders", reminders);

            // Generate total cost per user report (for admin)
            if (user.isAdmin()) {
                Map<String, Double> totalCostReport = new HashMap<>();
                List<User> allUsers = userService.getAllUsers();
                for (User u : allUsers) {
                    if (!u.isAdmin()) {
                        double userTotalCost = serviceManager.getUserServiceHistory(u.getUserId()).stream()
                            .mapToDouble(ServiceRecord::getCost)
                            .sum();
                        totalCostReport.put(u.getUsername(), userTotalCost);
                    }
                }
                request.setAttribute("totalCostReport", totalCostReport);
            }

            // Forward to reports page
            request.getRequestDispatcher("/pages/reports.jsp").forward(request, response);
        } catch (Exception e) {
            session.setAttribute("error", "Error loading reports: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/pages/error.jsp");
        }
    }
}