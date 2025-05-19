<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${empty sessionScope.user || sessionScope.user.admin}">
    <c:redirect url="/pages/login.jsp"/>
</c:if>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AutoTrack | User Dashboard</title>
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
            background: linear-gradient(#e6f0ff, #f0f5ff);
            padding: 1rem;
        }

        .container {
            max-width: 800px;
            margin: 0 auto;
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1.5rem;
            background: #fff;
            padding: 1rem;
            border-radius: 4px;
            box-shadow: 0 2px 4px rgba(37, 99, 235, 0.1);
        }

        .header .logo {
            display: flex;
            align-items: center;
        }

        .header .logo svg {
            width: 24px;
            height: 24px;
            color: #2563eb;
        }

        .header .logo span {
            font-size: 1.2rem;
            font-weight: 600;
            color: #1e3a8a;
            margin-left: 0.5rem;
        }

        .header h1 {
            font-size: 1.5rem;
            color: #1e3a8a;
        }

        .logout {
            background: #2563eb;
            color: #fff;
            padding: 0.5rem 1rem;
            border-radius: 4px;
            text-decoration: none;
            font-size: 0.9rem;
            transition: background-color 0.2s;
        }

        .logout:hover {
            background: #1d4ed8;
        }

        .cards {
            display: flex;
            flex-direction: column;
            gap: 1rem;
        }

        .card {
            background: #fff;
            padding: 1rem;
            border-radius: 4px;
            border: 1px solid #bfdbfe;
            box-shadow: 0 2px 4px rgba(37, 99, 235, 0.05);
        }

        .card h3 {
            font-size: 1.1rem;
            color: #1e3a8a;
            margin-bottom: 0.5rem;
        }

        .card p {
            color: #3b82f6;
            font-size: 0.9rem;
            margin-bottom: 0.5rem;
        }

        .card a {
            color: #2563eb;
            text-decoration: none;
            font-size: 0.9rem;
            transition: color 0.2s;
        }

        .card a:hover {
            color: #1d4ed8;
        }

        @media (max-width: 480px) {
            .header {
                flex-direction: column;
                gap: 1rem;
                text-align: center;
            }

            .header h1 {
                font-size: 1.3rem;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <div class="logo">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 8h8m0 0h2a2 2 0 012 2v6a2 2 0 01-2 2h-2m-8 0H6a2 2 0 01-2-2v-6a2 2 0 012-2h2m0 0V6a2 2 0 012-2h4a2 2 0 012 2v2" />
            </svg>
            <span>AutoTrack</span>
        </div>
        <div>
            <h1>Welcome, ${sessionScope.user.username}</h1>
            <a href="logout" class="logout">Logout</a>
        </div>
    </div>
    <div class="cards">
        <div class="card">
            <h3>My Vehicles</h3>
            <c:forEach var="vehicle" items="${vehicles}">
                <p>${vehicle}</p>
            </c:forEach>
            <a href="${pageContext.request.contextPath}/vehicle">View All Vehicles</a>
        </div>
        <div class="card">
            <h3>Recent Services</h3>
            <c:forEach var="service" items="${services}">
                <p>${service}</p>
            </c:forEach>
            <a href="${pageContext.request.contextPath}/services">View Service History</a>
        </div>
        <div class="card">
            <h3>Reports</h3>
            <p>View maintenance trends and insights.</p>
            <div class="flex flex-col gap-2 mt-2">
                <a href="${pageContext.request.contextPath}/reports?generate=true" class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded">Generate Report</a>
                <a href="${pageContext.request.contextPath}/reports?view=true" class="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded">View Reports</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>