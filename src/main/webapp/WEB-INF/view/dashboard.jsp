<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ include file="shared/dashboard-layout.jsp"%>

        <!-- Top Header Bar -->
        <div class="bg-white border-b border-gray-200 px-8 py-5">
            <div class="flex items-center justify-between">
                <div>
                    <h1 class="text-2xl font-bold text-gray-900 flex items-center">
                        <span id="greeting-text">Good Morning</span>, ${sessionScope.fullName != null ? sessionScope.fullName : sessionScope.username}
                        <span class="ml-2 text-2xl">🏖️</span>
                    </h1>
                    <p class="text-sm text-gray-500 mt-1">Here's an overview of Ocean View Resort operations</p>
                </div>
                <div class="flex items-center space-x-4">
                    <div class="relative">
                        <input type="text" placeholder="Search guests, rooms..."
                            class="w-64 pl-10 pr-4 py-2.5 bg-gray-50 border border-gray-200 rounded-xl text-sm focus:ring-2 focus:ring-cyan-400 focus:border-cyan-400 focus:bg-white transition-all">
                        <i class="fas fa-search absolute left-3.5 top-3 text-gray-400 text-sm"></i>
                    </div>
                    <button class="relative p-2.5 text-gray-500 hover:text-cyan-600 hover:bg-cyan-50 rounded-xl transition-colors">
                        <i class="fas fa-bell text-lg"></i>
                        <span class="absolute top-1 right-1 w-2.5 h-2.5 bg-red-500 rounded-full border-2 border-white"></span>
                    </button>
                    <!-- Real-Time Clock -->
                    <div class="hidden lg:flex items-center bg-gradient-to-r from-cyan-50 to-blue-50 border border-cyan-200/60 rounded-2xl px-5 py-3 space-x-4">
                        <div class="flex items-center space-x-2">
                            <div class="w-9 h-9 bg-gradient-to-br from-cyan-500 to-blue-600 rounded-xl flex items-center justify-center shadow-md shadow-cyan-500/20">
                                <i class="fas fa-clock text-white text-sm"></i>
                            </div>
                            <div>
                                <p id="live-time" class="text-xl font-bold text-gray-900 tracking-tight leading-none">--:--:--</p>
                                <p id="live-period" class="text-[10px] font-semibold text-cyan-600 uppercase tracking-widest leading-none mt-0.5">--</p>
                            </div>
                        </div>
                        <div class="w-px h-10 bg-cyan-200/80"></div>
                        <div class="flex items-center space-x-2">
                            <div class="w-9 h-9 bg-gradient-to-br from-blue-500 to-indigo-600 rounded-xl flex items-center justify-center shadow-md shadow-blue-500/20">
                                <i class="fas fa-calendar-alt text-white text-sm"></i>
                            </div>
                            <div>
                                <p id="live-day" class="text-xs font-bold text-gray-800 leading-none">---</p>
                                <p id="live-date" class="text-[10px] font-medium text-gray-500 leading-none mt-1">--- --, ----</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Alerts -->
        <div class="px-8 pt-4">
            <c:if test="${not empty success}">
                <div class="mb-4 bg-emerald-50 border border-emerald-200 rounded-xl p-4 flex items-center">
                    <i class="fas fa-check-circle text-emerald-500 mr-3"></i>
                    <span class="text-emerald-800 text-sm">${success}</span>
                </div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="mb-4 bg-red-50 border border-red-200 rounded-xl p-4 flex items-center">
                    <i class="fas fa-exclamation-circle text-red-500 mr-3"></i>
                    <span class="text-red-800 text-sm">${error}</span>
                </div>
            </c:if>
        </div>

        <!-- Main Dashboard Content -->
        <div class="px-8 py-6">

            <!-- 1. Quick Action Buttons -->
            <div class="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-8">
                <a href="reservation?action=add" class="group flex items-center bg-white rounded-2xl border border-gray-100 p-5 shadow-sm hover:shadow-lg hover:border-cyan-200 transition-all hover:-translate-y-0.5">
                    <div class="w-12 h-12 bg-gradient-to-br from-cyan-500 to-blue-600 rounded-xl flex items-center justify-center shadow-md shadow-cyan-500/20 group-hover:scale-110 transition-transform">
                        <i class="fas fa-calendar-plus text-white text-lg"></i>
                    </div>
                    <div class="ml-4">
                        <p class="text-sm font-bold text-gray-900">New Reservation</p>
                        <p class="text-xs text-gray-500 mt-0.5">Book a room for a guest</p>
                    </div>
                    <i class="fas fa-chevron-right text-gray-300 ml-auto group-hover:text-cyan-500 transition-colors"></i>
                </a>
                <a href="guest?action=add" class="group flex items-center bg-white rounded-2xl border border-gray-100 p-5 shadow-sm hover:shadow-lg hover:border-emerald-200 transition-all hover:-translate-y-0.5">
                    <div class="w-12 h-12 bg-gradient-to-br from-emerald-500 to-teal-600 rounded-xl flex items-center justify-center shadow-md shadow-emerald-500/20 group-hover:scale-110 transition-transform">
                        <i class="fas fa-user-plus text-white text-lg"></i>
                    </div>
                    <div class="ml-4">
                        <p class="text-sm font-bold text-gray-900">Register Guest</p>
                        <p class="text-xs text-gray-500 mt-0.5">Add a new guest profile</p>
                    </div>
                    <i class="fas fa-chevron-right text-gray-300 ml-auto group-hover:text-emerald-500 transition-colors"></i>
                </a>
                <a href="reservation?action=list&status=checked_in" class="group flex items-center bg-white rounded-2xl border border-gray-100 p-5 shadow-sm hover:shadow-lg hover:border-purple-200 transition-all hover:-translate-y-0.5">
                    <div class="w-12 h-12 bg-gradient-to-br from-purple-500 to-indigo-600 rounded-xl flex items-center justify-center shadow-md shadow-purple-500/20 group-hover:scale-110 transition-transform">
                        <i class="fas fa-concierge-bell text-white text-lg"></i>
                    </div>
                    <div class="ml-4">
                        <p class="text-sm font-bold text-gray-900">Current Guests</p>
                        <p class="text-xs text-gray-500 mt-0.5">${checkedInReservations} guests staying now</p>
                    </div>
                    <i class="fas fa-chevron-right text-gray-300 ml-auto group-hover:text-purple-500 transition-colors"></i>
                </a>
            </div>

            <!-- 2. Stats Row - 4 Tabs -->
            <div class="grid grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
                <div class="bg-white rounded-2xl border border-gray-100 p-5 shadow-sm hover:shadow-md transition-shadow">
                    <div class="flex items-center justify-between mb-3">
                        <div class="w-10 h-10 bg-cyan-50 rounded-xl flex items-center justify-center">
                            <i class="fas fa-bed text-cyan-600"></i>
                        </div>
                        <span class="text-xs font-medium text-emerald-600 bg-emerald-50 px-2 py-1 rounded-full">
                            <i class="fas fa-check mr-1"></i>${availableRooms} free
                        </span>
                    </div>
                    <p class="text-2xl font-bold text-gray-900">${totalRooms}</p>
                    <p class="text-xs text-gray-500 mt-1">Total Rooms</p>
                </div>
                <div class="bg-white rounded-2xl border border-gray-100 p-5 shadow-sm hover:shadow-md transition-shadow">
                    <div class="flex items-center justify-between mb-3">
                        <div class="w-10 h-10 bg-orange-50 rounded-xl flex items-center justify-center">
                            <i class="fas fa-door-closed text-orange-500"></i>
                        </div>
                        <span class="text-xs font-medium text-orange-600 bg-orange-50 px-2 py-1 rounded-full">
                            <i class="fas fa-clock mr-1"></i>${reservedRooms} reserved
                        </span>
                    </div>
                    <p class="text-2xl font-bold text-gray-900">${occupiedRooms}</p>
                    <p class="text-xs text-gray-500 mt-1">Occupied Rooms</p>
                </div>
                <div class="bg-white rounded-2xl border border-gray-100 p-5 shadow-sm hover:shadow-md transition-shadow">
                    <div class="flex items-center justify-between mb-3">
                        <div class="w-10 h-10 bg-blue-50 rounded-xl flex items-center justify-center">
                            <i class="fas fa-calendar-alt text-blue-600"></i>
                        </div>
                        <span class="text-xs font-medium text-blue-600 bg-blue-50 px-2 py-1 rounded-full">
                            <i class="fas fa-check-circle mr-1"></i>${confirmedReservations} active
                        </span>
                    </div>
                    <p class="text-2xl font-bold text-gray-900">${totalReservations}</p>
                    <p class="text-xs text-gray-500 mt-1">Total Reservations</p>
                </div>
                <div class="bg-white rounded-2xl border border-gray-100 p-5 shadow-sm hover:shadow-md transition-shadow">
                    <div class="flex items-center justify-between mb-3">
                        <div class="w-10 h-10 bg-purple-50 rounded-xl flex items-center justify-center">
                            <i class="fas fa-users text-purple-600"></i>
                        </div>
                        <span class="text-xs font-medium text-purple-600 bg-purple-50 px-2 py-1 rounded-full">
                            <i class="fas fa-user-check mr-1"></i>${checkedInReservations} staying
                        </span>
                    </div>
                    <p class="text-2xl font-bold text-gray-900">${totalGuests}</p>
                    <p class="text-xs text-gray-500 mt-1">Registered Guests</p>
                </div>
            </div>

            <!-- 3. Revenue Overview, Today's Movement, Room Overview -->
            <div class="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-8">
                <!-- Revenue -->
                <div class="bg-gradient-to-br from-cyan-500 via-blue-500 to-blue-600 rounded-2xl shadow-lg shadow-blue-500/20 p-6 text-white">
                    <h3 class="text-sm font-semibold mb-4 flex items-center">
                        <i class="fas fa-chart-line mr-2"></i>Revenue Overview
                    </h3>
                    <div class="grid grid-cols-2 gap-3">
                        <div class="bg-white/15 backdrop-blur rounded-xl p-4">
                            <p class="text-xs opacity-80 mb-1">Today</p>
                            <p class="text-xl font-bold">Rs.<fmt:formatNumber value="${todayRevenue}" pattern="#,##0"/></p>
                        </div>
                        <div class="bg-white/15 backdrop-blur rounded-xl p-4">
                            <p class="text-xs opacity-80 mb-1">This Month</p>
                            <p class="text-xl font-bold">Rs.<fmt:formatNumber value="${monthlyRevenue}" pattern="#,##0"/></p>
                        </div>
                    </div>
                </div>
                <!-- Today's Movement -->
                <div class="bg-white rounded-2xl border border-gray-100 shadow-sm p-6">
                    <h3 class="text-sm font-bold text-gray-900 mb-4">
                        <i class="fas fa-calendar-day mr-2 text-cyan-500"></i>Today's Movement
                    </h3>
                    <div class="grid grid-cols-2 gap-3">
                        <div class="bg-emerald-50 rounded-xl p-4 border border-emerald-100">
                            <div class="flex items-center justify-between">
                                <div>
                                    <p class="text-xs text-emerald-700">Check-ins</p>
                                    <p class="text-2xl font-bold text-emerald-800">${todayCheckInCount}</p>
                                </div>
                                <div class="w-10 h-10 bg-emerald-100 rounded-full flex items-center justify-center">
                                    <i class="fas fa-sign-in-alt text-emerald-600"></i>
                                </div>
                            </div>
                        </div>
                        <div class="bg-red-50 rounded-xl p-4 border border-red-100">
                            <div class="flex items-center justify-between">
                                <div>
                                    <p class="text-xs text-red-700">Check-outs</p>
                                    <p class="text-2xl font-bold text-red-800">${todayCheckOutCount}</p>
                                </div>
                                <div class="w-10 h-10 bg-red-100 rounded-full flex items-center justify-center">
                                    <i class="fas fa-sign-out-alt text-red-600"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- Room Overview -->
                <div class="bg-white rounded-2xl border border-gray-100 shadow-sm p-6">
                    <h3 class="text-sm font-bold text-gray-900 mb-4">
                        <i class="fas fa-chart-pie mr-2 text-cyan-500"></i>Room Overview
                    </h3>
                    <div class="space-y-3">
                        <div class="flex items-center justify-between">
                            <div class="flex items-center space-x-2">
                                <span class="w-3 h-3 rounded-full bg-emerald-500"></span>
                                <span class="text-sm text-gray-600">Available</span>
                            </div>
                            <span class="text-sm font-bold text-gray-900">${availableRooms}</span>
                        </div>
                        <div class="w-full bg-gray-100 rounded-full h-2">
                            <div class="bg-emerald-500 h-2 rounded-full" style="width: ${totalRooms > 0 ? (availableRooms * 100 / totalRooms) : 0}%"></div>
                        </div>
                        <div class="flex items-center justify-between">
                            <div class="flex items-center space-x-2">
                                <span class="w-3 h-3 rounded-full bg-cyan-500"></span>
                                <span class="text-sm text-gray-600">Occupied</span>
                            </div>
                            <span class="text-sm font-bold text-gray-900">${occupiedRooms}</span>
                        </div>
                        <div class="flex items-center justify-between">
                            <div class="flex items-center space-x-2">
                                <span class="w-3 h-3 rounded-full bg-amber-500"></span>
                                <span class="text-sm text-gray-600">Reserved</span>
                            </div>
                            <span class="text-sm font-bold text-gray-900">${reservedRooms}</span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 4. Expected Check-ins Today + Recent Activity (side by side) -->
            <div class="grid grid-cols-1 xl:grid-cols-3 gap-6 mb-8">
                <!-- Expected Check-ins Today (wider) -->
                <div class="xl:col-span-2 bg-white rounded-2xl border border-gray-100 shadow-sm overflow-hidden">
                    <div class="px-6 py-5 border-b border-gray-100 flex items-center justify-between">
                        <div>
                            <h3 class="text-lg font-bold text-gray-900">Expected Check-ins Today</h3>
                            <p class="text-xs text-gray-500 mt-0.5">${todayCheckInCount} guests expected</p>
                        </div>
                        <a href="reservation?action=list&status=confirmed" class="text-sm text-cyan-600 hover:text-cyan-700 font-medium">
                            View all <i class="fas fa-arrow-right ml-1"></i>
                        </a>
                    </div>
                    <c:choose>
                        <c:when test="${not empty todayCheckIns}">
                            <div class="overflow-x-auto">
                                <table class="min-w-full">
                                    <thead>
                                        <tr class="bg-gray-50/80">
                                            <th class="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Guest</th>
                                            <th class="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Room</th>
                                            <th class="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Nights</th>
                                            <th class="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Status</th>
                                            <th class="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Action</th>
                                        </tr>
                                    </thead>
                                    <tbody class="divide-y divide-gray-100">
                                        <c:forEach var="reservation" items="${todayCheckIns}">
                                            <tr class="hover:bg-gray-50/50 transition-colors">
                                                <td class="px-6 py-4">
                                                    <div class="flex items-center space-x-3">
                                                        <div class="w-9 h-9 bg-gradient-to-br from-cyan-400 to-blue-500 rounded-full flex items-center justify-center text-white text-xs font-bold">
                                                            ${fn:substring(reservation.guest.guestName, 0, 1)}
                                                        </div>
                                                        <div>
                                                            <p class="text-sm font-semibold text-gray-900">${reservation.guest.guestName}</p>
                                                            <p class="text-xs text-gray-500">${reservation.guest.contactNumber}</p>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td class="px-6 py-4">
                                                    <p class="text-sm font-medium text-gray-900">${reservation.room.roomType.typeName} ${reservation.room.roomNumber}</p>
                                                </td>
                                                <td class="px-6 py-4">
                                                    <p class="text-sm text-gray-700">${reservation.numberOfNights} nights</p>
                                                </td>
                                                <td class="px-6 py-4">
                                                    <span class="inline-flex items-center px-3 py-1 text-xs font-bold rounded-full bg-emerald-100 text-emerald-700 border border-emerald-200">CONFIRMED</span>
                                                </td>
                                                <td class="px-6 py-4">
                                                    <a href="reservation?action=checkin&id=${reservation.reservationId}"
                                                        class="inline-flex items-center px-3 py-1.5 bg-cyan-50 text-cyan-700 rounded-lg text-xs font-semibold hover:bg-cyan-100 transition-colors">
                                                        <i class="fas fa-sign-in-alt mr-1.5"></i>Check In
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="text-center py-12 text-gray-400">
                                <div class="w-16 h-16 bg-gray-50 rounded-full flex items-center justify-center mx-auto mb-3">
                                    <i class="fas fa-calendar-check text-3xl text-gray-300"></i>
                                </div>
                                <p class="text-sm font-medium">No check-ins expected today</p>
                                <p class="text-xs mt-1">All clear for now!</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Recent Activity Feed -->
                <div class="xl:col-span-1">
                    <div class="bg-white rounded-2xl border border-gray-100 shadow-sm h-full flex flex-col">
                        <div class="px-5 py-4 border-b border-gray-100 flex items-center justify-between">
                            <h3 class="text-sm font-bold text-gray-900">
                                <i class="fas fa-stream mr-2 text-cyan-500"></i>Recent Activity
                            </h3>
                            <c:if test="${sessionScope.role == 'admin'}">
                                <a href="activity?action=list" class="text-xs text-cyan-600 hover:text-cyan-700 font-medium">
                                    View all <i class="fas fa-arrow-right ml-1"></i>
                                </a>
                            </c:if>
                        </div>
                        <div class="p-4 space-y-1 flex-1 overflow-y-auto" style="max-height: 420px;">
                            <c:choose>
                                <c:when test="${not empty recentActivities}">
                                    <c:forEach var="activity" items="${recentActivities}" end="9">
                                        <div class="flex items-start space-x-3 p-3 rounded-xl hover:bg-gray-50 transition-colors">
                                            <div class="w-8 h-8 rounded-full flex items-center justify-center flex-shrink-0 mt-0.5
                                                ${activity.activityType == 'reservation_created' ? 'bg-blue-100' : ''}
                                                ${activity.activityType == 'check_in' ? 'bg-emerald-100' : ''}
                                                ${activity.activityType == 'check_out' ? 'bg-orange-100' : ''}
                                                ${activity.activityType == 'guest_added' ? 'bg-purple-100' : ''}
                                                ${activity.activityType == 'room_status_updated' ? 'bg-cyan-100' : ''}
                                                ${activity.activityType == 'reservation_cancelled' ? 'bg-red-100' : ''}
                                                ${activity.activityType == 'user_login' || activity.activityType == 'user_logout' ? 'bg-gray-100' : ''}">
                                                <i class="text-xs
                                                    ${activity.activityType == 'reservation_created' ? 'fas fa-calendar-plus text-blue-600' : ''}
                                                    ${activity.activityType == 'check_in' ? 'fas fa-sign-in-alt text-emerald-600' : ''}
                                                    ${activity.activityType == 'check_out' ? 'fas fa-sign-out-alt text-orange-600' : ''}
                                                    ${activity.activityType == 'guest_added' ? 'fas fa-user-plus text-purple-600' : ''}
                                                    ${activity.activityType == 'room_status_updated' ? 'fas fa-bed text-cyan-600' : ''}
                                                    ${activity.activityType == 'reservation_cancelled' ? 'fas fa-times-circle text-red-600' : ''}
                                                    ${activity.activityType == 'user_login' || activity.activityType == 'user_logout' ? 'fas fa-user text-gray-600' : ''}"></i>
                                            </div>
                                            <div class="flex-1 min-w-0">
                                                <p class="text-xs text-gray-800 leading-relaxed">${activity.description}</p>
                                                <p class="text-xs text-gray-400 mt-1">${activity.timestamp}</p>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <div class="text-center py-8 text-gray-400">
                                        <i class="fas fa-stream text-3xl mb-2"></i>
                                        <p class="text-xs">No recent activity</p>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <footer class="border-t border-gray-200 bg-white mt-auto px-8 py-4">
            <p class="text-center text-xs text-gray-400">&copy; 2026 Ocean View Resort, Galle, Sri Lanka. All rights reserved.</p>
        </footer>
    </main>
