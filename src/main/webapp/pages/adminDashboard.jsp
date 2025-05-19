<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:if test="${empty sessionScope.user || !sessionScope.user.admin}">
    <c:redirect url="/pages/login.jsp"/>
</c:if>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AutoTrack | Admin Dashboard</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background: linear-gradient(135deg, #e6f0ff, #f0f5ff);
        }
        .stat-card {
            transition: transform 0.3s ease;
            background: white;
            border: 1px solid #bfdbfe;
        }
        .stat-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 4px 6px rgba(37, 99, 235, 0.1);
        }
        .stat-card h3 {
            color: #1e40af;
        }
        .stat-card p {
            color: #2563eb;
        }
    </style>
</head>
<body>
    <div class="main-content">
        <!-- Header -->
        <header class="text-center mb-8">
            <h1 class="text-2xl font-semibold text-gray-800">Welcome, ${sessionScope.user.username}</h1>
            <p class="text-gray-600">Admin Dashboard</p>
        </header>

        <!-- Summary Section -->
        <section id="summary" class="mb-8">
            <h2 class="text-lg font-semibold text-gray-800 mb-4">Dashboard Summary</h2>
            <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
                <div class="stat-card bg-white shadow rounded-lg p-6 text-center">
                    <h3 class="text-sm font-medium text-gray-600">Total Repairs</h3>
                    <p class="text-2xl font-semibold text-blue-600">${totalServiceRecords}</p>
                </div>
                <div class="stat-card bg-white shadow rounded-lg p-6 text-center">
                    <h3 class="text-sm font-medium text-gray-600">Total Vehicles</h3>
                    <p class="text-2xl font-semibold text-blue-600">${totalVehicles}</p>
                </div>
                <div class="stat-card bg-white shadow rounded-lg p-6 text-center">
                    <h3 class="text-sm font-medium text-gray-600">Total Users</h3>
                    <p class="text-2xl font-semibold text-blue-600">${totalUsers}</p>
                </div>
            </div>
        </section>

        <!-- Services Report Section -->
        <section id="services-report" class="mb-8">
            <h2 class="text-lg font-semibold text-gray-800 mb-4">Services Report</h2>
            <table class="w-full text-sm text-gray-600 bg-white rounded-lg">
                <thead>
                    <tr class="bg-gray-100">
                        <th class="p-3 text-left">Service ID</th>
                        <th class="p-3 text-left">Name</th>
                        <th class="p-3 text-left">Cost</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="service" items="${services}">
                        <tr class="border-b">
                            <td class="p-3">${service.serviceId}</td>
                            <td class="p-3">${service.name}</td>
                            <td class="p-3">$${service.cost}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </section>

        <!-- Vehicles Report Section -->
        <section id="vehicles-report" class="mb-8">
            <h2 class="text-lg font-semibold text-gray-800 mb-4">Vehicles Report</h2>
            <table class="w-full text-sm text-gray-600 bg-white rounded-lg">
                <thead>
                    <tr class="bg-gray-100">
                        <th class="p-3 text-left">Vehicle ID</th>
                        <th class="p-3 text-left">User ID</th>
                        <th class="p-3 text-left">Make</th>
                        <th class="p-3 text-left">Model</th>
                        <th class="p-3 text-left">Mileage</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="vehicle" items="${vehicleList}">
                        <tr class="border-b">
                            <td class="p-3">${vehicle.vehicleId}</td>
                            <td class="p-3">${vehicle.userId}</td>
                            <td class="p-3">${vehicle.make}</td>
                            <td class="p-3">${vehicle.model}</td>
                            <td class="p-3">${vehicle.mileage}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </section>
    </div>
</body>
</html>