<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ocean View Resort - Hotel Reservation System</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
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
<body class="bg-gradient-to-br from-ocean-500 via-ocean-600 to-ocean-700 min-h-screen flex items-center justify-center">
    <div class="text-center text-white p-8 max-w-lg">
        <div class="w-24 h-24 bg-white/20 rounded-full flex items-center justify-center mx-auto mb-6 shadow-lg shadow-black/10">
            <i class="fas fa-umbrella-beach text-5xl"></i>
        </div>
        <h1 class="text-5xl font-bold mb-3">Ocean View Resort</h1>
        <div class="w-16 h-0.5 bg-white/40 mx-auto my-4 rounded-full"></div>
        <p class="text-xl mb-4 opacity-80">Hotel Reservation System</p>
        <p class="mb-8 text-sm opacity-70">
            <i class="fas fa-map-marker-alt mr-1"></i> Galle, Sri Lanka
        </p>
        <a href="auth?action=login"
           class="inline-block px-10 py-3.5 bg-white text-ocean-600 font-semibold rounded-full hover:bg-gray-100 transition-all shadow-xl hover:shadow-2xl">
            <i class="fas fa-sign-in-alt mr-2"></i>Login to System
        </a>
        <p class="mt-6 text-xs opacity-50">
            <i class="fas fa-circle-notch fa-spin mr-1"></i> Redirecting to login...
        </p>
        <p class="mt-6 text-sm opacity-60">&copy; 2026 Ocean View Resort. All rights reserved.</p>
    </div>
    <script>
        setTimeout(function() {
            window.location.href = 'auth?action=login';
        }, 4000);
    </script>
</body>
</html>