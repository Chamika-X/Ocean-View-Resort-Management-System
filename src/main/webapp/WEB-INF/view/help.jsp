<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="shared/layout.jsp"%>
<div class="px-8 py-6 max-w-4xl">
    <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900 mb-2">Help & User Guide</h1>
        <p class="text-gray-600">Learn how to use the Ocean View Resort Reservation System</p>
    </div>

    <div class="space-y-6">
        <!-- Getting Started -->
        <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
            <h2 class="text-xl font-semibold text-gray-900 mb-4">
                <i class="fas fa-play-circle mr-2 text-cyan-600"></i>Getting Started
            </h2>
            <div class="prose text-gray-600">
                <p>Welcome to the Ocean View Resort Reservation System. This system helps you manage:</p>
                <ul class="list-disc list-inside mt-2 space-y-1">
                    <li>Guest registrations and profiles</li>
                    <li>Room availability and management</li>
                    <li>Room reservations and bookings</li>
                    <li>Check-in and check-out processes</li>
                    <li>Bill generation and payments</li>
                </ul>
            </div>
        </div>

        <!-- How to Make a Reservation -->
        <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
            <h2 class="text-xl font-semibold text-gray-900 mb-4">
                <i class="fas fa-calendar-plus mr-2 text-green-600"></i>How to Make a New Reservation
            </h2>
            <div class="space-y-4">
                <div class="flex items-start">
                    <span class="flex-shrink-0 w-8 h-8 bg-cyan-100 text-cyan-600 rounded-full flex items-center justify-center font-bold mr-3">1</span>
                    <div>
                        <p class="font-medium text-gray-900">Register the Guest (if new)</p>
                        <p class="text-sm text-gray-600">Go to Guests → Register New Guest. Enter guest name, address, contact number, and other details.</p>
                    </div>
                </div>
                <div class="flex items-start">
                    <span class="flex-shrink-0 w-8 h-8 bg-cyan-100 text-cyan-600 rounded-full flex items-center justify-center font-bold mr-3">2</span>
                    <div>
                        <p class="font-medium text-gray-900">Create Reservation</p>
                        <p class="text-sm text-gray-600">Go to Reservations → New Reservation. Select the guest, choose an available room, and set check-in/check-out dates.</p>
                    </div>
                </div>
                <div class="flex items-start">
                    <span class="flex-shrink-0 w-8 h-8 bg-cyan-100 text-cyan-600 rounded-full flex items-center justify-center font-bold mr-3">3</span>
                    <div>
                        <p class="font-medium text-gray-900">Collect Advance (Optional)</p>
                        <p class="text-sm text-gray-600">Enter any advance payment amount. The system will calculate the balance automatically.</p>
                    </div>
                </div>
                <div class="flex items-start">
                    <span class="flex-shrink-0 w-8 h-8 bg-cyan-100 text-cyan-600 rounded-full flex items-center justify-center font-bold mr-3">4</span>
                    <div>
                        <p class="font-medium text-gray-900">Confirm Reservation</p>
                        <p class="text-sm text-gray-600">Review the details and click "Create Reservation". The reservation number will be generated automatically.</p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Check-in Process -->
        <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
            <h2 class="text-xl font-semibold text-gray-900 mb-4">
                <i class="fas fa-sign-in-alt mr-2 text-blue-600"></i>Check-in Process
            </h2>
            <div class="prose text-gray-600">
                <ol class="list-decimal list-inside space-y-2">
                    <li>Find the reservation using the search or from the dashboard's "Today's Check-ins" list</li>
                    <li>Click "View" to see reservation details</li>
                    <li>Verify guest identity and reservation details</li>
                    <li>Click "Check In Guest" to complete the check-in</li>
                    <li>The room status will automatically change to "Occupied"</li>
                </ol>
            </div>
        </div>

        <!-- Check-out and Billing -->
        <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
            <h2 class="text-xl font-semibold text-gray-900 mb-4">
                <i class="fas fa-file-invoice-dollar mr-2 text-purple-600"></i>Check-out & Bill Generation
            </h2>
            <div class="prose text-gray-600">
                <ol class="list-decimal list-inside space-y-2">
                    <li>Find the reservation (status should be "Checked In")</li>
                    <li>Click "Check Out & Generate Bill"</li>
                    <li>Add any additional charges (minibar, room service, etc.)</li>
                    <li>Apply discount if applicable</li>
                    <li>Click "Process Check-out" to generate the bill</li>
                    <li>Collect payment and mark bill as paid</li>
                    <li>Print the bill for the guest</li>
                </ol>
            </div>
        </div>

        <!-- Room Status Guide -->
        <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
            <h2 class="text-xl font-semibold text-gray-900 mb-4">
                <i class="fas fa-bed mr-2 text-cyan-600"></i>Room Status Guide
            </h2>
            <div class="grid grid-cols-2 gap-4">
                <div class="flex items-center p-3 bg-green-50 rounded-lg">
                    <div class="w-4 h-4 bg-green-500 rounded-full mr-3"></div>
                    <div>
                        <p class="font-medium text-green-800">Available</p>
                        <p class="text-sm text-green-600">Room is ready for booking</p>
                    </div>
                </div>
                <div class="flex items-center p-3 bg-red-50 rounded-lg">
                    <div class="w-4 h-4 bg-red-500 rounded-full mr-3"></div>
                    <div>
                        <p class="font-medium text-red-800">Occupied</p>
                        <p class="text-sm text-red-600">Guest currently staying</p>
                    </div>
                </div>
                <div class="flex items-center p-3 bg-yellow-50 rounded-lg">
                    <div class="w-4 h-4 bg-yellow-500 rounded-full mr-3"></div>
                    <div>
                        <p class="font-medium text-yellow-800">Reserved</p>
                        <p class="text-sm text-yellow-600">Booked for upcoming stay</p>
                    </div>
                </div>
                <div class="flex items-center p-3 bg-gray-50 rounded-lg">
                    <div class="w-4 h-4 bg-gray-500 rounded-full mr-3"></div>
                    <div>
                        <p class="font-medium text-gray-800">Maintenance</p>
                        <p class="text-sm text-gray-600">Under repair/cleaning</p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Contact Support -->
        <div class="bg-cyan-50 border border-cyan-200 rounded-xl p-6">
            <h2 class="text-xl font-semibold text-cyan-800 mb-4">
                <i class="fas fa-headset mr-2"></i>Need More Help?
            </h2>
            <p class="text-cyan-700 mb-4">If you encounter any issues or have questions, please contact:</p>
            <div class="space-y-2 text-cyan-800">
                <p><i class="fas fa-phone mr-2"></i>IT Support: +94 91 234 5678</p>
                <p><i class="fas fa-envelope mr-2"></i>Email: support@oceanviewresort.lk</p>
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