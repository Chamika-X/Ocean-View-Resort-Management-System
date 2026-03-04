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
        <h1 class="text-2xl font-bold text-gray-900">New Reservation</h1>
        <p class="text-gray-600">Create a new room reservation</p>
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

    <!-- Reservation Form -->
    <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
        <form action="reservation" method="post" id="reservationForm">
            <input type="hidden" name="action" value="add">

            <div class="space-y-6">
                <!-- Guest Selection -->
                <div>
                    <label for="guestId" class="block text-sm font-medium text-gray-700 mb-2">
                        Select Guest <span class="text-red-500">*</span>
                    </label>
                    <div class="flex gap-2">
                        <select id="guestId" name="guestId" required
                            class="flex-1 px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-cyan-400 focus:border-cyan-400">
                            <option value="">-- Select a Guest --</option>
                            <c:forEach var="guest" items="${guests}">
                                <option value="${guest.guestId}" ${param.guestId == guest.guestId ? 'selected' : ''}>
                                    ${guest.guestName} - ${guest.contactNumber}
                                </option>
                            </c:forEach>
                        </select>
                        <a href="guest?action=add" 
                           class="px-4 py-2 bg-green-100 text-green-700 rounded-lg hover:bg-green-200 transition-colors"
                           title="Register New Guest">
                            <i class="fas fa-user-plus"></i>
                        </a>
                    </div>
                    <c:if test="${fieldErrors.guestId != null}">
                        <p class="mt-1 text-sm text-red-500">${fieldErrors.guestId}</p>
                    </c:if>
                </div>

                <!-- Room Selection -->
                <div>
                    <label for="roomId" class="block text-sm font-medium text-gray-700 mb-2">
                        Select Room <span class="text-red-500">*</span>
                    </label>
                    <select id="roomId" name="roomId" required onchange="updateRoomRate()"
                        class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-cyan-400 focus:border-cyan-400">
                        <option value="" data-rate="0">-- Select a Room --</option>
                        <c:forEach var="room" items="${availableRooms}">
                            <option value="${room.roomId}" 
                                    data-rate="${room.roomType.ratePerNight}"
                                    data-type="${room.roomType.typeName}"
                                    ${param.roomId == room.roomId ? 'selected' : ''}>
                                Room ${room.roomNumber} - ${room.roomType.typeName} 
                                (Rs. <fmt:formatNumber value="${room.roomType.ratePerNight}" pattern="#,##0"/> / night)
                            </option>
                        </c:forEach>
                    </select>
                    <c:if test="${fieldErrors.roomId != null}">
                        <p class="mt-1 text-sm text-red-500">${fieldErrors.roomId}</p>
                    </c:if>
                </div>

                <!-- Date Selection -->
                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div>
                        <label for="checkInDate" class="block text-sm font-medium text-gray-700 mb-2">
                            Check-in Date <span class="text-red-500">*</span>
                        </label>
                        <input type="date" id="checkInDate" name="checkInDate" min="${minDate}" required
                            onchange="calculateTotal()"
                            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-cyan-400 focus:border-cyan-400">
                        <c:if test="${fieldErrors.checkInDate != null}">
                            <p class="mt-1 text-sm text-red-500">${fieldErrors.checkInDate}</p>
                        </c:if>
                    </div>
                    <div>
                        <label for="checkOutDate" class="block text-sm font-medium text-gray-700 mb-2">
                            Check-out Date <span class="text-red-500">*</span>
                        </label>
                        <input type="date" id="checkOutDate" name="checkOutDate" required
                            onchange="calculateTotal()"
                            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-cyan-400 focus:border-cyan-400">
                        <c:if test="${fieldErrors.checkOutDate != null}">
                            <p class="mt-1 text-sm text-red-500">${fieldErrors.checkOutDate}</p>
                        </c:if>
                    </div>
                </div>

                <!-- Number of Guests -->
                <div>
                    <label for="numberOfGuests" class="block text-sm font-medium text-gray-700 mb-2">
                        Number of Guests <span class="text-red-500">*</span>
                    </label>
                    <input type="number" id="numberOfGuests" name="numberOfGuests" value="1" min="1" max="10" required
                        class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-cyan-400 focus:border-cyan-400">
                </div>

                <!-- Advance Payment -->
                <div>
                    <label for="advancePayment" class="block text-sm font-medium text-gray-700 mb-2">
                        Advance Payment (Rs.)
                    </label>
                    <input type="number" id="advancePayment" name="advancePayment" value="0" min="0" step="100"
                        class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-cyan-400 focus:border-cyan-400">
                </div>

                <!-- Special Requests -->
                <div>
                    <label for="specialRequests" class="block text-sm font-medium text-gray-700 mb-2">
                        Special Requests
                    </label>
                    <textarea id="specialRequests" name="specialRequests" rows="3"
                        class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-cyan-400 focus:border-cyan-400"
                        placeholder="Any special requests or notes..."></textarea>
                </div>

                <!-- Cost Summary -->
                <div class="bg-cyan-50 border border-cyan-200 rounded-lg p-4">
                    <h3 class="text-lg font-semibold text-cyan-800 mb-3">
                        <i class="fas fa-calculator mr-2"></i>Cost Summary
                    </h3>
                    <div class="space-y-2 text-sm">
                        <div class="flex justify-between">
                            <span class="text-gray-600">Room Type:</span>
                            <span id="displayRoomType" class="font-medium">-</span>
                        </div>
                        <div class="flex justify-between">
                            <span class="text-gray-600">Rate per Night:</span>
                            <span id="displayRate" class="font-medium">Rs. 0.00</span>
                        </div>
                        <div class="flex justify-between">
                            <span class="text-gray-600">Number of Nights:</span>
                            <span id="displayNights" class="font-medium">0</span>
                        </div>
                        <div class="border-t border-cyan-200 pt-2 mt-2">
                            <div class="flex justify-between text-lg">
                                <span class="font-semibold text-cyan-800">Estimated Total:</span>
                                <span id="displayTotal" class="font-bold text-cyan-800">Rs. 0.00</span>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Submit Button -->
                <div class="flex justify-end space-x-4 pt-4">
                    <a href="reservation?action=list"
                        class="px-6 py-2 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 transition-colors">
                        Cancel
                    </a>
                    <button type="submit"
                        class="px-6 py-2.5 bg-gradient-to-r from-cyan-500 to-blue-600 text-white rounded-xl hover:from-cyan-600 hover:to-blue-700 shadow-lg shadow-cyan-500/25 transition-colors">
                        <i class="fas fa-calendar-check mr-2"></i>Create Reservation
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>

