<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ include file="../shared/layout.jsp"%>
<div class="px-8 py-6">
    <!-- Page Header -->
    <div class="flex justify-between items-center mb-6">
        <div>
            <h1 class="text-2xl font-bold text-gray-900">Room Management</h1>
            <p class="text-gray-600">View and manage hotel rooms</p>
        </div>
        <c:if test="${sessionScope.role == 'admin'}">
            <a href="room?action=add"
                class="inline-flex items-center px-4 py-2.5 bg-gradient-to-r from-cyan-500 to-blue-600 text-white rounded-xl hover:from-cyan-600 hover:to-blue-700 shadow-lg shadow-cyan-500/25 transition-colors">
                <i class="fas fa-plus mr-2"></i>Add New Room
            </a>
        </c:if>
    </div>

    <!-- Alerts -->
    <c:if test="${param.success != null}">
        <div class="mb-6 bg-emerald-50 border border-emerald-200 rounded-xl p-4">
            <div class="flex items-center">
                <i class="fas fa-check-circle text-green-500 mr-2"></i>
                <span class="text-green-800">${param.success}</span>
            </div>
        </div>
    </c:if>

    <!-- Room Status Summary -->
    <div class="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
        <a href="room?action=list&status=available" 
           class="bg-emerald-50 border border-emerald-200 rounded-xl p-4 hover:bg-green-100 transition-colors ${statusFilter == 'available' ? 'ring-2 ring-green-500' : ''}">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-green-700">Available</p>
                    <p class="text-2xl font-bold text-green-800">${availableCount}</p>
                </div>
                <i class="fas fa-check-circle text-green-500 text-2xl"></i>
            </div>
        </a>
        <a href="room?action=list&status=occupied"
           class="bg-red-50 border border-red-200 rounded-xl p-4 hover:bg-red-100 transition-colors ${statusFilter == 'occupied' ? 'ring-2 ring-red-500' : ''}">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-red-700">Occupied</p>
                    <p class="text-2xl font-bold text-red-800">${occupiedCount}</p>
                </div>
                <i class="fas fa-user text-red-500 text-2xl"></i>
            </div>
        </a>
        <a href="room?action=list&status=reserved"
           class="bg-yellow-50 border border-yellow-200 rounded-lg p-4 hover:bg-yellow-100 transition-colors ${statusFilter == 'reserved' ? 'ring-2 ring-yellow-500' : ''}">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-yellow-700">Reserved</p>
                    <p class="text-2xl font-bold text-yellow-800">${reservedCount}</p>
                </div>
                <i class="fas fa-calendar-check text-yellow-500 text-2xl"></i>
            </div>
        </a>
        <a href="room?action=list&status=maintenance"
           class="bg-gray-50 border border-gray-200 rounded-lg p-4 hover:bg-gray-100 transition-colors ${statusFilter == 'maintenance' ? 'ring-2 ring-gray-500' : ''}">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-gray-700">Maintenance</p>
                    <p class="text-2xl font-bold text-gray-800">${maintenanceCount}</p>
                </div>
                <i class="fas fa-tools text-gray-500 text-2xl"></i>
            </div>
        </a>
    </div>

    <c:if test="${not empty statusFilter}">
        <div class="mb-4">
            <a href="room?action=list" class="text-cyan-600 hover:text-cyan-700">
                <i class="fas fa-times mr-1"></i>Clear filter
            </a>
        </div>
    </c:if>

    <!-- Rooms Grid -->
    <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
        <c:choose>
            <c:when test="${not empty rooms}">
                <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
                    <c:forEach var="room" items="${rooms}">
                        <div class="border rounded-lg p-4 hover:shadow-md transition-shadow
                            ${room.status == 'available' ? 'border-green-200 bg-green-50' : ''}
                            ${room.status == 'occupied' ? 'border-red-200 bg-red-50' : ''}
                            ${room.status == 'reserved' ? 'border-yellow-200 bg-yellow-50' : ''}
                            ${room.status == 'maintenance' ? 'border-gray-200 bg-gray-50' : ''}">
                            <div class="flex justify-between items-start mb-3">
                                <div>
                                    <h3 class="text-lg font-bold text-gray-900">Room ${room.roomNumber}</h3>
                                    <p class="text-sm text-gray-600">Floor ${room.floorNumber}</p>
                                </div>
                                <span class="px-2 py-1 text-xs font-medium rounded-full
                                    ${room.status == 'available' ? 'bg-green-100 text-green-800' : ''}
                                    ${room.status == 'occupied' ? 'bg-red-100 text-red-800' : ''}
                                    ${room.status == 'reserved' ? 'bg-yellow-100 text-yellow-800' : ''}
                                    ${room.status == 'maintenance' ? 'bg-gray-100 text-gray-800' : ''}">
                                    ${room.status}
                                </span>
                            </div>
                            <c:if test="${room.roomType != null}">
                                <p class="text-sm font-medium text-cyan-600 mb-1">${room.roomType.typeName}</p>
                                <p class="text-sm text-gray-600 mb-2">
                                    Rs. <fmt:formatNumber value="${room.roomType.ratePerNight}" pattern="#,##0.00"/> / night
                                </p>
                                <p class="text-xs text-gray-500 mb-3">
                                    <i class="fas fa-users mr-1"></i>Max ${room.roomType.maxOccupancy} guests
                                </p>
                            </c:if>
                            <div class="flex space-x-2">
                                <a href="room?action=view&id=${room.roomId}"
                                    class="flex-1 text-center px-2 py-1 text-xs bg-cyan-100 text-cyan-700 rounded hover:bg-cyan-200">
                                    View
                                </a>
                                <c:if test="${room.status == 'available'}">
                                    <a href="reservation?action=add&roomId=${room.roomId}"
                                        class="flex-1 text-center px-2 py-1 text-xs bg-green-100 text-green-700 rounded hover:bg-green-200">
                                        Reserve
                                    </a>
                                </c:if>
                                <c:if test="${sessionScope.role == 'admin'}">
                                    <a href="room?action=edit&id=${room.roomId}"
                                        class="px-2 py-1 text-xs bg-yellow-100 text-yellow-700 rounded hover:bg-yellow-200">
                                        <i class="fas fa-edit"></i>
                                    </a>
                                </c:if>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="text-center py-12">
                    <i class="fas fa-bed text-gray-300 text-5xl mb-4"></i>
                    <p class="text-gray-500 text-lg">No rooms found</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<footer class="border-t border-gray-200 bg-white mt-auto px-8 py-4">
    <p class="text-center text-xs text-gray-400">&copy; 2026 Ocean View Resort, Galle, Sri Lanka. All rights reserved.</p>
</footer>
    </div>
</div>
</body>
</html>