<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ include file="../shared/layout.jsp"%>
<div class="px-8 py-6 max-w-3xl">
    <!-- Page Header -->
    <div class="mb-6">
        <a href="room?action=list" class="text-cyan-600 hover:text-cyan-700 mb-2 inline-block">
            <i class="fas fa-arrow-left mr-2"></i>Back to Rooms
        </a>
        <h1 class="text-2xl font-bold text-gray-900">Add New Room</h1>
        <p class="text-gray-600">Enter room details to add a new room</p>
    </div>

    <!-- Error Messages -->
    <c:if test="${not empty error}">
        <div class="mb-6 bg-red-50 border border-red-200 rounded-xl p-4">
            <div class="flex items-center">
                <i class="fas fa-exclamation-circle text-red-500 mr-2"></i>
                <span class="text-red-800">${error}</span>
            </div>
        </div>
    </c:if>

    <!-- Add Room Form -->
    <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
        <form action="room" method="post">
            <input type="hidden" name="action" value="add">

            <div class="space-y-6">
                <!-- Room Number -->
                <div>
                    <label for="roomNumber" class="block text-sm font-medium text-gray-700 mb-2">
                        Room Number <span class="text-red-500">*</span>
                    </label>
                    <input type="text" id="roomNumber" name="roomNumber" value="${roomNumber}"
                        class="w-full px-4 py-2 border ${fieldErrors.roomNumber != null ? 'border-red-500' : 'border-gray-300'} rounded-lg focus:ring-2 focus:ring-cyan-400 focus:border-cyan-400"
                        placeholder="e.g., 101, 201A" required>
                    <c:if test="${fieldErrors.roomNumber != null}">
                        <p class="mt-1 text-sm text-red-500">${fieldErrors.roomNumber}</p>
                    </c:if>
                </div>

                <!-- Room Type -->
                <div>
                    <label for="roomTypeId" class="block text-sm font-medium text-gray-700 mb-2">
                        Room Type <span class="text-red-500">*</span>
                    </label>
                    <select id="roomTypeId" name="roomTypeId"
                        class="w-full px-4 py-2 border ${fieldErrors.roomTypeId != null ? 'border-red-500' : 'border-gray-300'} rounded-lg focus:ring-2 focus:ring-cyan-400 focus:border-cyan-400"
                        required>
                        <option value="">-- Select Room Type --</option>
                        <c:forEach var="type" items="${roomTypes}">
                            <option value="${type.roomTypeId}" ${roomTypeId == type.roomTypeId ? 'selected' : ''}>
                                ${type.typeName} - Rs. <fmt:formatNumber value="${type.ratePerNight}" pattern="#,##0.00"/> / night (Max ${type.maxOccupancy} guests)
                            </option>
                        </c:forEach>
                    </select>
                    <c:if test="${fieldErrors.roomTypeId != null}">
                        <p class="mt-1 text-sm text-red-500">${fieldErrors.roomTypeId}</p>
                    </c:if>
                </div>

                <!-- Floor Number -->
                <div>
                    <label for="floorNumber" class="block text-sm font-medium text-gray-700 mb-2">
                        Floor Number <span class="text-red-500">*</span>
                    </label>
                    <input type="number" id="floorNumber" name="floorNumber" value="${floorNumber != null ? floorNumber : '1'}"
                        min="1" max="50"
                        class="w-full px-4 py-2 border ${fieldErrors.floorNumber != null ? 'border-red-500' : 'border-gray-300'} rounded-lg focus:ring-2 focus:ring-cyan-400 focus:border-cyan-400"
                        placeholder="e.g., 1" required>
                    <c:if test="${fieldErrors.floorNumber != null}">
                        <p class="mt-1 text-sm text-red-500">${fieldErrors.floorNumber}</p>
                    </c:if>
                </div>

                <!-- Description -->
                <div>
                    <label for="description" class="block text-sm font-medium text-gray-700 mb-2">
                        Description
                    </label>
                    <textarea id="description" name="description" rows="3"
                        class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-cyan-400 focus:border-cyan-400"
                        placeholder="Enter room description (optional)">${description}</textarea>
                </div>

                <!-- Submit Button -->
                <div class="flex justify-end space-x-4 pt-4">
                    <a href="room?action=list"
                        class="px-6 py-2 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 transition-colors">
                        Cancel
                    </a>
                    <button type="submit"
                        class="px-6 py-2.5 bg-gradient-to-r from-cyan-500 to-blue-600 text-white rounded-xl hover:from-cyan-600 hover:to-blue-700 shadow-lg shadow-cyan-500/25 transition-colors">
                        <i class="fas fa-plus mr-2"></i>Add Room
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>

<footer class="border-t border-gray-200 bg-white mt-auto px-8 py-4">
    <p class="text-center text-xs text-gray-400">&copy; 2026 Ocean View Resort, Galle, Sri Lanka. All rights reserved.</p>
</footer>
    </div>
</div>
</body>
</html>