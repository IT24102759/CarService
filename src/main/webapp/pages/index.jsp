<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AutoTrack | Car Service & Maintenance Tracker</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            min-height: 100vh;
            background: linear-gradient(135deg, #e6e9f0, #eef1f5);
            padding: 1rem;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
        }
        .nav {
            background: white;
            padding: 1rem;
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-radius: 0.5rem;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            margin-bottom: 2rem;
        }
        .nav .logo {
            display: flex;
            align-items: center;
        }
        .nav .logo svg {
            width: 28px;
            height: 28px;
            color: #2563eb;
        }
        .nav .logo span {
            font-size: 1.5rem;
            font-weight: 600;
            color: #1f2937;
            margin-left: 0.75rem;
        }
        .nav ul {
            display: flex;
            list-style: none;
            gap: 1.5rem;
        }
        .nav ul li a {
            color: #374151;
            text-decoration: none;
            font-size: 1rem;
            padding: 0.5rem 1rem;
            transition: background 0.3s ease, color 0.3s ease;
        }
        .nav ul li a:hover {
            background: #f3f4f6;
            color: #2563eb;
            border-radius: 0.25rem;
        }
        .header {
            text-align: center;
            margin-bottom: 2rem;
        }
        .header h1 {
            font-size: 2.25rem;
            color: #1f2937;
            font-weight: 600;
        }
        .header p {
            font-size: 1.125rem;
            color: #6b7280;
            margin-top: 0.5rem;
        }
        .logout {
            background: #2644dc;
            color: white;
            padding: 0.5rem 1.5rem;
            border-radius: 0.25rem;
            text-decoration: none;
            font-size: 0.9rem;
            transition: background 0.3s ease;
        }
        .logout:hover {
            background: #050c3e;
        }
        .guest-container {
            background: white;
            padding: 2.5rem;
            border-radius: 0.5rem;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            text-align: center;
            max-width: 600px;
            margin: 0 auto;
        }
        .guest-container h2 {
            font-size: 1.5rem;
            color: #1f2937;
            margin-bottom: 1rem;
        }
        .guest-container p {
            color: #6b7280;
            font-size: 1rem;
            margin-bottom: 1.5rem;
        }
        .guest-container .btn {
            display: inline-block;
            padding: 0.75rem 1.5rem;
            border-radius: 0.25rem;
            text-decoration: none;
            font-size: 1rem;
            font-weight: 500;
            margin: 0.5rem;
            transition: transform 0.2s ease, background 0.3s ease;
        }
        .guest-container .btn:hover {
            transform: translateY(-2px);
        }
        .login-btn-admin {
            background: #2563eb;
            color: white;
        }
        .login-btn-admin:hover {
            background: #1d4ed8;
        }
        .login-btn-user {
            background: #16a34a;
            color: white;
        }
        .login-btn-user:hover {
            background: #15803d;
        }
        .register-btn {
            background: #6b7280;
            color: white;
        }
        .register-btn:hover {
            background: #4b5563;
        }
        .admin-nav ul li a {
            color: #2563eb;
        }
        .admin-nav ul li a:hover {
            background: #dbeafe;
        }
        .user-nav ul li a {
            color: #16a34a;
        }
        .user-nav ul li a:hover {
            background: #dcfce7;
        }
        .hero {
            text-align: center;
            margin-bottom: 3rem;
        }
        .hero img {
            max-width: 100%;
            height: auto;
            border-radius: 0.5rem;
            margin-bottom: 1rem;
        }
        @media (max-width: 640px) {
            .nav {
                flex-direction: column;
                gap: 1rem;
            }
            .nav ul {
                flex-direction: column;
                align-items: center;
            }
            .header h1 {
                font-size: 1.75rem;
            }
            .guest-container {
                padding: 1.5rem;
            }
            .guest-container .btn {
                display: block;
                margin: 0.5rem auto;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <c:choose>
        <c:when test="${not empty sessionScope.user}">
            <c:choose>
                <c:when test="${sessionScope.user.role == 'admin'}">
                    <!-- Admin UI -->
                    <div class="nav admin-nav">
                        <div class="logo">
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 8h8m0 0h2a2 2 0 012 2v6a2 2 0 01-2 2h-2m-8 0H6a2 2 0 01-2-2v-6a2 2 0 012-2h2m0 0V6a2 2 0 012-2h4a2 2 0 012 2v2" />
                            </svg>
                            <span>AutoTrack</span>
                        </div>
                        <ul>
                            <li><a href="#">User Management</a></li>
                            <li><a href="#">Service Records</a></li>
                            <li><a href="reports">Reports</a></li>
                        </ul>
                    </div>
                    <div class="header">
                        <h1>Welcome, ${sessionScope.user.username}</h1>
                        <a href="logout" class="logout">Logout</a>
                    </div>
                    <jsp:include page="/admin"/>
                </c:when>
                <c:otherwise>
                    <!-- User UI -->
                    <div class="nav user-nav">
                        <div class="logo">
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 8h8m0 0h2a2 2 0 012 2v6a2 2 0 01-2 2h-2m-8 0H6a2 2 0 01-2-2v-6a2 2 0 012-2h2m0 0V6a2 2 0 012-2h4a2 2 0 012 2v2" />
                            </svg>
                            <span>AutoTrack</span>
                        </div>
                        <ul>
                            <li><a href="user">Dashboard</a></li>
                            <li><a href="vehicle">Vehicles</a></li>
                            <li><a href="services">Services</a></li>
                            <li><a href="reports">Reports</a></li>
                        </ul>
                    </div>
                    <div class="header">
                        <h1>Welcome, ${sessionScope.user.username}</h1>
                        <a href="logout" class="logout">Logout</a>
                    </div>
                    <jsp:include page="/user"/>
                </c:otherwise>
            </c:choose>
        </c:when>
        <c:otherwise>
            <!-- Guest UI -->
            <div class="nav">
                <div class="logo">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 8h8m0 0h2a2 2 0 012 2v6a2 2 0 01-2 2h-2m-8 0H6a2 2 0 01-2-2v-6a2 2 0 012-2h2m0 0V6a2 2 0 012-2h4a2 2 0 012 2v2" />
                    </svg>
                    <span>AutoTrack</span>
                </div>
                <ul>
                    <li><a href="index.jsp">Home</a></li>
                    <li><a href="services">Services</a></li>
                    <li><a href="#">Contact</a></li>
                </ul>
            </div>
            <div class="hero" style="position: relative; min-height: 260px;">
                <h1 class="text-3xl font-bold text-gray-800 mb-4" style="z-index: 2; position: relative;">Welcome to AutoTrack</h1>
                <img src="assets/audi-at-heart.jpg" alt="Engine Heart" style="position: absolute; right: 10%; top: 10px; width: 180px; max-width: 40vw; box-shadow: 0 8px 32px rgba(0,0,0,0.18); border-radius: 1rem; transform: rotate(-8deg); z-index: 1; opacity: 0.97;" />
                <p class="text-lg text-gray-600 mb-6" style="z-index: 2; position: relative;">Track your car maintenance and services with ease.</p>
            </div>
            <div class="guest-container">
                <h2 class="font-semibold">Get Started</h2>
                <p>Log in as an admin or user, or create a new account to start tracking.</p>
                <a href="login?role=admin" class="btn login-btn-admin">Admin Login</a>
                <a href="login?role=user" class="btn login-btn-user">User Login</a>
                <a href="register.jsp" class="btn register-btn">Register</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>