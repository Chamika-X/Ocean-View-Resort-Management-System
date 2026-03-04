<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ include file="../shared/layout.jsp"%>
<div class="px-8 py-6 max-w-4xl">
    <!-- Page Header -->
    <div class="mb-6">
        <a href="reservation?action=list" class="text-cyan-600 hover:text-cyan-700 mb-2 inline-block">
            <i class="fas fa-arrow-left mr-2"></i>Back to Reservations
        </a>
        <div class="flex justify-between items-start">
            <div>
                <h1 class="text-2xl font-bold text-gray-900">Reservation Details</h1>
                <p class="text-cyan-600 font-mono text-lg">${reservation.reservationNumber}</p>
            </div>
            <span class="px-3 py-1 text-sm font-medium rounded-full ${reservation.statusBadgeClass}">
                ${reservation.statusDisplayText}
            </span>
        </div>
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

    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <!-- Guest Information -->
        <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
            <h2 class="text-lg font-semibold text-gray-900 mb-4">
                <i class="fas fa-user mr-2 text-cyan-600"></i>Guest Information
            </h2>
            <div class="space-y-3">
                <div>
                    <span class="text-sm text-gray-500">Name</span>
                    <p class="font-medium text-gray-900">${reservation.guest.guestName}</p>
                </div>
                <div>
                    <span class="text-sm text-gray-500">Contact</span>
                    <p class="font-medium text-gray-900">${reservation.guest.contactNumber}</p>
                </div>
                <div>
                    <span class="text-sm text-gray-500">Email</span>
                    <p class="font-medium text-gray-900">${reservation.guest.email != null ? reservation.guest.email : 'N/A'}</p>
                </div>
                <div>
                    <span class="text-sm text-gray-500">Address</span>
                    <p class="font-medium text-gray-900">${reservation.guest.address}</p>
                </div>
            </div>
        </div>

        <!-- Room Information -->
        <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
            <h2 class="text-lg font-semibold text-gray-900 mb-4">
                <i class="fas fa-bed mr-2 text-cyan-600"></i>Room Information
            </h2>
            <div class="space-y-3">
                <div>
                    <span class="text-sm text-gray-500">Room Number</span>
                    <p class="font-medium text-gray-900">Room ${reservation.room.roomNumber}</p>
                </div>
                <div>
                    <span class="text-sm text-gray-500">Room Type</span>
                    <p class="font-medium text-gray-900">${reservation.room.roomType.typeName}</p>
                </div>
                <div>
                    <span class="text-sm text-gray-500">Floor</span>
                    <p class="font-medium text-gray-900">Floor ${reservation.room.floorNumber}</p>
                </div>
                <div>
                    <span class="text-sm text-gray-500">Rate per Night</span>
                    <p class="font-medium text-gray-900">
                        Rs. <fmt:formatNumber value="${reservation.room.roomType.ratePerNight}" pattern="#,##0.00"/>
                    </p>
                </div>
            </div>
        </div>

        <!-- Stay Details -->
        <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
            <h2 class="text-lg font-semibold text-gray-900 mb-4">
                <i class="fas fa-calendar-alt mr-2 text-cyan-600"></i>Stay Details
            </h2>
            <div class="space-y-3">
                <div class="flex justify-between">
                    <div>
                        <span class="text-sm text-gray-500">Check-in</span>
                        <p class="font-medium text-gray-900">${reservation.checkInDate}</p>
                    </div>
                    <div class="text-right">
                        <span class="text-sm text-gray-500">Check-out</span>
                        <p class="font-medium text-gray-900">${reservation.checkOutDate}</p>
                    </div>
                </div>
                <div>
                    <span class="text-sm text-gray-500">Duration</span>
                    <p class="font-medium text-gray-900">${reservation.numberOfNights} night(s)</p>
                </div>
                <div>
                    <span class="text-sm text-gray-500">Number of Guests</span>
                    <p class="font-medium text-gray-900">${reservation.numberOfGuests} guest(s)</p>
                </div>
                <c:if test="${not empty reservation.specialRequests}">
                    <div>
                        <span class="text-sm text-gray-500">Special Requests</span>
                        <p class="font-medium text-gray-900">${reservation.specialRequests}</p>
                    </div>
                </c:if>
            </div>
        </div>

        <!-- Payment Summary -->
        <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
            <h2 class="text-lg font-semibold text-gray-900 mb-4">
                <i class="fas fa-money-bill-wave mr-2 text-cyan-600"></i>Payment Summary
            </h2>
            <div class="space-y-3">
                <div class="flex justify-between">
                    <span class="text-gray-600">Total Amount</span>
                    <span class="font-medium text-gray-900">
                        Rs. <fmt:formatNumber value="${reservation.totalAmount}" pattern="#,##0.00"/>
                    </span>
                </div>
                <div class="flex justify-between">
                    <span class="text-gray-600">Advance Payment</span>
                    <span class="font-medium text-green-600">
                        Rs. <fmt:formatNumber value="${reservation.advancePayment}" pattern="#,##0.00"/>
                    </span>
                </div>
                <div class="border-t pt-3 mt-3">
                    <div class="flex justify-between">
                        <span class="font-semibold text-gray-900">Balance Due</span>
                        <span class="font-bold text-lg text-cyan-600">
                            Rs. <fmt:formatNumber value="${reservation.balanceAmount}" pattern="#,##0.00"/>
                        </span>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Action Buttons -->
    <div class="mt-6 bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
        <div class="flex flex-wrap gap-4 justify-center">
            <c:if test="${reservation.status == 'confirmed'}">
                <a href="reservation?action=checkin&id=${reservation.reservationId}"
                    class="px-6 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors">
                    <i class="fas fa-sign-in-alt mr-2"></i>Check In Guest
                </a>
                <a href="reservation?action=edit&id=${reservation.reservationId}"
                    class="px-6 py-2 bg-yellow-600 text-white rounded-lg hover:bg-yellow-700 transition-colors">
                    <i class="fas fa-edit mr-2"></i>Edit Reservation
                </a>
                <form action="reservation" method="post" style="display:inline;">
                    <input type="hidden" name="action" value="cancel">
                    <input type="hidden" name="reservationId" value="${reservation.reservationId}">
                    <button type="submit" onclick="return confirm('Are you sure you want to cancel this reservation?');"
                        class="px-6 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors">
                        <i class="fas fa-times mr-2"></i>Cancel Reservation
                    </button>
                </form>
            </c:if>

            <c:if test="${reservation.status == 'checked_in'}">
                <a href="reservation?action=checkout&id=${reservation.reservationId}"
                    class="px-6 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors">
                    <i class="fas fa-sign-out-alt mr-2"></i>Check Out & Generate Bill
                </a>
            </c:if>

            <c:if test="${reservation.status == 'checked_out' && bill != null}">
                <a href="reservation?action=bill&id=${reservation.reservationId}"
                    class="px-6 py-2 bg-purple-600 text-white rounded-lg hover:bg-purple-700 transition-colors">
                    <i class="fas fa-file-invoice-dollar mr-2"></i>View Bill
                </a>
                <a href="reservation?action=printBill&id=${reservation.reservationId}" target="_blank"
                    class="px-6 py-2 bg-gray-600 text-white rounded-lg hover:bg-gray-700 transition-colors">
                    <i class="fas fa-print mr-2"></i>Print Bill
                </a>
            </c:if>

            <a href="reservation?action=list"
                class="px-6 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors">
                <i class="fas fa-list mr-2"></i>Back to List
            </a>
        </div>
    </div>

    <!-- Reservation Meta -->
    <div class="mt-4 text-center text-sm text-gray-500">
        <p>Created on ${reservation.createdAt} by ${reservation.createdByUsername}</p>
    </div>
</div>

<footer class="border-t border-gray-200 bg-white mt-auto px-8 py-4">
    <p class="text-center text-xs text-gray-400">&copy; 2026 Ocean View Resort, Galle, Sri Lanka. All rights reserved.</p>
</footer>
    </div>
</div>
</body>
</html>