</div>

<script>
    function updateClock() {
        const now = new Date();
        let hours = now.getHours();
        const minutes = now.getMinutes().toString().padStart(2, '0');
        const seconds = now.getSeconds().toString().padStart(2, '0');
        const period = hours >= 12 ? 'PM' : 'AM';
        const displayHours = hours % 12 || 12;

        // Live time
        const timeEl = document.getElementById('live-time');
        if (timeEl) timeEl.textContent = displayHours + ':' + minutes + ':' + seconds;
        const periodEl = document.getElementById('live-period');
        if (periodEl) periodEl.textContent = period;

        // Live date
        const days = ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'];
        const months = ['January','February','March','April','May','June','July','August','September','October','November','December'];
        const dayEl = document.getElementById('live-day');
        if (dayEl) dayEl.textContent = days[now.getDay()];
        const dateEl = document.getElementById('live-date');
        if (dateEl) dateEl.textContent = months[now.getMonth()] + ' ' + now.getDate() + ', ' + now.getFullYear();

        // Greeting
        let greeting = 'Good Morning';
        if (hours >= 12 && hours < 17) greeting = 'Good Afternoon';
        else if (hours >= 17) greeting = 'Good Evening';
        const greetEl = document.getElementById('greeting-text');
        if (greetEl) greetEl.textContent = greeting;
    }
    updateClock();
    setInterval(updateClock, 1000);
</script>
</body>
</html>
