package service;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import model.ServiceRecord;
import model.User;
import model.Vehicle;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportsService {
    private final ServiceManager serviceManager;
    private final VehicleService vehicleService;
    private final UserService userService;

    public ReportsService(ServletContext context) {
        this.serviceManager = new ServiceManager(context);
        this.vehicleService = new VehicleService(context);
        this.userService = new UserService(context);
    }

    public String generateUserServiceHistoryReport(int userId) {
        List<ServiceRecord> serviceHistory = serviceManager.getUserServiceHistory(userId);
        List<Vehicle> userVehicles = vehicleService.getUserVehicles(userId);
        
        StringBuilder report = new StringBuilder();
        report.append("Service History Report\n");
        report.append("=====================\n\n");
        
        if (serviceHistory.isEmpty()) {
            report.append("No service records found.\n");
            return report.toString();
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        for (ServiceRecord record : serviceHistory) {
            report.append("Date: ").append(record.getServiceDate().format(formatter)).append("\n");
            report.append("Service: ").append(serviceManager.getServiceById(record.getServiceId()).getName()).append("\n");
            report.append("Cost: $").append(String.format("%.2f", record.getCost())).append("\n");
            report.append("-------------------\n");
        }
        
        return report.toString();
    }

    public String generateVehicleServiceSummaryReport() {
        List<Vehicle> allVehicles = vehicleService.getAllVehicles();
        StringBuilder report = new StringBuilder();
        report.append("Vehicle Service Summary Report\n");
        report.append("============================\n\n");
        
        for (Vehicle vehicle : allVehicles) {
            List<ServiceRecord> vehicleServices = serviceManager.getUserServiceHistory(vehicle.getUserId())
                .stream()
                .filter(record -> record.getVehicleId() == vehicle.getVehicleId())
                .collect(Collectors.toList());
            
            report.append("Vehicle: ").append(vehicle.getMake()).append(" ").append(vehicle.getModel())
                  .append(" (").append(vehicle.getYear()).append(")\n");
            report.append("Total Services: ").append(vehicleServices.size()).append("\n");
            report.append("Total Cost: $")
                  .append(String.format("%.2f", vehicleServices.stream().mapToDouble(ServiceRecord::getCost).sum()))
                  .append("\n");
            report.append("-------------------\n");
        }
        
        return report.toString();
    }

    public String generateTotalCostPerUserReport() {
        List<User> allUsers = userService.getAllUsers();
        StringBuilder report = new StringBuilder();
        report.append("Total Cost Per User Report\n");
        report.append("========================\n\n");
        
        for (User user : allUsers) {
            List<ServiceRecord> userServices = serviceManager.getUserServiceHistory(user.getUserId());
            double totalCost = userServices.stream().mapToDouble(ServiceRecord::getCost).sum();
            
            report.append("User: ").append(user.getUsername()).append("\n");
            report.append("Total Services: ").append(userServices.size()).append("\n");
            report.append("Total Cost: $").append(String.format("%.2f", totalCost)).append("\n");
            report.append("-------------------\n");
        }
        
        return report.toString();
    }

    public void prepareReportsData(HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null) {
            return;
        }

        // Get user's service history
        List<ServiceRecord> serviceHistory = serviceManager.getUserServiceHistory(currentUser.getUserId());
        request.setAttribute("serviceHistory", serviceHistory);

        // Get vehicle service summary
        List<Vehicle> userVehicles = vehicleService.getUserVehicles(currentUser.getUserId());
        Map<Integer, List<ServiceRecord>> vehicleServices = userVehicles.stream()
            .collect(Collectors.toMap(
                Vehicle::getVehicleId,
                vehicle -> serviceHistory.stream()
                    .filter(record -> record.getVehicleId() == vehicle.getVehicleId())
                    .collect(Collectors.toList())
            ));
        request.setAttribute("vehicleServices", vehicleServices);

        // Get total cost per user (admin only)
        if (currentUser.isAdmin()) {
            Map<Integer, Double> totalCostReport = userService.getAllUsers().stream()
                .collect(Collectors.toMap(
                    User::getUserId,
                    user -> serviceManager.getUserServiceHistory(user.getUserId()).stream()
                        .mapToDouble(ServiceRecord::getCost)
                        .sum()
                ));
            request.setAttribute("totalCostReport", totalCostReport);
        }
    }
}