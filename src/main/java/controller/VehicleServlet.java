package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import model.Vehicle;
import service.VehicleService;

import java.io.IOException;

@WebServlet("/vehicle")
public class VehicleServlet extends HttpServlet {
    private VehicleService vehicleService;

    @Override
    public void init() throws ServletException {
        vehicleService = new VehicleService(getServletContext());
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
            request.setAttribute("vehicles", vehicleService.getUserVehicles(user.getUserId()));
            
            // Forward to vehicle page
            request.getRequestDispatcher("/pages/vehicle.jsp").forward(request, response);
        } catch (Exception e) {
            session.setAttribute("error", "Error loading vehicles: " + e.getMessage());
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
                case "add":
                    handleAddVehicle(request, response, user);
                    break;
                case "update":
                    handleUpdateVehicle(request, response, user);
                    break;
                case "delete":
                    handleDeleteVehicle(request, response, user);
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

    private void handleAddVehicle(HttpServletRequest request, HttpServletResponse response, User user) 
            throws IOException {
        String make = request.getParameter("make");
        String model = request.getParameter("model");
        String licensePlate = request.getParameter("licensePlate");
        int year = Integer.parseInt(request.getParameter("year"));
        int mileage = Integer.parseInt(request.getParameter("mileage"));
        
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new IllegalArgumentException("License plate is required");
        }
        
        // Generate a new vehicle ID (you might want to implement a proper ID generation strategy)
        int newVehicleId = vehicleService.getUserVehicles(user.getUserId()).size() + 1;
        Vehicle vehicle = new Vehicle(newVehicleId, user.getUserId(), make, model, year, licensePlate, mileage);
        vehicleService.addVehicle(vehicle);
        response.sendRedirect(request.getContextPath() + "/vehicle");
    }

    private void handleUpdateVehicle(HttpServletRequest request, HttpServletResponse response, User user) 
            throws IOException {
        int vehicleId = Integer.parseInt(request.getParameter("vehicleId"));
        int mileage = Integer.parseInt(request.getParameter("mileage"));
        
        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
        if (vehicle != null) {
            vehicle.setMileage(mileage);
            vehicleService.updateVehicle(vehicle);
        }
        response.sendRedirect(request.getContextPath() + "/vehicle");
    }

    private void handleDeleteVehicle(HttpServletRequest request, HttpServletResponse response, User user) 
            throws IOException {
        int vehicleId = Integer.parseInt(request.getParameter("vehicleId"));
        
        vehicleService.deleteVehicle(vehicleId);
        response.sendRedirect(request.getContextPath() + "/vehicle");
    }
} 