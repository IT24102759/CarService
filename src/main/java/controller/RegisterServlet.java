package controller;

import model.User;
import service.FileHandler;
import service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class RegisterServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() {
        try {
            userService = new UserService(getServletContext());
            System.out.println("RegisterServlet initialized successfully");
        } catch (Exception e) {
            System.err.println("Error initializing RegisterServlet: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Handling GET request for /register");
        try {
            request.getRequestDispatcher("/pages/register.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Error forwarding to register.jsp: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading registration page");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("=== Starting Registration Process ===");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        System.out.println("Received registration request:");
        System.out.println("Username: [" + username + "]");
        System.out.println("Password: [" + (password != null ? "***" : "null") + "]");
        System.out.println("Role: [" + role + "]");

        String error = null;
        if (username == null || username.trim().isEmpty()) {
            error = "Username is required.";
        } else if (password == null || password.trim().isEmpty()) {
            error = "Password is required.";
        } else if (role == null || !role.equals("user")) {
            error = "Invalid role. Only 'user' role is allowed.";
        }

        if (error != null) {
            System.out.println("Validation failed: " + error);
            request.setAttribute("error", error);
            try {
                request.getRequestDispatcher("/pages/register.jsp").forward(request, response);
            } catch (Exception e) {
                System.err.println("Error forwarding to register.jsp after validation failure: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing registration");
            }
            return;
        }

        try {
            userService.registerUser(username, password, username + "@example.com", false);
            System.out.println("User registered successfully, forwarding to login.jsp");
            request.setAttribute("success", "Registration successful! Please log in.");
            request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            System.err.println("Registration failed: " + e.getMessage());
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/pages/register.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Error during registration: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Registration failed: " + e.getMessage());
            try {
                request.getRequestDispatcher("/pages/register.jsp").forward(request, response);
            } catch (Exception ex) {
                System.err.println("Error forwarding to register.jsp after registration failure: " + ex.getMessage());
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing registration");
            }
        }
    }
}