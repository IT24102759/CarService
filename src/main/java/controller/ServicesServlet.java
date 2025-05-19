package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import service.ServiceManager;
import service.MaintenanceService;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/services")
public class ServicesServlet extends HttpServlet {
    private ServiceManager serviceManager;
    private MaintenanceService maintenanceService;

    @Override
    public void init() throws ServletException {
        serviceManager = new ServiceManager(getServletContext());
        maintenanceService = new MaintenanceService(getServletContext());
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
            // Get all available services
            request.setAttribute("services", serviceManager.getAllServices());
            
            // Get user's service history
            request.setAttribute("serviceHistory", serviceManager.getUserServiceHistory(user.getUserId()));
            
            // Get upcoming maintenance reminders
            request.setAttribute("reminders", maintenanceService.getMaintenanceReminders(user.getUserId()));
            
            // Forward to services page
            request.getRequestDispatcher("/pages/services.jsp").forward(request, response);
        } catch (Exception e) {
            session.setAttribute("error", "Error loading services: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/pages/error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
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
                    handleScheduleService(request, response, user);
                    break;
                case "record":
                    handleRecordService(request, response, user);
                    break;
                default:
                    session.setAttribute("error", "Invalid action specified");
                    response.sendRedirect(request.getContextPath() + "/pages/error.jsp");
            }
        } catch (Exception e) {
            session.setAttribute("error", "Error processing request: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/pages/error.jsp");
        }
    }

    private void handleScheduleService(HttpServletRequest request, HttpServletResponse response, User user) 
            throws IOException {
        int serviceId = Integer.parseInt(request.getParameter("serviceId"));
        int vehicleId = Integer.parseInt(request.getParameter("vehicleId"));
        
        maintenanceService.scheduleMaintenance(user.getUserId(), vehicleId, serviceId);
        response.sendRedirect(request.getContextPath() + "/services");
    }

    private void handleRecordService(HttpServletRequest request, HttpServletResponse response, User user) 
            throws IOException {
        int serviceId = Integer.parseInt(request.getParameter("serviceId"));
        int vehicleId = Integer.parseInt(request.getParameter("vehicleId"));
        double cost = Double.parseDouble(request.getParameter("cost"));
        
        serviceManager.addServiceRecord(user.getUserId(), vehicleId, serviceId, LocalDate.now(), cost);
        response.sendRedirect(request.getContextPath() + "/services");
    }
}
