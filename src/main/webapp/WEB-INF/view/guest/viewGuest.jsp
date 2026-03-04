<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ include file="../shared/layout.jsp"%>
<div class="px-8 py-6 max-w-3xl">
    <div class="mb-6">
        <a href="guest?action=list" class="text-cyan-600 hover:text-cyan-700 mb-2 inline-block">
            <i class="fas fa-arrow-left mr-2"></i>Back to Guests
        </a>
        <h1 class="text-2xl font-bold text-gray-900">Guest Profile</h1>
    </div>

    <div class="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
        <!-- Header -->
        <div class="bg-gradient-to-r from-cyan-500 to-blue-600 p-6">
            <div class="flex items-center">
                <div class="w-16 h-16 bg-white/20 rounded-full flex items-center justify-center">
                    <i class="fas fa-user text-white text-2xl"></i>
                </div>
                <div class="ml-4 text-white">
                    <h2 class="text-2xl font-bold">${guest.guestName}</h2>
                    <p class="opacity-80">${guest.nationality}</p>
                </div>
            </div>
        </div>

        <!-- Details -->
        <div class="p-6">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                    <h3 class="text-sm font-medium text-gray-500 mb-1">Contact Number</h3>
                    <p class="text-gray-900 font-medium">
                        <i class="fas fa-phone mr-2 text-cyan-600"></i>${guest.contactNumber}
                    </p>
                </div>
                <div>
                    <h3 class="text-sm font-medium text-gray-500 mb-1">Email</h3>
                    <p class="text-gray-900 font-medium">
                        <i class="fas fa-envelope mr-2 text-cyan-600"></i>${guest.email != null ? guest.email : 'N/A'}
                    </p>
                </div>
                <div class="md:col-span-2">
                    <h3 class="text-sm font-medium text-gray-500 mb-1">Address</h3>
                    <p class="text-gray-900 font-medium">
                        <i class="fas fa-map-marker-alt mr-2 text-cyan-600"></i>${guest.address}
                    </p>
                </div>
                <div>
                    <h3 class="text-sm font-medium text-gray-500 mb-1">NIC / Passport</h3>
                    <p class="text-gray-900 font-medium">
                        <i class="fas fa-id-card mr-2 text-cyan-600"></i>${guest.nicPassport != null ? guest.nicPassport : 'N/A'}
                    </p>
                </div>
                <div>
                    <h3 class="text-sm font-medium text-gray-500 mb-1">Registered On</h3>
                    <p class="text-gray-900 font-medium">
                        <i class="fas fa-calendar mr-2 text-cyan-600"></i>${guest.createdAt}
                    </p>
                </div>
            </div>
        </div>

        <!-- Actions -->
        <div class="bg-gray-50 px-6 py-4 border-t border-gray-200">
            <div class="flex justify-between">
                <a href="reservation?action=add&guestId=${guest.guestId}"
                    class="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors">
                    <i class="fas fa-calendar-plus mr-2"></i>New Reservation
                </a>
                <div class="space-x-2">
                    <a href="guest?action=edit&id=${guest.guestId}"
                        class="px-4 py-2 bg-yellow-600 text-white rounded-lg hover:bg-yellow-700 transition-colors">
                        <i class="fas fa-edit mr-2"></i>Edit
                    </a>
                    <a href="guest?action=delete&id=${guest.guestId}"
                        onclick="return confirm('Are you sure you want to delete this guest?');"
                        class="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors">
                        <i class="fas fa-trash mr-2"></i>Delete
                    </a>
                </div>
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