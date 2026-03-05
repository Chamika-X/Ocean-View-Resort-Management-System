<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Login - Ocean View Resort</title>
<script src="https://cdn.tailwindcss.com"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
<script>
    tailwind.config = {
        theme: {
            extend: {
                fontFamily: { sans: ['Inter', 'sans-serif'] },
                colors: {
                    ocean: {
                        50: '#f0f9ff', 100: '#e0f2fe', 200: '#bae6fd', 300: '#7dd3fc',
                        400: '#38bdf8', 500: '#0ea5e9', 600: '#0284c7', 700: '#0369a1',
                    }
                }
            }
        }
    }
</script>
<style>body { font-family: 'Inter', sans-serif; }</style>
</head>
<body class="min-h-screen flex font-sans">
    <!-- Left Side - Gradient Panel -->
    <div class="hidden lg:flex lg:w-1/2 relative overflow-hidden bg-gradient-to-br from-cyan-400 via-cyan-500 to-blue-600">
        <div class="absolute inset-0 opacity-10">
            <div class="absolute top-20 left-20 w-72 h-72 bg-white rounded-full blur-3xl"></div>
            <div class="absolute bottom-20 right-20 w-96 h-96 bg-white rounded-full blur-3xl"></div>
        </div>
        <div class="absolute inset-0 flex flex-col items-center justify-center text-white p-12">
            <div class="text-center max-w-lg">
                <div class="w-20 h-20 bg-white/20 backdrop-blur rounded-2xl flex items-center justify-center mx-auto mb-8">
                    <i class="fas fa-umbrella-beach text-4xl"></i>
                </div>
                <h2 class="text-4xl font-bold mb-4">Ocean View Resort</h2>
                <p class="text-lg leading-relaxed mb-8 text-cyan-100">Your gateway to paradise on the beautiful shores of Galle, Sri Lanka</p>
                <div class="flex items-center justify-center space-x-8 text-sm">
                    <div class="flex items-center bg-white/10 backdrop-blur px-4 py-2 rounded-xl">
                        <i class="fas fa-bed text-lg mr-2"></i>
                        <span>Luxury Rooms</span>
                    </div>
                    <div class="flex items-center bg-white/10 backdrop-blur px-4 py-2 rounded-xl">
                        <i class="fas fa-water text-lg mr-2"></i>
                        <span>Ocean Views</span>
                    </div>
                    <div class="flex items-center bg-white/10 backdrop-blur px-4 py-2 rounded-xl">
                        <i class="fas fa-spa text-lg mr-2"></i>
                        <span>Spa & Wellness</span>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Right Side - Login Form -->
    <div class="w-full lg:w-1/2 flex items-center justify-center p-8 bg-slate-50">
        <div class="max-w-md w-full">
            <!-- Logo and Header -->
            <div class="text-center mb-8">
                <div class="w-16 h-16 bg-gradient-to-r from-cyan-500 to-blue-600 rounded-2xl flex items-center justify-center mx-auto mb-5 shadow-lg shadow-cyan-500/25">
                    <i class="fas fa-umbrella-beach text-white text-2xl"></i>
                </div>
                <h1 class="text-2xl font-bold text-gray-900 mb-1">Welcome Back</h1>
                <p class="text-sm text-gray-500">Sign in to the Resort Management System</p>
            </div>

            <!-- Alerts -->
            <c:if test="${not empty error}">
                <div class="mb-6 bg-red-50 border border-red-200 rounded-xl p-4">
                    <div class="flex items-center">
                        <i class="fas fa-exclamation-circle text-red-500 mr-2"></i>
                        <span class="text-red-800 text-sm">${error}</span>
                    </div>
                </div>
            </c:if>

            <c:if test="${not empty fieldErrors}">
                <div class="mb-6 bg-red-50 border border-red-200 rounded-xl p-4">
                    <c:forEach var="err" items="${fieldErrors}">
                        <div class="flex items-center text-red-800 text-sm">
                            <i class="fas fa-exclamation-circle text-red-500 mr-2"></i>
                            <span>${err.value}</span>
                        </div>
                    </c:forEach>
                </div>
            </c:if>

            <!-- Login Form -->
            <form action="auth" method="post" class="space-y-5">
                <input type="hidden" name="action" value="login">

                <div>
                    <label for="username" class="block text-sm font-medium text-gray-700 mb-1.5">Username</label>
                    <div class="relative">
                        <span class="absolute left-3.5 top-3.5 text-gray-400"><i class="fas fa-user text-sm"></i></span>
                        <input type="text" id="username" name="username" value="${username}"
                            class="w-full pl-10 pr-4 py-3 bg-white border border-gray-200 rounded-xl focus:ring-2 focus:ring-cyan-400 focus:border-cyan-400 transition-all text-sm"
                            placeholder="Enter your username" required>
                    </div>
                </div>

                <div>
                    <label for="password" class="block text-sm font-medium text-gray-700 mb-1.5">Password</label>
                    <div class="relative">
                        <span class="absolute left-3.5 top-3.5 text-gray-400"><i class="fas fa-lock text-sm"></i></span>
                        <input type="password" id="password" name="password"
                            class="w-full pl-10 pr-12 py-3 bg-white border border-gray-200 rounded-xl focus:ring-2 focus:ring-cyan-400 focus:border-cyan-400 transition-all text-sm"
                            placeholder="Enter your password" required>
                        <button type="button" onclick="togglePassword()"
                            class="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-400 hover:text-gray-600">
                            <i id="password-icon" class="fas fa-eye text-sm"></i>
                        </button>
                    </div>
                </div>

                <button type="submit"
                    class="w-full bg-gradient-to-r from-cyan-500 to-blue-600 text-white py-3 px-4 rounded-full font-semibold hover:from-cyan-600 hover:to-blue-700 focus:outline-none focus:ring-2 focus:ring-cyan-400 focus:ring-offset-2 transition-all shadow-lg shadow-cyan-500/25 text-sm">
                    <i class="fas fa-sign-in-alt mr-2"></i>Sign In
                </button>
            </form>

            <!-- Default Credentials -->
            <div class="mt-6 p-4 bg-cyan-50 border border-cyan-200 rounded-xl">
                <p class="text-xs text-cyan-800">
                    <i class="fas fa-info-circle mr-1.5"></i><strong>Default Credentials:</strong><br>
                    Admin: <code class="bg-cyan-100 px-1.5 py-0.5 rounded text-xs">admin</code> / <code class="bg-cyan-100 px-1.5 py-0.5 rounded text-xs">admin123</code><br>
                    Staff: <code class="bg-cyan-100 px-1.5 py-0.5 rounded text-xs">receptionist</code> / <code class="bg-cyan-100 px-1.5 py-0.5 rounded text-xs">staff123</code>
                </p>
            </div>

            <div class="mt-6 text-center text-xs text-gray-400">
                <p>&copy; 2026 Ocean View Resort, Galle, Sri Lanka</p>
            </div>
        </div>
    </div>

    <script>
        function togglePassword() {
            const passwordInput = document.getElementById('password');
            const passwordIcon = document.getElementById('password-icon');
            if (passwordInput.type === 'password') {
                passwordInput.type = 'text';
                passwordIcon.classList.remove('fa-eye');
                passwordIcon.classList.add('fa-eye-slash');
            } else {
                passwordInput.type = 'password';
                passwordIcon.classList.remove('fa-eye-slash');
                passwordIcon.classList.add('fa-eye');
            }
        }
    </script>
</body>
</html>