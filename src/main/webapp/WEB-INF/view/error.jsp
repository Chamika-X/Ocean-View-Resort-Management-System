<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - Ocean View Resort</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <style>body { font-family: 'Inter', sans-serif; }</style>
</head>
<body class="bg-slate-50 min-h-screen flex items-center justify-center font-sans">
    <div class="max-w-lg w-full mx-4">
        <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-10 text-center">
            <div class="w-20 h-20 bg-red-50 rounded-2xl flex items-center justify-center mx-auto mb-6">
                <i class="fas fa-exclamation-triangle text-red-400 text-3xl"></i>
            </div>
            
            <h1 class="text-2xl font-bold text-gray-900 mb-2">Oops! Something went wrong</h1>
            
            <c:choose>
                <c:when test="${not empty errorMessage}">
                    <p class="text-gray-500 text-sm mb-8">${errorMessage}</p>
                </c:when>
                <c:otherwise>
                    <p class="text-gray-500 text-sm mb-8">An unexpected error occurred. Please try again later.</p>
                </c:otherwise>
            </c:choose>

            <div class="flex items-center justify-center space-x-3">
                <a href="dashboard"
                   class="inline-flex items-center px-5 py-2.5 bg-gradient-to-r from-cyan-500 to-blue-600 text-white rounded-xl text-sm font-semibold hover:from-cyan-600 hover:to-blue-700 shadow-lg shadow-cyan-500/25 transition-all">
                    <i class="fas fa-home mr-2"></i>Dashboard
                </a>
                <a href="javascript:history.back()"
                   class="inline-flex items-center px-5 py-2.5 bg-white border border-gray-200 text-gray-700 rounded-xl text-sm font-medium hover:bg-gray-50 transition-colors">
                    <i class="fas fa-arrow-left mr-2"></i>Go Back
                </a>
            </div>
        </div>
        
        <p class="text-center text-xs text-gray-400 mt-6">
            &copy; 2026 Ocean View Resort, Galle, Sri Lanka
        </p>
    </div>
</body>
</html>