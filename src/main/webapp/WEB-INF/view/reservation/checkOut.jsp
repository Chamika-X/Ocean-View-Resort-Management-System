<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ include file="../shared/layout.jsp"%>
<div class="px-8 py-6 max-w-3xl">
    <div class="mb-6">
        <a href="reservation?action=view&id=${reservation.reservationId}" class="text-cyan-600 hover:text-cyan-700 mb-2 inline-block">
            <i class="fas fa-arrow-left mr-2"></i>Back to Reservation
        </a>
        <h1 class="text-2xl font-bold text-gray-900">Check-out & Generate Bill</h1>
        <p class="text-cyan-600 font-mono">${reservation.reservationNumber}</p>
    </div>

    <!-- Guest & Stay Summary -->
    <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 mb-6">
        <div class="grid grid-cols-2 gap-6">
            <div>
                <h3 class="text-sm font-medium text-gray-500 mb-2">GUEST</h3>
                <p class="font-semibold text-gray-900">${reservation.guest.guestName}</p>
                <p class="text-gray-600">${reservation.guest.contactNumber}</p>
            </div>
            <div>
                <h3 class="text-sm font-medium text-gray-500 mb-2">STAY</h3>
                <p class="text-gray-600">Room ${reservation.room.roomNumber} (${reservation.room.roomType.typeName})</p>
                <p class="text-gray-600">${reservation.checkInDate} to ${reservation.checkOutDate}</p>
                <p class="text-gray-600">${reservation.numberOfNights} night(s)</p>
            </div>
        </div>
    </div>

    <!-- Check-out Form -->
    <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
        <form action="reservation" method="post">
            <input type="hidden" name="action" value="checkout">
            <input type="hidden" name="reservationId" value="${reservation.reservationId}">

            <div class="space-y-6">
                <!-- Bill Preview -->
                <div class="bg-gray-50 rounded-lg p-4">
                    <h3 class="font-semibold text-gray-900 mb-3">Bill Summary</h3>
                    <div class="space-y-2 text-sm">
                        <div class="flex justify-between">
                            <span class="text-gray-600">Room Charges (${reservation.numberOfNights} nights)</span>
                            <span class="font-medium">Rs. <fmt:formatNumber value="${billPreview.roomCharges}" pattern="#,##0.00"/></span>
                        </div>
                    </div>
                </div>

                <!-- Additional Charges -->
                <div>
                    <label for="additionalCharges" class="block text-sm font-medium text-gray-700 mb-2">
                        Additional Charges (Rs.)
                    </label>
                    <input type="number" id="additionalCharges" name="additionalCharges" value="0" min="0" step="100"
                        class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-cyan-400 focus:border-cyan-400"
                        placeholder="Minibar, room service, etc.">
                    <p class="mt-1 text-sm text-gray-500">Enter any additional charges for minibar, room service, phone calls, etc.</p>
                </div>

                <!-- Discount -->
                <div>
                    <label for="discountAmount" class="block text-sm font-medium text-gray-700 mb-2">
                        Discount (Rs.)
                    </label>
                    <input type="number" id="discountAmount" name="discountAmount" value="0" min="0" step="100"
                        class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-cyan-400 focus:border-cyan-400"
                        placeholder="Enter discount amount">
                </div>

                <!-- Advance Payment Info -->
                <c:if test="${reservation.advancePayment > 0}">
                    <div class="bg-emerald-50 border border-emerald-200 rounded-xl p-4">
                        <p class="text-green-800">
                            <i class="fas fa-info-circle mr-2"></i>
                            Advance payment of <strong>Rs. <fmt:formatNumber value="${reservation.advancePayment}" pattern="#,##0.00"/></strong> 
                            has been received. This will be deducted from the final bill.
                        </p>
                    </div>
                </c:if>

                <!-- Submit Buttons -->
                <div class="flex justify-end space-x-4 pt-4">
                    <a href="reservation?action=view&id=${reservation.reservationId}"
                        class="px-6 py-2 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 transition-colors">
                        Cancel
                    </a>
                    <button type="submit"
                        class="px-6 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors">
                        <i class="fas fa-sign-out-alt mr-2"></i>Process Check-out & Generate Bill
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