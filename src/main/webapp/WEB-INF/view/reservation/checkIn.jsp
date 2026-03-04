<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ include file="../shared/layout.jsp"%>
<div class="px-8 py-6 max-w-3xl">
    <div class="mb-6">
        <a href="reservation?action=view&id=${reservation.reservationId}" class="text-cyan-600 hover:text-cyan-700 mb-2 inline-block">
            <i class="fas fa-arrow-left mr-2"></i>Back to Reservation
        </a>
        <h1 class="text-2xl font-bold text-gray-900">Guest Check-in</h1>
        <p class="text-cyan-600 font-mono">${reservation.reservationNumber}</p>
    </div>

    <!-- Reservation Summary -->
    <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 mb-6">
        <h2 class="text-lg font-semibold text-gray-900 mb-4">Reservation Details</h2>
        <div class="grid grid-cols-2 gap-6">
            <div>
                <h3 class="text-sm font-medium text-gray-500 mb-2">GUEST</h3>
                <p class="font-semibold text-gray-900">${reservation.guest.guestName}</p>
                <p class="text-gray-600">${reservation.guest.contactNumber}</p>
                <p class="text-gray-600">${reservation.guest.address}</p>
            </div>
            <div>
                <h3 class="text-sm font-medium text-gray-500 mb-2">ROOM</h3>
                <p class="font-semibold text-gray-900">Room ${reservation.room.roomNumber}</p>
                <p class="text-gray-600">${reservation.room.roomType.typeName}</p>
                <p class="text-gray-600">Floor ${reservation.room.floorNumber}</p>
            </div>
        </div>
        <div class="grid grid-cols-3 gap-6 mt-6 pt-6 border-t border-gray-200">
            <div>
                <p class="text-sm text-gray-500">Check-in</p>
                <p class="font-semibold text-gray-900">${reservation.checkInDate}</p>
            </div>
            <div>
                <p class="text-sm text-gray-500">Check-out</p>
                <p class="font-semibold text-gray-900">${reservation.checkOutDate}</p>
            </div>
            <div>
                <p class="text-sm text-gray-500">Duration</p>
                <p class="font-semibold text-gray-900">${reservation.numberOfNights} night(s)</p>
            </div>
        </div>
    </div>

    <!-- Payment Status -->
    <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 mb-6">
        <h2 class="text-lg font-semibold text-gray-900 mb-4">Payment Status</h2>
        <div class="flex justify-between items-center">
            <div>
                <p class="text-gray-600">Total Amount</p>
                <p class="text-2xl font-bold text-gray-900">
                    Rs. <fmt:formatNumber value="${reservation.totalAmount}" pattern="#,##0.00"/>
                </p>
            </div>
            <div class="text-right">
                <p class="text-gray-600">Advance Paid</p>
                <p class="text-xl font-semibold text-green-600">
                    Rs. <fmt:formatNumber value="${reservation.advancePayment}" pattern="#,##0.00"/>
                </p>
            </div>
            <div class="text-right">
                <p class="text-gray-600">Balance</p>
                <p class="text-xl font-semibold text-cyan-600">
                    Rs. <fmt:formatNumber value="${reservation.balanceAmount}" pattern="#,##0.00"/>
                </p>
            </div>
        </div>
    </div>

    <!-- Check-in Confirmation -->
    <div class="bg-green-50 border border-green-200 rounded-xl p-6">
        <div class="flex items-start">
            <div class="flex-shrink-0">
                <i class="fas fa-info-circle text-green-600 text-2xl"></i>
            </div>
            <div class="ml-4">
                <h3 class="text-lg font-semibold text-green-800">Ready for Check-in</h3>
                <p class="text-green-700 mt-1">
                    Please verify the guest's identity and confirm the reservation details before proceeding with check-in.
                </p>
                <ul class="mt-3 text-sm text-green-700 space-y-1">
                    <li><i class="fas fa-check mr-2"></i>Verify guest ID (NIC/Passport)</li>
                    <li><i class="fas fa-check mr-2"></i>Confirm number of guests</li>
                    <li><i class="fas fa-check mr-2"></i>Provide room key/card</li>
                    <li><i class="fas fa-check mr-2"></i>Explain hotel amenities and policies</li>
                </ul>
            </div>
        </div>
    </div>

    <!-- Action Buttons -->
    <div class="mt-6 flex justify-end space-x-4">
        <a href="reservation?action=view&id=${reservation.reservationId}"
            class="px-6 py-2 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 transition-colors">
            Cancel
        </a>
        <form action="reservation" method="post" style="display:inline;">
            <input type="hidden" name="action" value="checkin">
            <input type="hidden" name="reservationId" value="${reservation.reservationId}">
            <button type="submit"
                class="px-6 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors">
                <i class="fas fa-sign-in-alt mr-2"></i>Confirm Check-in
            </button>
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