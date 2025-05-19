<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AutoTrack - Vehicle Maintenance Management</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <script>
        tailwind.config = {
            theme: {
                extend: {
                    colors: {
                        primary: '#2563eb',
                        secondary: '#1e40af',
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
    <style>
        .search-bar {
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            border: 1px solid rgba(255, 255, 255, 0.2);
        }
        .gradient-bg {
            background: linear-gradient(135deg, #2563eb 0%, #1e40af 100%);
        }
        .hover-scale {
            transition: transform 0.3s ease;
        }
        .hover-scale:hover {
            transform: scale(1.05);
        }
    </style>
</head>
<body class="font-sans bg-light">
    <!-- Navigation -->
    <nav class="bg-white shadow-md fixed w-full z-50">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="flex justify-between h-16">
                <div class="flex items-center">
                    <div class="flex-shrink-0 flex items-center">
                        <svg class="h-8 w-8 text-primary" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 8h8m0 0h2a2 2 0 012 2v6a2 2 0 01-2 2h-2m-8 0H6a2 2 0 01-2-2v-6a2 2 0 012-2h2m0 0V6a2 2 0 012-2h4a2 2 0 012 2v2" />
                        </svg>
                        <span class="ml-2 text-xl font-bold text-dark">AutoTrack</span>
                    </div>
                </div>
                <div class="flex items-center space-x-4">
                    <a href="${pageContext.request.contextPath}/pages/login.jsp" class="text-dark hover:text-primary px-3 py-2 rounded-md text-sm font-medium transition duration-300">Login</a>
                    <a href="${pageContext.request.contextPath}/pages/register.jsp" class="bg-primary hover:bg-secondary text-white px-4 py-2 rounded-md text-sm font-medium transition duration-300">Get Started</a>
                </div>
            </div>
        </div>
    </nav>

    <!-- Hero Section with Search -->
    <div class="relative bg-gradient-to-r from-primary to-secondary pt-24">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-24">
            <div class="text-center">
                <h1 class="text-4xl tracking-tight font-extrabold text-white sm:text-5xl md:text-6xl">
                    <span class="block">Find Your Perfect</span>
                    <span class="block">Vehicle Service</span>
                </h1>
                <p class="mt-3 max-w-md mx-auto text-base text-white sm:text-lg md:mt-5 md:text-xl md:max-w-3xl">
                    Search for services, compare prices, and book maintenance for your vehicle.
                </p>
                
                <!-- Search Bar -->
                <!-- Removed search bar and popular links as requested -->
            </div>
        </div>
        <div class="absolute bottom-0 left-0 right-0 h-16 bg-gradient-to-t from-light to-transparent"></div>
    </div>

    <!-- Features Section -->
    <div class="py-16 bg-light">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="lg:text-center">
                <h2 class="text-base text-primary font-semibold tracking-wide uppercase">Features</h2>
                <p class="mt-2 text-3xl leading-8 font-extrabold tracking-tight text-dark sm:text-4xl">
                    Everything you need to manage your vehicle
                </p>
            </div>

            <div class="mt-12">
                <div class="grid grid-cols-1 gap-8 md:grid-cols-2 lg:grid-cols-4">
                    <!-- Feature 1 -->
                    <div class="relative bg-white p-6 rounded-xl shadow-lg hover-scale">
                        <div class="absolute -top-4 left-6">
                            <div class="flex items-center justify-center h-12 w-12 rounded-full bg-primary text-white">
                                <i class="fas fa-bell text-xl"></i>
                            </div>
                        </div>
                        <div class="mt-8">
                            <h3 class="text-lg font-medium text-dark">Maintenance Reminders</h3>
                            <p class="mt-2 text-base text-gray-500">
                                Get timely notifications for upcoming maintenance tasks.
                            </p>
                        </div>
                    </div>

                    <!-- Feature 2 -->
                    <div class="relative bg-white p-6 rounded-xl shadow-lg hover-scale">
                        <div class="absolute -top-4 left-6">
                            <div class="flex items-center justify-center h-12 w-12 rounded-full bg-primary text-white">
                                <i class="fas fa-history text-xl"></i>
                            </div>
                        </div>
                        <div class="mt-8">
                            <h3 class="text-lg font-medium text-dark">Service History</h3>
                            <p class="mt-2 text-base text-gray-500">
                                Track all your vehicle's service records.
                            </p>
                        </div>
                    </div>

                    <!-- Feature 3 -->
                    <div class="relative bg-white p-6 rounded-xl shadow-lg hover-scale">
                        <div class="absolute -top-4 left-6">
                            <div class="flex items-center justify-center h-12 w-12 rounded-full bg-primary text-white">
                                <i class="fas fa-dollar-sign text-xl"></i>
                            </div>
                        </div>
                        <div class="mt-8">
                            <h3 class="text-lg font-medium text-dark">Cost Tracking</h3>
                            <p class="mt-2 text-base text-gray-500">
                                Monitor maintenance expenses and running costs.
                            </p>
                        </div>
                    </div>

                    <!-- Feature 4 -->
                    <div class="relative bg-white p-6 rounded-xl shadow-lg hover-scale">
                        <div class="absolute -top-4 left-6">
                            <div class="flex items-center justify-center h-12 w-12 rounded-full bg-primary text-white">
                                <i class="fas fa-heartbeat text-xl"></i>
                            </div>
                        </div>
                        <div class="mt-8">
                            <h3 class="text-lg font-medium text-dark">Vehicle Health</h3>
                            <p class="mt-2 text-base text-gray-500">
                                Get preventive maintenance recommendations.
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- CTA Section -->
    <div class="gradient-bg">
        <div class="max-w-7xl mx-auto py-16 px-4 sm:px-6 lg:py-24 lg:px-8 lg:flex lg:items-center lg:justify-between">
            <h2 class="text-3xl font-extrabold tracking-tight text-white sm:text-4xl">
                <span class="block">Ready to get started?</span>
                <span class="block text-white">Create your account today.</span>
            </h2>
            <div class="mt-8 flex lg:mt-0 lg:flex-shrink-0">
                <div class="inline-flex rounded-md shadow">
                    <a href="${pageContext.request.contextPath}/pages/register.jsp" class="inline-flex items-center justify-center px-5 py-3 border border-transparent text-base font-medium rounded-md text-primary bg-white hover:bg-blue-50 transition duration-300">
                        Get started
                    </a>
                </div>
                <div class="ml-3 inline-flex rounded-md shadow">
                    <a href="${pageContext.request.contextPath}/pages/login.jsp" class="inline-flex items-center justify-center px-5 py-3 border border-transparent text-base font-medium rounded-md text-white bg-secondary hover:bg-blue-900 transition duration-300">
                        Login
                    </a>
                </div>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <footer class="bg-white">
        <div class="max-w-7xl mx-auto py-12 px-4 sm:px-6 md:flex md:items-center md:justify-between lg:px-8">
            <div class="flex justify-center space-x-6 md:order-2">
                <a href="#" class="text-gray-400 hover:text-primary transition duration-300">
                    <i class="fab fa-facebook text-2xl"></i>
                </a>
                <a href="#" class="text-gray-400 hover:text-primary transition duration-300">
                    <i class="fab fa-twitter text-2xl"></i>
                </a>
                <a href="#" class="text-gray-400 hover:text-primary transition duration-300">
                    <i class="fab fa-instagram text-2xl"></i>
                </a>
            </div>
            <div class="mt-8 md:mt-0 md:order-1">
                <p class="text-center text-base text-gray-400">
                    &copy; 2024 AutoTrack. All rights reserved.
                </p>
            </div>
        </div>
    </footer>

    <script>
        // Add smooth scrolling
        document.querySelectorAll('a[href^="#"]').forEach(anchor => {
            anchor.addEventListener('click', function (e) {
                e.preventDefault();
                document.querySelector(this.getAttribute('href')).scrollIntoView({
                    behavior: 'smooth'
                });
            });
        });
    </script>
</body>
</html> 