<script>
    function updateRoomRate() {
        const roomSelect = document.getElementById('roomId');
        const selectedOption = roomSelect.options[roomSelect.selectedIndex];
        const rate = selectedOption.getAttribute('data-rate') || 0;
        const type = selectedOption.getAttribute('data-type') || '-';
        
        document.getElementById('displayRoomType').textContent = type;
        document.getElementById('displayRate').textContent = 'Rs. ' + parseFloat(rate).toLocaleString('en-US', {minimumFractionDigits: 2});
        
        calculateTotal();
    }

    function calculateTotal() {
        const checkIn = document.getElementById('checkInDate').value;
        const checkOut = document.getElementById('checkOutDate').value;
        const roomSelect = document.getElementById('roomId');
        const selectedOption = roomSelect.options[roomSelect.selectedIndex];
        const rate = parseFloat(selectedOption.getAttribute('data-rate')) || 0;

        if (checkIn && checkOut) {
            const checkInDate = new Date(checkIn);
            const checkOutDate = new Date(checkOut);
            const nights = Math.ceil((checkOutDate - checkInDate) / (1000 * 60 * 60 * 24));
            
            if (nights > 0) {
                document.getElementById('displayNights').textContent = nights;
                const total = nights * rate;
                document.getElementById('displayTotal').textContent = 'Rs. ' + total.toLocaleString('en-US', {minimumFractionDigits: 2});
            } else {
                document.getElementById('displayNights').textContent = '0';
                document.getElementById('displayTotal').textContent = 'Rs. 0.00';
            }
        }
    }

    // Initialize on page load
    document.addEventListener('DOMContentLoaded', function() {
        updateRoomRate();
        
        // Set min date for checkout based on check-in
        document.getElementById('checkInDate').addEventListener('change', function() {
            const checkOutInput = document.getElementById('checkOutDate');
            const nextDay = new Date(this.value);
            nextDay.setDate(nextDay.getDate() + 1);
            checkOutInput.min = nextDay.toISOString().split('T')[0];
            if (checkOutInput.value && checkOutInput.value <= this.value) {
                checkOutInput.value = nextDay.toISOString().split('T')[0];
            }
            calculateTotal();
        });
    });
</script>

<footer class="border-t border-gray-200 bg-white mt-auto px-8 py-4">
    <p class="text-center text-xs text-gray-400">&copy; 2026 Ocean View Resort, Galle, Sri Lanka. All rights reserved.</p>
</footer>
    </div>
</div>
</body>
</html>