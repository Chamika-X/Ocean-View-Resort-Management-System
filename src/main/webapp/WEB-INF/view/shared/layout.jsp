<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Ocean View Resort - Galle</title>
<script src="https://cdn.tailwindcss.com"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
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
                        800: '#075985', 900: '#0c4a6e',
                    }
                }
            }
        }
    }
</script>
<style>
    body { font-family: 'Inter', sans-serif; }
    .sidebar-link { transition: all 0.2s ease; }
    .sidebar-link:hover { background: rgba(255,255,255,0.15); transform: translateX(4px); }
    .sidebar-link.active { background: rgba(255,255,255,0.2); border-left: 3px solid white; }
    ::-webkit-scrollbar { width: 6px; }
    ::-webkit-scrollbar-track { background: #f1f5f9; }
    ::-webkit-scrollbar-thumb { background: #cbd5e1; border-radius: 3px; }
    ::-webkit-scrollbar-thumb:hover { background: #94a3b8; }
</style>
</head>
<body class="bg-slate-50 min-h-screen font-sans">
<div class="flex min-h-screen">

    <!-- Sidebar -->
    <aside class="w-64 fixed left-0 top-0 h-full bg-gradient-to-b from-cyan-400 via-cyan-500 to-blue-600 text-white flex flex-col z-50 shadow-xl">
        <!-- Logo -->
        <div class="px-6 py-6 border-b border-white/20">
            <a href="dashboard" class="flex items-center space-x-3">
                <div class="w-10 h-10 bg-white/20 backdrop-blur rounded-xl flex items-center justify-center">
                    <i class="fas fa-umbrella-beach text-white text-lg"></i>
                </div>
                <div>
                    <h1 class="font-bold text-lg leading-tight">OCEAN VIEW</h1>
                    <p class="text-xs text-cyan-100 tracking-wider">RESORT & SPA</p>
                </div>
            </a>
        </div>

        <!-- Navigation -->
        <nav class="flex-1 px-4 py-6 space-y-1 overflow-y-auto">
            <a href="dashboard" class="sidebar-link ${fn:contains(pageContext.request.requestURI, 'dashboard') ? 'active' : ''} flex items-center space-x-3 px-4 py-3 rounded-lg text-sm font-medium ${fn:contains(pageContext.request.requestURI, 'dashboard') ? 'text-white' : 'text-white/80 hover:text-white'}">
                <i class="fas fa-th-large w-5 text-center"></i>
                <span>Dashboard</span>
            </a>
            <a href="room?action=list" class="sidebar-link ${fn:contains(pageContext.request.requestURI, 'room') ? 'active' : ''} flex items-center space-x-3 px-4 py-3 rounded-lg text-sm font-medium ${fn:contains(pageContext.request.requestURI, 'room') ? 'text-white' : 'text-white/80 hover:text-white'}">
                <i class="fas fa-bed w-5 text-center"></i>
                <span>Rooms & Suites</span>
            </a>
            <a href="reservation?action=list" class="sidebar-link ${fn:contains(pageContext.request.requestURI, 'reservation') ? 'active' : ''} flex items-center space-x-3 px-4 py-3 rounded-lg text-sm font-medium ${fn:contains(pageContext.request.requestURI, 'reservation') ? 'text-white' : 'text-white/80 hover:text-white'}">
                <i class="fas fa-calendar-check w-5 text-center"></i>
                <span>Reservations</span>
            </a>
            <a href="guest?action=list" class="sidebar-link ${fn:contains(pageContext.request.requestURI, 'guest') ? 'active' : ''} flex items-center space-x-3 px-4 py-3 rounded-lg text-sm font-medium ${fn:contains(pageContext.request.requestURI, 'guest') ? 'text-white' : 'text-white/80 hover:text-white'}">
                <i class="fas fa-user-friends w-5 text-center"></i>
                <span>Guest Profiles</span>
            </a>
            <c:if test="${sessionScope.role == 'admin'}">
                <a href="activity?action=list" class="sidebar-link ${fn:contains(pageContext.request.requestURI, 'activity') ? 'active' : ''} flex items-center space-x-3 px-4 py-3 rounded-lg text-sm font-medium ${fn:contains(pageContext.request.requestURI, 'activity') ? 'text-white' : 'text-white/80 hover:text-white'}">
                    <i class="fas fa-hiking w-5 text-center"></i>
                    <span>Activities</span>
                </a>
                <a href="user-management?action=list" class="sidebar-link ${fn:contains(pageContext.request.requestURI, 'user-management') ? 'active' : ''} flex items-center space-x-3 px-4 py-3 rounded-lg text-sm font-medium ${fn:contains(pageContext.request.requestURI, 'user-management') ? 'text-white' : 'text-white/80 hover:text-white'}">
                    <i class="fas fa-user-cog w-5 text-center"></i>
                    <span>User Management</span>
                </a>
            </c:if>
            <a href="dashboard?action=help" class="sidebar-link ${fn:contains(pageContext.request.requestURI, 'help') ? 'active' : ''} flex items-center space-x-3 px-4 py-3 rounded-lg text-sm font-medium ${fn:contains(pageContext.request.requestURI, 'help') ? 'text-white' : 'text-white/80 hover:text-white'}">
                <i class="fas fa-question-circle w-5 text-center"></i>
                <span>Help & Support</span>
            </a>
        </nav>

        <!-- User Section -->
        <div class="px-4 pb-6 border-t border-white/20 pt-4">
            <div class="flex items-center space-x-3">
                <div class="w-10 h-10 bg-white/20 rounded-full flex items-center justify-center text-sm font-bold">
                    ${fn:substring(sessionScope.fullName != null ? sessionScope.fullName : sessionScope.username, 0, 2)}
                </div>
                <div class="flex-1 min-w-0">
                    <p class="text-sm font-semibold truncate">${sessionScope.fullName != null ? sessionScope.fullName : sessionScope.username}</p>
                    <p class="text-xs text-cyan-100 capitalize">${sessionScope.role}</p>
                </div>
                <a href="auth?action=logout" class="text-white/60 hover:text-white transition-colors" title="Logout">
                    <i class="fas fa-sign-out-alt"></i>
                </a>
            </div>
        </div>
    </aside>

    <!-- Main Content Area -->
    <div class="flex-1 ml-64 flex flex-col min-h-screen">
        <!-- Add your main content here -->
