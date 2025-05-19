package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import model.Vehicle;
import model.Service;
import service.UserService;
import service.VehicleService;
import service.ServiceManager;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {
    private UserService userService;
    private VehicleService vehicleService;
    private ServiceManager serviceManager;

    @Override
    public void init() throws ServletException {
        userService = new UserService(getServletContext());
        vehicleService = new VehicleService(getServletContext());
        serviceManager = new ServiceManager(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (pathInfo == null || pathInfo.equals("/")) {
            // Show admin dashboard
            request.setAttribute("totalUsers", userService.getTotalUsers());
            request.setAttribute("totalVehicles", vehicleService.getTotalVehicles());
            request.setAttribute("totalServices", serviceManager.getTotalServices());
            request.setAttribute("totalRevenue", serviceManager.getTotalRevenue());
            request.getRequestDispatcher("/pages/admin/dashboard.jsp").forward(request, response);
        } else if (pathInfo.equals("/users")) {
            // Show user management page
            List<User> users = userService.getAllUsers();
            request.setAttribute("users", users);
            request.getRequestDispatcher("/pages/admin/users.jsp").forward(request, response);
        } else if (pathInfo.equals("/vehicles")) {
            // Show vehicle management page
            List<Vehicle> vehicles = vehicleService.getAllVehicles();
            request.setAttribute("vehicles", vehicles);
            request.getRequestDispatcher("/pages/admin/vehicles.jsp").forward(request, response);
        } else if (pathInfo.equals("/services")) {
            // Show service management page
            List<Service> services = serviceManager.getAllServices();
            request.setAttribute("services", services);
            request.getRequestDispatcher("/pages/admin/services.jsp").forward(request, response);
            } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendRedirect(request.getContextPath() + "/admin");
        } else if (pathInfo.equals("/add-user")) {
            handleAddUser(request, response);
        } else if (pathInfo.equals("/edit-user")) {
            handleEditUser(request, response);
        } else if (pathInfo.equals("/delete-user")) {
            handleDeleteUser(request, response);
        } else if (pathInfo.equals("/add-vehicle")) {
            handleAddVehicle(request, response);
        } else if (pathInfo.equals("/edit-vehicle")) {
            handleEditVehicle(request, response);
        } else if (pathInfo.equals("/delete-vehicle")) {
            handleDeleteVehicle(request, response);
        } else if (pathInfo.equals("/add-service")) {
            handleAddService(request, response);
        } else if (pathInfo.equals("/edit-service")) {
            handleEditService(request, response);
        } else if (pathInfo.equals("/delete-service")) {
            handleDeleteService(request, response);
            } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }

    private void handleAddUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        boolean isAdmin = "on".equals(request.getParameter("isAdmin"));

        try {
            userService.registerUser(username, password, email, isAdmin);
            response.sendRedirect(request.getContextPath() + "/admin/users?success=true");
        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath() + "/admin/users?error=" + e.getMessage());
        }
    }

    private void handleEditUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        boolean isAdmin = "on".equals(request.getParameter("isAdmin"));

        User user = userService.getUserById(userId);
        if (user != null) {
            user.setUsername(username);
            user.setEmail(email);
            user.setAdmin(isAdmin);
            userService.updateUser(user);
            response.sendRedirect(request.getContextPath() + "/admin/users?success=true");
            } else {
            response.sendRedirect(request.getContextPath() + "/admin/users?error=User not found");
            }
        }

    private void handleDeleteUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        userService.deleteUser(userId);
        response.sendRedirect(request.getContextPath() + "/admin/users?success=true");
    }

    private void handleAddVehicle(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        String make = request.getParameter("make");
        String model = request.getParameter("model");
        int year = Integer.parseInt(request.getParameter("year"));
        String licensePlate = request.getParameter("licensePlate");
        int mileage = Integer.parseInt(request.getParameter("mileage"));

        Vehicle vehicle = new Vehicle(0, userId, make, model, year, licensePlate, mileage);
        vehicleService.addVehicle(vehicle);
        response.sendRedirect(request.getContextPath() + "/admin/vehicles?success=true");
    }

    private void handleEditVehicle(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int vehicleId = Integer.parseInt(request.getParameter("vehicleId"));
        String make = request.getParameter("make");
        String model = request.getParameter("model");
        int year = Integer.parseInt(request.getParameter("year"));
        String licensePlate = request.getParameter("licensePlate");
        int mileage = Integer.parseInt(request.getParameter("mileage"));

        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
        if (vehicle != null) {
            vehicle.setMake(make);
            vehicle.setModel(model);
            vehicle.setYear(year);
            vehicle.setLicensePlate(licensePlate);
            vehicle.setMileage(mileage);
            vehicleService.updateVehicle(vehicle);
            response.sendRedirect(request.getContextPath() + "/admin/vehicles?success=true");
            } else {
            response.sendRedirect(request.getContextPath() + "/admin/vehicles?error=Vehicle not found");
            }
        }

    private void handleDeleteVehicle(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int vehicleId = Integer.parseInt(request.getParameter("vehicleId"));
        vehicleService.deleteVehicle(vehicleId);
        response.sendRedirect(request.getContextPath() + "/admin/vehicles?success=true");
    }

    private void handleAddService(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        double cost = Double.parseDouble(request.getParameter("cost"));

        serviceManager.addService(name, description, cost);
        response.sendRedirect(request.getContextPath() + "/admin/services?success=true");
    }

    private void handleEditService(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int serviceId = Integer.parseInt(request.getParameter("serviceId"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        double cost = Double.parseDouble(request.getParameter("cost"));

        Service service = serviceManager.getServiceById(serviceId);
        if (service != null) {
            service.setName(name);
            service.setDescription(description);
            service.setCost(cost);
            serviceManager.updateService(service);
            response.sendRedirect(request.getContextPath() + "/admin/services?success=true");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/services?error=Service not found");
        }
    }

    private void handleDeleteService(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int serviceId = Integer.parseInt(request.getParameter("serviceId"));
        serviceManager.deleteService(serviceId);
        response.sendRedirect(request.getContextPath() + "/admin/services?success=true");
    }
}