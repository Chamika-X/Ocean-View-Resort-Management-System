<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ include file="../shared/layout.jsp"%>
<div class="px-8 py-6">
    <!-- Page Header -->
    <div class="flex justify-between items-center mb-6">
        <div>
            <h1 class="text-2xl font-bold text-gray-900">Reservations</h1>
            <p class="text-gray-600">Manage hotel room reservations</p>
        </div>
        <a href="reservation?action=add"
            class="inline-flex items-center px-4 py-2.5 bg-gradient-to-r from-cyan-500 to-blue-600 text-white rounded-xl hover:from-cyan-600 hover:to-blue-700 shadow-lg shadow-cyan-500/25 transition-colors">
            <i class="fas fa-calendar-plus mr-2"></i>New Reservation
        </a>
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

    <c:if test="${param.error != null}">
        <div class="mb-6 bg-red-50 border border-red-200 rounded-xl p-4">
            <div class="flex items-center">
                <i class="fas fa-exclamation-circle text-red-500 mr-2"></i>
                <span class="text-red-800">${param.error}</span>
            </div>
        </div>
    </c:if>

    <!-- Search and Filter Bar -->
    <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-4 mb-6">
        <form action="reservation" method="get" class="flex flex-wrap gap-4">
            <input type="hidden" name="action" value="list">
            <div class="flex-1 min-w-[200px]">
                <input type="text" name="search" value="${searchTerm}"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-cyan-400 focus:border-cyan-400"
                    placeholder="Search by reservation #, guest name, or room...">
            </div>
            <select name="status" class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-cyan-400">
                <option value="">All Status</option>
                <option value="confirmed" ${statusFilter == 'confirmed' ? 'selected' : ''}>Confirmed</option>
                <option value="checked_in" ${statusFilter == 'checked_in' ? 'selected' : ''}>Checked In</option>
                <option value="checked_out" ${statusFilter == 'checked_out' ? 'selected' : ''}>Checked Out</option>
                <option value="cancelled" ${statusFilter == 'cancelled' ? 'selected' : ''}>Cancelled</option>
            </select>
            <button type="submit"
                class="px-6 py-2.5 bg-gradient-to-r from-cyan-500 to-blue-600 text-white rounded-xl hover:from-cyan-600 hover:to-blue-700 shadow-lg shadow-cyan-500/25 transition-colors">
                <i class="fas fa-search mr-2"></i>Search
            </button>
            <c:if test="${not empty searchTerm || not empty statusFilter}">
                <a href="reservation?action=list"
                    class="px-6 py-2 bg-white border border-gray-200 text-gray-700 rounded-xl hover:bg-gray-50 transition-colors">
                    <i class="fas fa-times mr-2"></i>Clear
                </a>
            </c:if>
        </form>
    </div>

    <!-- Reservations Table -->
    <div class="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
        <c:choose>
            <c:when test="${not empty reservations}">
                <div class="overflow-x-auto">
                    <table class="min-w-full divide-y divide-gray-200">
                        <thead class="bg-gray-50/80">
                            <tr>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Reservation #</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Guest</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Room</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Check-in</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Check-out</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Total</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                            </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                            <c:forEach var="reservation" items="${reservations}">
                                <tr class="hover:bg-gray-50">
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <div class="text-sm font-medium text-cyan-600">${reservation.reservationNumber}</div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <div class="text-sm font-medium text-gray-900">${reservation.guest.guestName}</div>
                                        <div class="text-sm text-gray-500">${reservation.guest.contactNumber}</div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <div class="text-sm font-medium text-gray-900">Room ${reservation.room.roomNumber}</div>
                                        <div class="text-sm text-gray-500">${reservation.room.roomType.typeName}</div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <div class="text-sm text-gray-900">${reservation.checkInDate}</div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <div class="text-sm text-gray-900">${reservation.checkOutDate}</div>
                                        <div class="text-sm text-gray-500">${reservation.numberOfNights} nights</div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <div class="text-sm font-medium text-gray-900">
                                            Rs. <fmt:formatNumber value="${reservation.totalAmount}" pattern="#,##0.00"/>
                                        </div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <span class="px-2 py-1 text-xs font-medium rounded-full ${reservation.statusBadgeClass}">
                                            ${reservation.statusDisplayText}
                                        </span>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm">
                                        <div class="flex space-x-2">
                                            <a href="reservation?action=view&id=${reservation.reservationId}"
                                                class="text-cyan-600 hover:text-cyan-800" title="View">
                                                <i class="fas fa-eye"></i>
                                            </a>
                                            <c:if test="${reservation.status == 'confirmed'}">
                                                <a href="reservation?action=checkin&id=${reservation.reservationId}"
                                                    class="text-green-600 hover:text-green-800" title="Check In">
                                                    <i class="fas fa-sign-in-alt"></i>
                                                </a>
                                            </c:if>
                                            <c:if test="${reservation.status == 'checked_in'}">
                                                <a href="reservation?action=checkout&id=${reservation.reservationId}"
                                                    class="text-red-600 hover:text-red-800" title="Check Out">
                                                    <i class="fas fa-sign-out-alt"></i>
                                                </a>
                                            </c:if>
                                            <c:if test="${reservation.status == 'checked_out'}">
                                                <a href="reservation?action=bill&id=${reservation.reservationId}"
                                                    class="text-purple-600 hover:text-purple-800" title="View Bill">
                                                    <i class="fas fa-file-invoice-dollar"></i>
                                                </a>
                                                <a href="reservation?action=delete&id=${reservation.reservationId}"
                                                    onclick="return confirm('Are you sure you want to delete reservation ${reservation.reservationNumber}? This action cannot be undone.');"
                                                    class="text-red-600 hover:text-red-800" title="Delete">
                                                    <i class="fas fa-trash"></i>
                                                </a>
                                            </c:if>
                                            <c:if test="${reservation.status == 'cancelled'}">
                                                <a href="reservation?action=delete&id=${reservation.reservationId}"
                                                    onclick="return confirm('Are you sure you want to delete reservation ${reservation.reservationNumber}? This action cannot be undone.');"
                                                    class="text-red-600 hover:text-red-800" title="Delete">
                                                    <i class="fas fa-trash"></i>
                                                </a>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!-- Pagination -->
                <c:if test="${pagination.totalPages > 1}">
                    <div class="bg-gray-50 px-6 py-3 flex items-center justify-between border-t border-gray-200">
                        <div class="text-sm text-gray-700">
                            Showing <span class="font-medium">${pagination.startItem}</span> to 
                            <span class="font-medium">${pagination.endItem}</span> of 
                            <span class="font-medium">${pagination.totalItems}</span> reservations
                        </div>
                        <div class="flex space-x-2">
                            <c:if test="${pagination.currentPage > 1}">
                                <a href="reservation?action=list&page=${pagination.currentPage - 1}&search=${searchTerm}&status=${statusFilter}"
                                    class="px-3 py-1 bg-white border border-gray-300 rounded text-sm hover:bg-gray-50">
                                    Previous
                                </a>
                            </c:if>
                            <c:if test="${pagination.currentPage < pagination.totalPages}">
                                <a href="reservation?action=list&page=${pagination.currentPage + 1}&search=${searchTerm}&status=${statusFilter}"
                                    class="px-3 py-1 bg-white border border-gray-300 rounded text-sm hover:bg-gray-50">
                                    Next
                                </a>
                            </c:if>
                        </div>
                    </div>
                </c:if>
            </c:when>
            <c:otherwise>
                <div class="text-center py-12">
                    <i class="fas fa-calendar-alt text-gray-300 text-5xl mb-4"></i>
                    <p class="text-gray-500 text-lg">No reservations found</p>
                    <p class="text-gray-400 text-sm mt-2">Create a new reservation to get started</p>
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