<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AutoTrack | Login</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Poppins', sans-serif;
        }
        body {
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            background: linear-gradient(#e6e9f0, #eef1f5);
        }
        .login-container {
            background: #fff;
            padding: 2rem;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 400px;
            text-align: center;
        }
        .logo {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-bottom: 1.5rem;
        }
        .logo svg {
            width: 32px;
            height: 32px;
            color: #2563eb;
        }
        .logo span {
            font-size: 1.5rem;
            font-weight: 600;
            color: #1f2937;
            margin-left: 0.5rem;
        }
        h1 {
            font-size: 1.75rem;
            color: #1f2937;
            margin-bottom: 1.5rem;
            font-weight: 600;
        }
        .error-message {
            background: #fee2e2;
            border-left: 4px solid #dc2626;
            color: #b91c1c;
            padding: 1rem;
            margin-bottom: 1.5rem;
            border-radius: 4px;
        }
        .form-group {
            margin-bottom: 1.25rem;
            text-align: left;
        }
        label {
            display: block;
            font-size: 0.9rem;
            color: #374151;
            margin-bottom: 0.25rem;
            font-weight: 400;
        }
        input[type="text"],
        input[type="password"] {
            width: 100%;
            padding: 0.75rem;
            border: 1px solid #d1d5db;
            border-radius: 4px;
            font-size: 1rem;
            color: #1f2937;
            outline: none;
            transition: border-color 0.2s;
        }
        input[type="text"]:focus,
        input[type="password"]:focus {
            border-color: #2563eb;
            box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.1);
        }
        button {
            width: 100%;
            padding: 0.75rem;
            border: none;
            border-radius: 4px;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            transition: background 0.2s;
            background: #2563eb;
            color: #fff;
        }
        button:hover {
            background: #1d4ed8;
        }
        .role-info {
            font-size: 0.9rem;
            color: #374151;
            margin-bottom: 1rem;
        }
        .role-selection {
            display: flex;
            gap: 1rem;
            margin-bottom: 1.5rem;
        }
        .role-button {
            flex: 1;
            padding: 0.75rem;
            border: 2px solid #2563eb;
            border-radius: 4px;
            background: transparent;
            color: #2563eb;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.2s;
            text-decoration: none;
            display: inline-block;
        }
        .role-button:hover {
            background: #2563eb;
            color: #fff;
        }
        .role-button.active {
            background: #2563eb;
            color: #fff;
        }
        @media (max-width: 480px) {
            .login-container {
                margin: 1rem;
                padding: 1.5rem;
            }
            h1 {
                font-size: 1.5rem;
            }
        }
    </style>
</head>
<body>
<div class="login-container">
    <!-- Logo -->
    <div class="logo">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 8h8m0 0h2a2 2 0 012 2v6a2 2 0 01-2 2h-2m-8 0H6a2 2 0 01-2-2v-6a2 2 0 012-2h2m0 0V6a2 2 0 012-2h4a2 2 0 012 2v2" />
        </svg>
        <span>AutoTrack</span>
    </div>

    <!-- Title -->
    <h1>Welcome to AutoTrack</h1>

    <!-- Error Message -->
    <c:if test="${not empty error}">
        <div class="error-message">
            <p><c:out value="${error}"/></p>
        </div>
    </c:if>

    <c:if test="${empty requestScope.role}">
        <!-- Role Selection -->
        <div class="role-selection">
            <a href="${pageContext.request.contextPath}/login?role=admin&logout=true" class="role-button">Admin Login</a>
            <a href="${pageContext.request.contextPath}/login?role=user&logout=true" class="role-button">User Login</a>
        </div>
    </c:if>

    <c:if test="${not empty requestScope.role}">
        <!-- Role Info -->
        <p class="role-info">Logging in as <c:out value="${requestScope.role == 'admin' ? 'Administrator' : 'User'}"/></p>

        <!-- Login Form -->
        <form action="${pageContext.request.contextPath}/login" method="POST">
            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" id="username" name="username" required aria-label="Username">
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required aria-label="Password">
            </div>
            <input type="hidden" name="role" value="${requestScope.role}">
            <button type="submit">Login</button>
        </form>
    </c:if>
</div>
</body>
</html>