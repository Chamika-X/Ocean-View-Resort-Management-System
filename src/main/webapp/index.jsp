<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ocean View Resort - Hotel Reservation System</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <script>
        tailwind.config = {
            theme: {
                extend: {
                    colors: {
                        ocean: {
                            500: '#0ea5e9',
                            600: '#0284c7',
                            700: '#0369a1',
                        }
                    }
                }
            }
        }
    </script>
</head>
<body class="bg-gradient-to-br from-ocean-500 to-ocean-700 min-h-screen flex items-center justify-center">
    <div class="text-center text-white p-8">
        <div class="w-24 h-24 bg-white/20 rounded-full flex items-center justify-center mx-auto mb-6">
            <i class="fas fa-umbrella-beach text-5xl"></i>
        </div>
        <h1 class="text-4xl font-bold mb-4">Ocean View Resort</h1>
        <p class="text-xl mb-8 opacity-80">Hotel Reservation System</p>
        <p class="mb-8">Galle, Sri Lanka</p>
        <a href="auth?action=login" 
           class="inline-block px-8 py-3 bg-white text-ocean-600 font-semibold rounded-lg hover:bg-gray-100 transition-colors shadow-lg">
            Login to System
        </a>
        <p class="mt-8 text-sm opacity-60">&copy; 2026 Ocean View Resort. All rights reserved.</p>
    </div>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <script>
        // Redirect to login if already page loads
        setTimeout(function() {
            window.location.href = 'auth?action=login';
        }, 2000);
    </script>
</body>
</html>