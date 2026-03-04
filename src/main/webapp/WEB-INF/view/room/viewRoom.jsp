<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ include file="../shared/layout.jsp"%>
<div class="px-8 py-6 max-w-3xl">
    <div class="mb-6">
        <a href="room?action=list" class="text-cyan-600 hover:text-cyan-700 mb-2 inline-block">
            <i class="fas fa-arrow-left mr-2"></i>Back to Rooms
        </a>
        <h1 class="text-2xl font-bold text-gray-900">Room Details</h1>
    </div>

    <div class="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
        <!-- Header -->
        <div class="bg-gradient-to-r from-cyan-500 to-blue-600 p-6">
            <div class="flex items-center justify-between">
                <div class="flex items-center">
                    <div class="w-16 h-16 bg-white/20 rounded-full flex items-center justify-center">
                        <i class="fas fa-bed text-white text-2xl"></i>
                    </div>
                    <div class="ml-4 text-white">
                        <h2 class="text-2xl font-bold">Room ${room.roomNumber}</h2>
                        <c:if test="${room.roomType != null}">
                            <p class="opacity-80">${room.roomType.typeName}</p>
                        </c:if>
                    </div>
                </div>
                <span class="px-3 py-1 text-sm font-medium rounded-full
                    ${room.status == 'available' ? 'bg-green-100 text-green-800' : ''}
                    ${room.status == 'occupied' ? 'bg-red-100 text-red-800' : ''}
                    ${room.status == 'reserved' ? 'bg-yellow-100 text-yellow-800' : ''}
                    ${room.status == 'maintenance' ? 'bg-gray-100 text-gray-800' : ''}">
                    ${room.status}
                </span>
            </div>
        </div>

        <!-- Details -->
        <div class="p-6">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                    <h3 class="text-sm font-medium text-gray-500 mb-1">Room Number</h3>
                    <p class="text-gray-900 font-medium">
                        <i class="fas fa-door-open mr-2 text-cyan-600"></i>${room.roomNumber}
                    </p>
                </div>
                <div>
                    <h3 class="text-sm font-medium text-gray-500 mb-1">Floor</h3>
                    <p class="text-gray-900 font-medium">
                        <i class="fas fa-building mr-2 text-cyan-600"></i>Floor ${room.floorNumber}
                    </p>
                </div>
                <c:if test="${room.roomType != null}">
                    <div>
                        <h3 class="text-sm font-medium text-gray-500 mb-1">Room Type</h3>
                        <p class="text-gray-900 font-medium">
                            <i class="fas fa-tag mr-2 text-cyan-600"></i>${room.roomType.typeName}
                        </p>
                    </div>
                    <div>
                        <h3 class="text-sm font-medium text-gray-500 mb-1">Rate Per Night</h3>
                        <p class="text-gray-900 font-medium">
                            <i class="fas fa-money-bill-wave mr-2 text-cyan-600"></i>Rs. <fmt:formatNumber value="${room.roomType.ratePerNight}" pattern="#,##0.00"/>
                        </p>
                    </div>
                    <div>
                        <h3 class="text-sm font-medium text-gray-500 mb-1">Max Occupancy</h3>
                        <p class="text-gray-900 font-medium">
                            <i class="fas fa-users mr-2 text-cyan-600"></i>${room.roomType.maxOccupancy} guests
                        </p>
                    </div>
                </c:if>
                <div>
                    <h3 class="text-sm font-medium text-gray-500 mb-1">Status</h3>
                    <p class="text-gray-900 font-medium">
                        <i class="fas fa-info-circle mr-2 text-cyan-600"></i>
                        <span class="px-2 py-1 text-xs font-medium rounded-full
                            ${room.status == 'available' ? 'bg-green-100 text-green-800' : ''}
                            ${room.status == 'occupied' ? 'bg-red-100 text-red-800' : ''}
                            ${room.status == 'reserved' ? 'bg-yellow-100 text-yellow-800' : ''}
                            ${room.status == 'maintenance' ? 'bg-gray-100 text-gray-800' : ''}">
                            ${room.status}
                        </span>
                    </p>
                </div>
                <c:if test="${not empty room.description}">
                    <div class="md:col-span-2">
                        <h3 class="text-sm font-medium text-gray-500 mb-1">Description</h3>
                        <p class="text-gray-900 font-medium">
                            <i class="fas fa-align-left mr-2 text-cyan-600"></i>${room.description}
                        </p>
                    </div>
                </c:if>
                <div>
                    <h3 class="text-sm font-medium text-gray-500 mb-1">Created On</h3>
                    <p class="text-gray-900 font-medium">
                        <i class="fas fa-calendar mr-2 text-cyan-600"></i>${room.createdAt}
                    </p>
                </div>
                <c:if test="${room.updatedAt != null}">
                    <div>
                        <h3 class="text-sm font-medium text-gray-500 mb-1">Last Updated</h3>
                        <p class="text-gray-900 font-medium">
                            <i class="fas fa-clock mr-2 text-cyan-600"></i>${room.updatedAt}
                        </p>
                    </div>
                </c:if>
            </div>
        </div>

        <!-- Actions -->
        <div class="bg-gray-50 px-6 py-4 border-t border-gray-200">
            <!-- Reservation Management -->
            <div class="mb-4">
                <h3 class="text-sm font-medium text-gray-700 mb-3">
                    <i class="fas fa-calendar-alt mr-1"></i>Reservation Management
                </h3>
                <div class="flex flex-wrap gap-2">
                    <a href="reservation?action=list&search=${room.roomNumber}"
                        class="px-4 py-2.5 bg-gradient-to-r from-cyan-500 to-blue-600 text-white rounded-xl hover:from-cyan-600 hover:to-blue-700 shadow-lg shadow-cyan-500/25 transition-colors">
                        <i class="fas fa-list mr-2"></i>View Reservations
                    </a>
                    <c:if test="${room.status == 'available'}">
                        <a href="reservation?action=add&roomId=${room.roomId}"
                            class="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors">
                            <i class="fas fa-calendar-plus mr-2"></i>New Reservation
                        </a>
                    </c:if>
                    <c:if test="${not empty activeReservation}">
                        <a href="reservation?action=view&id=${activeReservation.reservationId}"
                            class="px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition-colors">
                            <i class="fas fa-eye mr-2"></i>View Reservation
                        </a>
                        <a href="reservation?action=delete&id=${activeReservation.reservationId}"
                            onclick="return confirm('Are you sure you want to delete reservation ${activeReservation.reservationNumber}? This action cannot be undone.');"
                            class="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors">
                            <i class="fas fa-trash mr-2"></i>Delete Reservation
                        </a>
                    </c:if>
                </div>
            </div>

            <!-- Room Actions -->
            <div class="flex justify-end border-t border-gray-200 pt-4">
                <div class="space-x-2">
                    <c:if test="${sessionScope.role == 'admin'}">
                        <a href="room?action=edit&id=${room.roomId}"
                            class="px-4 py-2 bg-yellow-600 text-white rounded-lg hover:bg-yellow-700 transition-colors">
                            <i class="fas fa-edit mr-2"></i>Edit
                        </a>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>

<footer class="border-t border-gray-200 bg-white mt-auto px-8 py-4">
    <p class="text-center text-xs text-gray-400">&copy; 2026 Ocean View Resort, Galle, Sri Lanka. All rights reserved.</p>
</footer>
    </div>
</div>
</body>
</html>