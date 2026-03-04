<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ include file="../shared/layout.jsp"%>
<div class="px-8 py-6 max-w-3xl">
    <!-- Page Header -->
    <div class="mb-6">
        <a href="guest?action=list" class="text-cyan-600 hover:text-cyan-700 mb-2 inline-block">
            <i class="fas fa-arrow-left mr-2"></i>Back to Guests
        </a>
        <h1 class="text-2xl font-bold text-gray-900">Register New Guest</h1>
        <p class="text-gray-600">Enter guest details for registration</p>
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

    <!-- Registration Form -->
    <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
        <form action="guest" method="post">
            <input type="hidden" name="action" value="add">

            <div class="space-y-6">
                <!-- Guest Name -->
                <div>
                    <label for="guestName" class="block text-sm font-medium text-gray-700 mb-2">
                        Guest Name <span class="text-red-500">*</span>
                    </label>
                    <input type="text" id="guestName" name="guestName" value="${guestName}"
                        class="w-full px-4 py-2 border ${fieldErrors.guestName != null ? 'border-red-500' : 'border-gray-300'} rounded-lg focus:ring-2 focus:ring-cyan-400 focus:border-cyan-400"
                        placeholder="Enter full name" required>
                    <c:if test="${fieldErrors.guestName != null}">
                        <p class="mt-1 text-sm text-red-500">${fieldErrors.guestName}</p>
                    </c:if>
                </div>

                <!-- Address -->
                <div>
                    <label for="address" class="block text-sm font-medium text-gray-700 mb-2">
                        Address <span class="text-red-500">*</span>
                    </label>
                    <textarea id="address" name="address" rows="3"
                        class="w-full px-4 py-2 border ${fieldErrors.address != null ? 'border-red-500' : 'border-gray-300'} rounded-lg focus:ring-2 focus:ring-cyan-400 focus:border-cyan-400"
                        placeholder="Enter full address" required>${address}</textarea>
                    <c:if test="${fieldErrors.address != null}">
                        <p class="mt-1 text-sm text-red-500">${fieldErrors.address}</p>
                    </c:if>
                </div>

                <!-- Contact Number -->
                <div>
                    <label for="contactNumber" class="block text-sm font-medium text-gray-700 mb-2">
                        Contact Number <span class="text-red-500">*</span>
                    </label>
                    <input type="tel" id="contactNumber" name="contactNumber" value="${contactNumber}"
                        class="w-full px-4 py-2 border ${fieldErrors.contactNumber != null ? 'border-red-500' : 'border-gray-300'} rounded-lg focus:ring-2 focus:ring-cyan-400 focus:border-cyan-400"
                        placeholder="e.g., 0771234567" required>
                    <c:if test="${fieldErrors.contactNumber != null}">
                        <p class="mt-1 text-sm text-red-500">${fieldErrors.contactNumber}</p>
                    </c:if>
                </div>

                <!-- Email -->
                <div>
                    <label for="email" class="block text-sm font-medium text-gray-700 mb-2">
                        Email Address
                    </label>
                    <input type="email" id="email" name="email" value="${email}"
                        class="w-full px-4 py-2 border ${fieldErrors.email != null ? 'border-red-500' : 'border-gray-300'} rounded-lg focus:ring-2 focus:ring-cyan-400 focus:border-cyan-400"
                        placeholder="guest@example.com">
                    <c:if test="${fieldErrors.email != null}">
                        <p class="mt-1 text-sm text-red-500">${fieldErrors.email}</p>
                    </c:if>
                </div>

                <!-- NIC/Passport -->
                <div>
                    <label for="nicPassport" class="block text-sm font-medium text-gray-700 mb-2">
                        NIC / Passport Number
                    </label>
                    <input type="text" id="nicPassport" name="nicPassport" value="${nicPassport}"
                        class="w-full px-4 py-2 border ${fieldErrors.nicPassport != null ? 'border-red-500' : 'border-gray-300'} rounded-lg focus:ring-2 focus:ring-cyan-400 focus:border-cyan-400"
                        placeholder="e.g., 901234567V or PA1234567">
                    <c:if test="${fieldErrors.nicPassport != null}">
                        <p class="mt-1 text-sm text-red-500">${fieldErrors.nicPassport}</p>
                    </c:if>
                </div>

                <!-- Nationality -->
                <div>
                    <label for="nationality" class="block text-sm font-medium text-gray-700 mb-2">
                        Nationality
                    </label>
                    <select id="nationality" name="nationality"
                        class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-cyan-400 focus:border-cyan-400">
                        <option value="Sri Lankan" ${nationality == 'Sri Lankan' || empty nationality ? 'selected' : ''}>Sri Lankan</option>
                        <option value="Indian" ${nationality == 'Indian' ? 'selected' : ''}>Indian</option>
                        <option value="British" ${nationality == 'British' ? 'selected' : ''}>British</option>
                        <option value="American" ${nationality == 'American' ? 'selected' : ''}>American</option>
                        <option value="Australian" ${nationality == 'Australian' ? 'selected' : ''}>Australian</option>
                        <option value="German" ${nationality == 'German' ? 'selected' : ''}>German</option>
                        <option value="French" ${nationality == 'French' ? 'selected' : ''}>French</option>
                        <option value="Chinese" ${nationality == 'Chinese' ? 'selected' : ''}>Chinese</option>
                        <option value="Japanese" ${nationality == 'Japanese' ? 'selected' : ''}>Japanese</option>
                        <option value="Other" ${nationality == 'Other' ? 'selected' : ''}>Other</option>
                    </select>
                </div>

                <!-- Submit Button -->
                <div class="flex justify-end space-x-4 pt-4">
                    <a href="guest?action=list"
                        class="px-6 py-2 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 transition-colors">
                        Cancel
                    </a>
                    <button type="submit"
                        class="px-6 py-2.5 bg-gradient-to-r from-cyan-500 to-blue-600 text-white rounded-xl hover:from-cyan-600 hover:to-blue-700 shadow-lg shadow-cyan-500/25 transition-colors">
                        <i class="fas fa-user-plus mr-2"></i>Register Guest
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