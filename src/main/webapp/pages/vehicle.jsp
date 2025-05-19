<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AutoTrack | Manage Vehicles</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <script>
        tailwind.config = {
            theme: {
                extend: {
                    colors: {
                        primary: '#dc2626',
                        secondary: '#b91c1c',
                        dark: '#171717',
                        light: '#f5f5f5'
                    },
                    fontFamily: {
                        sans: ['Poppins', 'sans-serif'],
                    },
                }
            }
        }
    </script>
</head>
<body class="font-sans min-h-screen bg-light">
    <!-- Navigation Bar -->
    <nav class="bg-white shadow-md">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="flex justify-between h-16">
                <div class="flex items-center">
                    <div class="flex-shrink-0 flex items-center">
                        <svg class="h-8 w-8 text-primary" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 8h8m0 0h2a2 2 0 012 2v6a2 2 0 01-2 2h-2m-8 0H6a2 2 0 01-2-2v-6a2 2 0 012-2h2m0 0V6a2 2 0 012-2h4a2 2 0 012 2v2" />
                        </svg>
                        <span class="ml-2 text-xl font-bold text-dark">AutoTrack</span>
                    </div>
                    <div class="hidden sm:ml-6 sm:flex sm:space-x-8">
                        <a href="user" class="border-transparent text-gray-500 hover:border-gray-300 hover:text-dark inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium">Dashboard</a>
                        <a href="vehicle" class="border-primary text-dark inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium">Vehicles</a>
                        <a href="services" class="border-transparent text-gray-500 hover:border-gray-300 hover:text-dark inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium">Services</a>
                        <a href="maintenance" class="border-transparent text-gray-500 hover:border-gray-300 hover:text-dark inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium">Maintenance</a>
                        <a href="reports" class="border-transparent text-gray-500 hover:border-gray-300 hover:text-dark inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium">Reports</a>
                    </div>
                </div>
                <div class="hidden sm:ml-6 sm:flex sm:items-center">
                    <a href="logout" class="text-dark hover:text-primary px-3 py-2 rounded-md text-sm font-medium">Logout</a>
                </div>
                <div class="-mr-2 flex items-center sm:hidden">
                    <button type="button" class="inline-flex items-center justify-center p-2 rounded-md text-gray-400 hover:text-dark hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-primary" aria-controls="mobile-menu" aria-expanded="false">
                        <span class="sr-only">Open main menu</span>
                        <svg class="block h-6 w-6" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" aria-hidden="true">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
                        </svg>
                    </button>
                </div>
            </div>
        </div>
        <div class="sm:hidden hidden" id="mobile-menu">
            <div class="pt-2 pb-3 space-y-1">
                <a href="user" class="border-transparent text-gray-500 hover:bg-gray-50 hover:border-gray-300 hover:text-dark block pl-3 pr-4 py-2 border-l-4 text-base font-medium">Dashboard</a>
                <a href="vehicle" class="bg-red-50 border-primary text-primary block pl-3 pr-4 py-2 border-l-4 text-base font-medium">Vehicles</a>
                <a href="services" class="border-transparent text-gray-500 hover:bg-gray-50 hover:border-gray-300 hover:text-dark block pl-3 pr-4 py-2 border-l-4 text-base font-medium">Services</a>
                <a href="maintenance" class="border-transparent text-gray-500 hover:bg-gray-50 hover:border-gray-300 hover:text-dark block pl-3 pr-4 py-2 border-l-4 text-base font-medium">Maintenance</a>
                <a href="reports" class="border-transparent text-gray-500 hover:bg-gray-50 hover:border-gray-300 hover:text-dark block pl-3 pr-4 py-2 border-l-4 text-base font-medium">Reports</a>
                <a href="logout" class="block px-4 py-2 text-base font-medium text-gray-500 hover:text-dark hover:bg-gray-100">Logout</a>
            </div>
        </div>
    </nav>

    <!-- Main Content -->
    <div class="max-w-7xl mx-auto py-12 px-4 sm:px-6 lg:px-8">
        <h1 class="text-3xl font-bold text-dark mb-6">Manage Vehicles</h1>

        <!-- Messages -->
        <c:if test="${not empty message}">
            <div class="bg-green-100 border-l-4 border-green-500 text-green-700 p-4 mb-6" role="alert">
                <p>${message}</p>
            </div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 mb-6" role="alert">
                <p>${error}</p>
            </div>
        </c:if>

        <!-- Add Vehicle Form -->
        <div class="bg-white p-6 rounded-lg shadow-md mb-12">
            <h2 class="text-2xl font-semibold text-dark mb-4">Add New Vehicle</h2>
            <form action="vehicle" method="POST">
                <input type="hidden" name="action" value="add">
                <div class="grid grid-cols-1 gap-4 sm:grid-cols-2">
                    <div>
                        <label for="model" class="block text-sm font-medium text-dark">Model</label>
                        <input type="text" id="model" name="model" required class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary">
                    </div>
                    <div>
                        <label for="year" class="block text-sm font-medium text-dark">Year</label>
                        <input type="number" id="year" name="year" required min="1900" max="2024" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary">
                    </div>
                    <div>
                        <label for="licensePlate" class="block text-sm font-medium text-dark">License Plate</label>
                        <input type="text" id="licensePlate" name="licensePlate" required class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary">
                    </div>
                    <div>
                        <label for="mileage" class="block text-sm font-medium text-dark">Mileage</label>
                        <input type="number" id="mileage" name="mileage" required min="0" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary">
                    </div>
                </div>
                <button type="submit" class="mt-4 bg-primary hover:bg-secondary text-white py-2 px-4 rounded-md font-medium transition duration-300">Add Vehicle</button>
            </form>
        </div>

        <!-- Vehicles List -->
        <div>
            <h2 class="text-2xl font-semibold text-dark mb-4">Your Vehicles</h2>
            <c:choose>
                <c:when test="${empty vehicles}">
                    <p class="text-gray-500">No vehicles added yet.</p>
                </c:when>
                <c:otherwise>
                    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        <c:forEach var="vehicle" items="${vehicles}">
                            <div class="bg-white p-6 rounded-lg shadow-md">
                                <h3 class="text-lg font-medium text-dark">${vehicle.model}</h3>
                                <p class="text-gray-500">License Plate: ${vehicle.licensePlate}</p>
                                <p class="text-gray-500">Mileage: ${vehicle.mileage} miles</p>
                                <div class="mt-4 flex space-x-4">
                                    <form action="vehicle" method="POST">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="vehicleId" value="${vehicle.vehicleId}">
                                        <button type="submit" class="text-red-600 hover:text-red-800">Delete</button>
                                    </form>
                                    <a href="vehicle?action=edit&vehicleId=${vehicle.vehicleId}" class="text-primary hover:text-secondary">Edit</a>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Edit Vehicle Form (if edit action) -->
        <c:if test="${not empty param.action and param.action == 'edit'}">
            <div class="bg-white p-6 rounded-lg shadow-md mt-12">
                <h2 class="text-2xl font-semibold text-dark mb-4">Edit Vehicle</h2>
                <c:forEach var="vehicle" items="${vehicles}">
                    <c:if test="${vehicle.vehicleId == param.vehicleId}">
                        <form action="vehicle" method="POST">
                            <input type="hidden" name="action" value="update">
                            <input type="hidden" name="vehicleId" value="${vehicle.vehicleId}">
                            <div class="grid grid-cols-1 gap-4 sm:grid-cols-2">
                                <div>
                                    <label for="model" class="block text-sm font-medium text-dark">Model</label>
                                    <input type="text" id="model" name="model" value="${vehicle.model}" required class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary">
                                </div>
                                <div>
                                    <label for="year" class="block text-sm font-medium text-dark">Year</label>
                                    <input type="number" id="year" name="year" value="${vehicle.year}" required min="1900" max="2024" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary">
                                </div>
                                <div>
                                    <label for="licensePlate" class="block text-sm font-medium text-dark">License Plate</label>
                                    <input type="text" id="licensePlate" name="licensePlate" value="${vehicle.licensePlate}" required class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary">
                                </div>
                                <div>
                                    <label for="mileage" class="block text-sm font-medium text-dark">Mileage</label>
                                    <input type="number" id="mileage" name="mileage" value="${vehicle.mileage}" required min="0" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary">
                                </div>
                            </div>
                            <button type="submit" class="mt-4 bg-primary hover:bg-secondary text-white py-2 px-4 rounded-md font-medium transition duration-300">Update Vehicle</button>
                        </form>
                    </c:if>
                </c:forEach>
            </div>
        </c:if>
    </div>

    <script>
        document.querySelector('button[aria-controls="mobile-menu"]').addEventListener('click', function() {
            document.getElementById('mobile-menu').classList.toggle('hidden');
        });
    </script>
</body>
</html>