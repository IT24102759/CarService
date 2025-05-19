<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AutoTrack | Register</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background: linear-gradient(135deg, #e6e9f0, #eef1f5);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .register-container {
            background: white;
            border-radius: 1rem;
            box-shadow: 0 10px 15px rgba(0, 0, 0, 0.1);
            padding: 2rem;
            width: 100%;
            max-width: 400px;
            transition: transform 0.3s ease;
        }
        .register-container:hover {
            transform: translateY(-5px);
        }
        .form-input {
            transition: border-color 0.2s, box-shadow 0.2s;
        }
        .form-input:focus {
            border-color: #2563eb;
            box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.1);
        }
        .btn-primary {
            background: linear-gradient(to right, #2563eb, #1e40af);
            transition: background 0.3s ease;
        }
        .btn-primary:hover {
            background: linear-gradient(to right, #1e40af, #2563eb);
        }
        .error-message, .success-message {
            animation: fadeIn 0.5s ease;
        }
        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }
        .error-border {
            border-color: #ef4444 !important;
        }
    </style>
</head>
<body>
<div class="register-container">
    <div class="flex items-center justify-center mb-6">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" class="w-8 h-8 text-blue-600">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 8h8m0 0h2a2 2 0 012 2v6a2 2 0 01-2 2h-2m-8 0H6a2 2 0 01-2-2v-6a2 2 0 012-2h2m0 0V6a2 2 0 012-2h4a2 2 0 012 2v2" />
        </svg>
        <h1 class="text-2xl font-semibold text-gray-800 ml-2">AutoTrack</h1>
    </div>
    <h2 class="text-xl font-semibold text-gray-800 text-center mb-4">Create Your Account</h2>

    <!-- Error/Success Messages -->
    <c:if test="${not empty error}">
        <div class="error-message bg-red-100 text-red-700 p-3 rounded mb-4 text-sm">${error}</div>
    </c:if>
    <c:if test="${not empty success}">
        <div class="success-message bg-green-100 text-green-700 p-3 rounded mb-4 text-sm">${success}</div>
    </c:if>

    <!-- Registration Form -->
    <form id="registerForm" action="${pageContext.request.contextPath}/register" method="post" novalidate>
        <div class="mb-4">
            <label for="username" class="block text-sm font-medium text-gray-700">Username</label>
            <input type="text" id="username" name="username" class="form-input mt-1 block w-full border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none" required>
            <p id="usernameError" class="text-red-500 text-xs mt-1 hidden">Username is required.</p>
        </div>
        <div class="mb-4">
            <label for="password" class="block text-sm font-medium text-gray-700">Password</label>
            <input type="password" id="password" name="password" class="form-input mt-1 block w-full border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none" required>
            <p id="passwordError" class="text-red-500 text-xs mt-1 hidden">Password is required.</p>
        </div>
        <input type="hidden" name="role" value="user">
        <button type="submit" class="btn-primary w-full text-white font-medium rounded-lg px-4 py-2 mt-4">Register</button>
    </form>

    <p class="text-sm text-gray-600 text-center mt-4">
        Already have an account? <a href="login.jsp" class="text-blue-600 hover:underline">Log in</a>
    </p>
</div>

<script>
    const form = document.getElementById('registerForm');
    const usernameInput = document.getElementById('username');
    const passwordInput = document.getElementById('password');
    const usernameError = document.getElementById('usernameError');
    const passwordError = document.getElementById('passwordError');

    function validateInput(input, errorElement, message) {
        if (input.value.trim() === '') {
            errorElement.classList.remove('hidden');
            errorElement.textContent = message;
            input.classList.add('error-border');
            return false;
        } else {
            errorElement.classList.add('hidden');
            input.classList.remove('error-border');
            return true;
        }
    }

    usernameInput.addEventListener('input', () => {
        validateInput(usernameInput, usernameError, 'Username is required.');
    });

    passwordInput.addEventListener('input', () => {
        validateInput(passwordInput, passwordError, 'Password is required.');
    });

    form.addEventListener('submit', (e) => {
        const isUsernameValid = validateInput(usernameInput, usernameError, 'Username is required.');
        const isPasswordValid = validateInput(passwordInput, passwordError, 'Password is required.');

        if (!isUsernameValid || !isPasswordValid) {
            e.preventDefault();
        }
    });
</script>
</body>
</html>