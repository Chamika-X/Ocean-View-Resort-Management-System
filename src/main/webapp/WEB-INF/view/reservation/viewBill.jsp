<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ include file="../shared/layout.jsp"%>
<div class="px-8 py-6 max-w-4xl">
    <!-- Page Header -->
    <div class="mb-6">
        <a href="reservation?action=view&id=${reservation.reservationId}" class="text-cyan-600 hover:text-cyan-700 mb-2 inline-block">
            <i class="fas fa-arrow-left mr-2"></i>Back to Reservation
        </a>
        <h1 class="text-2xl font-bold text-gray-900">Bill / Invoice</h1>
        <p class="text-cyan-600 font-mono text-lg">${bill.billNumber}</p>
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

    <!-- Bill Card -->
    <div class="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
        <!-- Header -->
        <div class="bg-gradient-to-r from-cyan-500 to-blue-600 text-white p-6">
            <div class="flex justify-between items-start">
                <div>
                    <div class="flex items-center mb-2">
                        <div class="w-12 h-12 bg-white/20 rounded-lg flex items-center justify-center mr-3">
                            <i class="fas fa-umbrella-beach text-2xl"></i>
                        </div>
                        <div>
                            <h2 class="text-2xl font-bold">Ocean View Resort</h2>
                            <p class="text-sm opacity-80">Galle, Sri Lanka</p>
                        </div>
                    </div>
                </div>
                <div class="text-right">
                    <p class="text-sm opacity-80">Bill Number</p>
                    <p class="text-xl font-bold">${bill.billNumber}</p>
                    <p class="text-sm opacity-80 mt-2">Date: ${bill.billDate}</p>
                </div>
            </div>
        </div>

        <!-- Guest & Stay Info -->
        <div class="p-6 border-b border-gray-200">
            <div class="grid grid-cols-2 gap-6">
                <div>
                    <h3 class="text-sm font-medium text-gray-500 mb-2">BILLED TO</h3>
                    <p class="font-semibold text-gray-900">${reservation.guest.guestName}</p>
                    <p class="text-gray-600">${reservation.guest.address}</p>
                    <p class="text-gray-600">${reservation.guest.contactNumber}</p>
                </div>
                <div>
                    <h3 class="text-sm font-medium text-gray-500 mb-2">RESERVATION DETAILS</h3>
                    <p class="text-gray-600">Reservation: <span class="font-medium">${reservation.reservationNumber}</span></p>
                    <p class="text-gray-600">Room: <span class="font-medium">${reservation.room.roomNumber} (${reservation.room.roomType.typeName})</span></p>
                    <p class="text-gray-600">Check-in: <span class="font-medium">${reservation.checkInDate}</span></p>
                    <p class="text-gray-600">Check-out: <span class="font-medium">${reservation.checkOutDate}</span></p>
                </div>
            </div>
        </div>

        <!-- Charges Breakdown -->
        <div class="p-6">
            <table class="w-full">
                <thead>
                    <tr class="border-b border-gray-200">
                        <th class="text-left py-3 text-sm font-medium text-gray-500">Description</th>
                        <th class="text-right py-3 text-sm font-medium text-gray-500">Amount</th>
                    </tr>
                </thead>
                <tbody>
                    <tr class="border-b border-gray-100">
                        <td class="py-4">
                            <p class="font-medium text-gray-900">Room Charges</p>
                            <p class="text-sm text-gray-500">
                                ${reservation.room.roomType.typeName} × ${reservation.numberOfNights} night(s) @ 
                                Rs. <fmt:formatNumber value="${reservation.room.roomType.ratePerNight}" pattern="#,##0.00"/>
                            </p>
                        </td>
                        <td class="py-4 text-right font-medium text-gray-900">
                            Rs. <fmt:formatNumber value="${bill.roomCharges}" pattern="#,##0.00"/>
                        </td>
                    </tr>
                    <c:if test="${bill.additionalCharges > 0}">
                        <tr class="border-b border-gray-100">
                            <td class="py-4">
                                <p class="font-medium text-gray-900">Additional Charges</p>
                                <p class="text-sm text-gray-500">Minibar, Room Service, etc.</p>
                            </td>
                            <td class="py-4 text-right font-medium text-gray-900">
                                Rs. <fmt:formatNumber value="${bill.additionalCharges}" pattern="#,##0.00"/>
                            </td>
                        </tr>
                    </c:if>
                    <tr class="border-b border-gray-100">
                        <td class="py-4">
                            <p class="font-medium text-gray-900">Service Charge (10%)</p>
                        </td>
                        <td class="py-4 text-right font-medium text-gray-900">
                            Rs. <fmt:formatNumber value="${bill.taxAmount}" pattern="#,##0.00"/>
                        </td>
                    </tr>
                    <c:if test="${bill.discountAmount > 0}">
                        <tr class="border-b border-gray-100">
                            <td class="py-4">
                                <p class="font-medium text-green-600">Discount</p>
                            </td>
                            <td class="py-4 text-right font-medium text-green-600">
                                - Rs. <fmt:formatNumber value="${bill.discountAmount}" pattern="#,##0.00"/>
                            </td>
                        </tr>
                    </c:if>
                </tbody>
                <tfoot>
                    <tr class="bg-gray-50">
                        <td class="py-4 px-4 text-lg font-bold text-gray-900">Total Amount</td>
                        <td class="py-4 px-4 text-right text-xl font-bold text-cyan-600">
                            Rs. <fmt:formatNumber value="${bill.totalAmount}" pattern="#,##0.00"/>
                        </td>
                    </tr>
                    <c:if test="${reservation.advancePayment > 0}">
                        <tr>
                            <td class="py-2 px-4 text-gray-600">Advance Payment</td>
                            <td class="py-2 px-4 text-right text-green-600">
                                - Rs. <fmt:formatNumber value="${reservation.advancePayment}" pattern="#,##0.00"/>
                            </td>
                        </tr>
                        <tr class="bg-cyan-50">
                            <td class="py-4 px-4 text-lg font-bold text-cyan-800">Balance Due</td>
                            <td class="py-4 px-4 text-right text-xl font-bold text-cyan-800">
                                Rs. <fmt:formatNumber value="${bill.totalAmount - reservation.advancePayment}" pattern="#,##0.00"/>
                            </td>
                        </tr>
                    </c:if>
                </tfoot>
            </table>
        </div>

        <!-- Payment Status & Actions -->
        <div class="bg-gray-50 p-6 border-t border-gray-200">
            <div class="flex justify-between items-center">
                <div>
                    <span class="text-sm text-gray-500">Payment Status:</span>
                    <span class="ml-2 px-3 py-1 text-sm font-medium rounded-full ${bill.paymentStatusBadgeClass}">
                        ${bill.paymentStatusDisplayText}
                    </span>
                    <c:if test="${bill.paymentMethod != null}">
                        <span class="ml-2 text-sm text-gray-500">via ${bill.paymentMethod}</span>
                    </c:if>
                </div>
                <div class="flex space-x-3">
                    <c:if test="${bill.paymentStatus == 'pending'}">
                        <form action="reservation" method="post" style="display:inline;">
                            <input type="hidden" name="action" value="processPayment">
                            <input type="hidden" name="billId" value="${bill.billId}">
                            <select name="paymentMethod" required class="px-3 py-2 border border-gray-300 rounded-lg text-sm">
                                <option value="">Payment Method</option>
                                <option value="cash">Cash</option>
                                <option value="card">Card</option>
                                <option value="bank_transfer">Bank Transfer</option>
                            </select>
                            <button type="submit"
                                class="ml-2 px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 text-sm">
                                <i class="fas fa-check mr-1"></i>Mark as Paid
                            </button>
                        </form>
                    </c:if>
                    <a href="reservation?action=printBill&id=${reservation.reservationId}" target="_blank"
                        class="px-4 py-2 bg-gray-600 text-white rounded-lg hover:bg-gray-700 text-sm">
                        <i class="fas fa-print mr-1"></i>Print Bill
                    </a>
                </div>
            </div>
        </div>
    </div>

    <!-- Footer Note -->
    <div class="mt-6 text-center text-sm text-gray-500">
        <p>Thank you for staying with us at Ocean View Resort!</p>
        <p class="mt-1">For any queries, please contact: +94 91 234 5678 | info@oceanviewresort.lk</p>
    </div>
</div>

<footer class="border-t border-gray-200 bg-white mt-auto px-8 py-4">
    <p class="text-center text-xs text-gray-400">&copy; 2026 Ocean View Resort, Galle, Sri Lanka. All rights reserved.</p>
</footer>
    </div>
</div>
</body>
</html>