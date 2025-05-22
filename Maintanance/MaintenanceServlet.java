package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import service.MaintenanceService;
import service.VehicleService;
import service.ServiceManager;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/maintenance")
public class MaintenanceServlet extends HttpServlet {
    private MaintenanceService maintenanceService;
    private VehicleService vehicleService;
    private ServiceManager serviceManager;

    @Override
    public void init() throws ServletException {
        maintenanceService = new MaintenanceService(getServletContext());
        vehicleService = new VehicleService(getServletContext());
        serviceManager = new ServiceManager(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/pages/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        try {
            request.setAttribute("vehicles", vehicleService.getUserVehicles(user.getUserId()));
            request.setAttribute("services", serviceManager.getAllServices());
            request.setAttribute("serviceHistory", serviceManager.getUserServiceHistory(user.getUserId()));
            request.setAttribute("reminders", maintenanceService.getMaintenanceReminders(user.getUserId()));
            request.getRequestDispatcher("/pages/maintenance.jsp").forward(request, response);
        } catch (Exception e) {
            session.setAttribute("error", "Error loading maintenance page: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/pages/error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/pages/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "schedule":
                    handleScheduleMaintenance(request, response, user);
                    break;
                case "record":
                    handleRecordService(request, response, user);
                    break;
                case "remove":
                    handleRemoveReminder(request, response, user);
                    break;
                default:
                    session.setAttribute("error", "Invalid action specified");
                    response.sendRedirect(request.getContextPath() + "/pages/error.jsp");
            }
        } catch (NumberFormatException e) {
            session.setAttribute("error", "Vehicle and Service must be selected.");
            response.sendRedirect(request.getContextPath() + "/maintenance");
        } catch (IllegalArgumentException e) {
            session.setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/maintenance");
        } catch (Exception e) {
            session.setAttribute("error", "Error processing request: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/pages/error.jsp");
        }
    }

    private void handleScheduleMaintenance(HttpServletRequest request, HttpServletResponse response, User user) 
            throws IOException {
        String vehicleIdStr = request.getParameter("vehicleId");
        String serviceIdStr = request.getParameter("serviceId");

        if (vehicleIdStr == null || vehicleIdStr.isEmpty() || serviceIdStr == null || serviceIdStr.isEmpty()) {
            throw new IllegalArgumentException("Vehicle and Service must be selected.");
        }

        int vehicleId = Integer.parseInt(vehicleIdStr);
        int serviceId = Integer.parseInt(serviceIdStr);
        maintenanceService.scheduleMaintenance(user.getUserId(), vehicleId, serviceId);
        maintenanceService.removeMaintenanceReminder(user.getUserId(), vehicleId, serviceId);
        response.sendRedirect(request.getContextPath() + "/maintenance");
    }

    private void handleRecordService(HttpServletRequest request, HttpServletResponse response, User user) 
            throws IOException {
        int serviceId = Integer.parseInt(request.getParameter("serviceId"));
        int vehicleId = Integer.parseInt(request.getParameter("vehicleId"));
        double cost = Double.parseDouble(request.getParameter("cost"));
        serviceManager.addServiceRecord(user.getUserId(), vehicleId, serviceId, LocalDate.now(), cost);
        maintenanceService.removeMaintenanceReminder(user.getUserId(), vehicleId, serviceId);
        response.sendRedirect(request.getContextPath() + "/maintenance");
    }

    private void handleRemoveReminder(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException {
        int serviceId = Integer.parseInt(request.getParameter("serviceId"));
        int vehicleId = Integer.parseInt(request.getParameter("vehicleId"));
        maintenanceService.dismissMaintenanceReminder(user.getUserId(), vehicleId, serviceId);
        response.sendRedirect(request.getContextPath() + "/maintenance");
    }
}