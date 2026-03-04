<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Bill - ${bill.billNumber} - Ocean View Resort</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        @media print {
            .no-print { display: none !important; }
            body { padding: 0; margin: 0; }
        }
    </style>
</head>
<body class="bg-white">
    <!-- Print Button -->
    <div class="no-print fixed top-4 right-4 space-x-2">
        <button onclick="window.print()" class="px-4 py-2.5 bg-gradient-to-r from-cyan-500 to-blue-600 text-white rounded-xl hover:from-cyan-600 hover:to-blue-700 shadow-lg shadow-cyan-500/25">
            <i class="fas fa-print mr-2"></i>Print
        </button>
        <a href="reservation?action=bill&id=${reservation.reservationId}" class="px-4 py-2 bg-gray-600 text-white rounded-lg hover:bg-gray-700">
            Back
        </a>
    </div>

    <div class="max-w-3xl mx-auto p-8">
        <!-- Header -->
        <div class="text-center border-b-2 border-gray-800 pb-6 mb-6">
            <h1 class="text-3xl font-bold text-gray-900">Ocean View Resort</h1>
            <p class="text-gray-600">Beach Road, Unawatuna, Galle, Sri Lanka</p>
            <p class="text-gray-600">Tel: +94 91 234 5678 | Email: info@oceanviewresort.lk</p>
        </div>

        <!-- Bill Title -->
        <div class="text-center mb-6">
            <h2 class="text-2xl font-bold text-gray-800">TAX INVOICE</h2>
            <p class="text-lg font-mono text-gray-600">${bill.billNumber}</p>
        </div>

        <!-- Bill Info -->
        <div class="grid grid-cols-2 gap-8 mb-8">
            <div>
                <h3 class="font-bold text-gray-700 mb-2">BILLED TO:</h3>
                <p class="font-semibold">${reservation.guest.guestName}</p>
                <p class="text-gray-600">${reservation.guest.address}</p>
                <p class="text-gray-600">Tel: ${reservation.guest.contactNumber}</p>
                <c:if test="${reservation.guest.nicPassport != null}">
                    <p class="text-gray-600">ID: ${reservation.guest.nicPassport}</p>
                </c:if>
            </div>
            <div class="text-right">
                <h3 class="font-bold text-gray-700 mb-2">INVOICE DETAILS:</h3>
                <p class="text-gray-600">Date: ${bill.billDate}</p>
                <p class="text-gray-600">Reservation: ${reservation.reservationNumber}</p>
                <p class="text-gray-600">Room: ${reservation.room.roomNumber}</p>
            </div>
        </div>

        <!-- Stay Details -->
        <div class="bg-gray-100 rounded-lg p-4 mb-6">
            <div class="grid grid-cols-4 gap-4 text-sm">
                <div>
                    <span class="text-gray-500">Check-in</span>
                    <p class="font-medium">${reservation.checkInDate}</p>
                </div>
                <div>
                    <span class="text-gray-500">Check-out</span>
                    <p class="font-medium">${reservation.checkOutDate}</p>
                </div>
                <div>
                    <span class="text-gray-500">Room Type</span>
                    <p class="font-medium">${reservation.room.roomType.typeName}</p>
                </div>
                <div>
                    <span class="text-gray-500">Duration</span>
                    <p class="font-medium">${reservation.numberOfNights} Night(s)</p>
                </div>
            </div>
        </div>

        <!-- Charges Table -->
        <table class="w-full mb-6">
            <thead>
                <tr class="border-b-2 border-gray-800">
                    <th class="text-left py-2">Description</th>
                    <th class="text-right py-2">Amount (Rs.)</th>
                </tr>
            </thead>
            <tbody>
                <tr class="border-b border-gray-200">
                    <td class="py-3">
                        Room Charges - ${reservation.room.roomType.typeName}<br>
                        <span class="text-sm text-gray-500">
                            ${reservation.numberOfNights} night(s) @ Rs. <fmt:formatNumber value="${reservation.room.roomType.ratePerNight}" pattern="#,##0.00"/>
                        </span>
                    </td>
                    <td class="py-3 text-right"><fmt:formatNumber value="${bill.roomCharges}" pattern="#,##0.00"/></td>
                </tr>
                <c:if test="${bill.additionalCharges > 0}">
                    <tr class="border-b border-gray-200">
                        <td class="py-3">Additional Charges</td>
                        <td class="py-3 text-right"><fmt:formatNumber value="${bill.additionalCharges}" pattern="#,##0.00"/></td>
                    </tr>
                </c:if>
                <tr class="border-b border-gray-200">
                    <td class="py-3">Service Charge (10%)</td>
                    <td class="py-3 text-right"><fmt:formatNumber value="${bill.taxAmount}" pattern="#,##0.00"/></td>
                </tr>
                <c:if test="${bill.discountAmount > 0}">
                    <tr class="border-b border-gray-200">
                        <td class="py-3">Discount</td>
                        <td class="py-3 text-right text-green-600">-<fmt:formatNumber value="${bill.discountAmount}" pattern="#,##0.00"/></td>
                    </tr>
                </c:if>
            </tbody>
            <tfoot>
                <tr class="border-t-2 border-gray-800">
                    <td class="py-3 font-bold text-lg">TOTAL</td>
                    <td class="py-3 text-right font-bold text-lg">Rs. <fmt:formatNumber value="${bill.totalAmount}" pattern="#,##0.00"/></td>
                </tr>
                <c:if test="${reservation.advancePayment > 0}">
                    <tr>
                        <td class="py-2 text-gray-600">Less: Advance Payment</td>
                        <td class="py-2 text-right text-green-600">-Rs. <fmt:formatNumber value="${reservation.advancePayment}" pattern="#,##0.00"/></td>
                    </tr>
                    <tr class="border-t border-gray-300">
                        <td class="py-3 font-bold">BALANCE DUE</td>
                        <td class="py-3 text-right font-bold">Rs. <fmt:formatNumber value="${bill.totalAmount - reservation.advancePayment}" pattern="#,##0.00"/></td>
                    </tr>
                </c:if>
            </tfoot>
        </table>

        <!-- Payment Status -->
        <div class="text-center mb-8">
            <c:choose>
                <c:when test="${bill.paymentStatus == 'paid'}">
                    <span class="inline-block px-6 py-2 bg-green-100 text-green-800 font-bold text-lg rounded">
                        PAID - ${bill.paymentMethod}
                    </span>
                </c:when>
                <c:otherwise>
                    <span class="inline-block px-6 py-2 bg-yellow-100 text-yellow-800 font-bold text-lg rounded">
                        PAYMENT PENDING
                    </span>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Footer -->
        <div class="border-t-2 border-gray-800 pt-6 text-center text-sm text-gray-600">
            <p class="font-semibold mb-2">Thank you for staying with us!</p>
            <p>We hope you enjoyed your stay at Ocean View Resort.</p>
            <p>Please visit us again.</p>
            <p class="mt-4 text-xs">This is a computer-generated invoice and does not require a signature.</p>
        </div>
    </div>

    <script>
        // Auto-print on load (optional)
        // window.onload = function() { window.print(); }
    </script>
</body>
</html>
