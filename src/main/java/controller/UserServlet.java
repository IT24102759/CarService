package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        // Check if user is authenticated and has user role
        if (user == null || user.isAdmin()) {
            System.out.println("UserServlet: Unauthorized access attempt");
            response.sendRedirect("pages/login.jsp");
            return;
        }

        // Prepare placeholder data for user dashboard
        List<String> vehicles = Arrays.asList("Toyota Camry 2020", "Honda Civic 2018");
        List<String> services = Arrays.asList("Oil Change - 2025-03-15", "Tire Rotation - 2025-02-10");

        request.setAttribute("vehicles", vehicles);
        request.setAttribute("services", services);

        System.out.println("UserServlet: Forwarding to userDashboard.jsp for user " + user.getUsername());
        request.getRequestDispatcher("/pages/userDashboard.jsp").forward(request, response);
    }
}