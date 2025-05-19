package controller;

import model.User;
import service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() {
        userService = new UserService(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if this is a logout request or role switch
        String logout = request.getParameter("logout");
        String role = request.getParameter("role");
        
        // If it's a logout request or role switch, invalidate the session
        if (logout != null && logout.equals("true")) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            // If role is specified, set it and forward to login page
            if (role != null) {
                request.setAttribute("role", role);
                request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
                return;
            }
            response.sendRedirect(request.getContextPath() + "/pages/login.jsp");
            return;
        }

        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            if (user.isAdmin()) {
                response.sendRedirect(request.getContextPath() + "/pages/adminDashboard.jsp");
            } else {
                response.sendRedirect(request.getContextPath() + "/pages/userDashboard.jsp");
            }
            return;
        }

        request.setAttribute("role", role);
        request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        System.out.println("=== Login Attempt ===");
        System.out.println("Username: [" + username + "]");
        System.out.println("Password: [" + password + "]");
        System.out.println("Role: [" + role + "]");

        if (role == null || (!role.equals("admin") && !role.equals("user"))) {
            System.out.println("Invalid role: " + role);
            request.setAttribute("error", "Invalid role selected");
            request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
            return;
        }

        User user = userService.authenticateUser(username, password);
        System.out.println("Authentication result: " + (user != null ? "Success" : "Failed"));
        if (user == null) {
            request.setAttribute("error", "Invalid username or password");
            request.setAttribute("role", role);
            request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
            return;
        }

        System.out.println("User is admin: " + user.isAdmin());
        if ((user.isAdmin() && !role.equals("admin")) || (!user.isAdmin() && !role.equals("user"))) {
            System.out.println("Role mismatch - User is admin: " + user.isAdmin() + ", Selected role: " + role);
            request.setAttribute("error", "Invalid role for this user");
            request.setAttribute("role", role);
            request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        System.out.println("Login successful - Redirecting to dashboard");

        if (user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else {
            response.sendRedirect(request.getContextPath() + "/pages/userDashboard.jsp");
        }
    }
}