<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AutoTrack | Error</title>
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
<body class="font-sans min-h-screen flex items-center justify-center bg-light">
    <div class="w-full max-w-md p-8 bg-white rounded-lg shadow-xl">
        <div class="flex justify-center mb-6">
            <div class="flex items-center">
                <svg class="h-8 w-8 text-primary" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 8h8m0 0h2a2 2 0 012 2v6a2 2 0 01-2 2h-2m-8 0H6a2 2 0 01-2-2v-6a2 2 0 012-2h2m0 0V6a2 2 0 012-2h4a2 2 0 012 2v2" />
                </svg>
                <span class="ml-2 text-xl font-bold text-dark">AutoTrack</span>
            </div>
        </div>
        <h1 class="text-2xl font-bold text-center text-dark mb-6">Error</h1>
        <div class="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 mb-6" role="alert">
            <p>${error}</p>
        </div>
        <a href="index.jsp" class="block text-center text-primary hover:text-secondary font-medium">Return to Home</a>
    </div>
</body>
</html>