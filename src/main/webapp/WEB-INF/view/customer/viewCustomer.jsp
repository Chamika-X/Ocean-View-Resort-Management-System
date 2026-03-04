<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../shared/layout.jsp"%>
<!-- Main Content -->
<div class="px-8 py-6">
	<!-- Page Header -->
	<div class="mb-6">
		<h1 class="text-3xl font-bold text-gray-900">Customer Details</h1>
		<p class="text-gray-600 mt-1">View customer information and manage
			their account</p>
	</div>

	<!-- Alerts -->
	<c:if test="${not empty success}">
		<div class="mb-6 bg-emerald-50 border border-emerald-200 rounded-xl p-4">
			<div class="flex items-center">
				<i class="fas fa-check-circle text-green-500 mr-2"></i> <span
					class="text-green-800">${success}</span>
			</div>
		</div>
	</c:if>

	<c:if test="${not empty error}">
		<div class="mb-6 bg-red-50 border border-red-200 rounded-xl p-4">
			<div class="flex items-center">
				<i class="fas fa-exclamation-circle text-red-500 mr-2"></i> <span
					class="text-red-800">${error}</span>
			</div>
		</div>
	</c:if>

	<c:if test="${not empty customer}">
		<!-- Customer Details Card -->
		<div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
			<!-- Customer Header -->
			<div
				class="bg-gradient-to-r from-orange-600 to-orange-700 text-white rounded-lg p-6 mb-6 text-center">
				<div
					class="w-20 h-20 bg-white bg-opacity-20 rounded-full flex items-center justify-center mx-auto mb-4">
					<i class="fas fa-user text-white text-3xl"></i>
				</div>
				<h2 class="text-2xl font-semibold mb-2">${customer.name}</h2>
				<p class="text-orange-100">Account Number:
					${customer.accountNumber}</p>
			</div>

			<!-- Customer Information -->
			<div class="grid grid-cols-1 md:grid-cols-2 gap-6">
				<div>
					<label class="block text-sm font-medium text-gray-700 mb-2">
						<i class="fas fa-hashtag mr-2"></i>Customer ID
					</label>
					<div
						class="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-lg text-gray-700">
						${customer.customerId}</div>
				</div>
				<div>
					<label class="block text-sm font-medium text-gray-700 mb-2">
						<i class="fas fa-phone mr-2"></i>Telephone
					</label>
					<div
						class="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-lg text-gray-700">
						${customer.telephone}</div>
				</div>
				<div class="md:col-span-2">
					<label class="block text-sm font-medium text-gray-700 mb-2">
						<i class="fas fa-map-marker-alt mr-2"></i>Address
					</label>
					<div
						class="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-lg text-gray-700">
						${customer.address}</div>
				</div>
			</div>

			<!-- Action Buttons -->
			<div class="flex items-center gap-4 mt-8 border-t pt-6">
				<a href="customer?action=edit&id=${customer.customerId}"
					class="bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700 transition-colors duration-200 flex items-center">
					<i class="fas fa-edit mr-2"></i>Edit Customer
				</a> <a href="bill?action=create&customerId=${customer.customerId}"
					class="bg-green-600 text-white px-6 py-3 rounded-lg hover:bg-green-700 transition-colors duration-200 flex items-center">
					<i class="fas fa-file-invoice-dollar mr-2"></i>Create Bill
				</a> <a href="customer?action=delete&id=${customer.customerId}"
					onclick="return confirm('Are you sure you want to delete this customer?')"
					class="bg-red-600 text-white px-6 py-3 rounded-lg hover:bg-red-700 transition-colors duration-200 flex items-center">
					<i class="fas fa-trash mr-2"></i>Delete
				</a> <a href="customer?action=list"
					class="bg-gray-100 text-gray-700 px-6 py-3 rounded-lg hover:bg-gray-200 transition-colors duration-200 flex items-center">
					<i class="fas fa-arrow-left mr-2"></i>Back
				</a>
			</div>
		</div>
	</c:if>
</div>
<footer class="border-t border-gray-200 bg-white mt-auto px-8 py-4">
    <p class="text-center text-xs text-gray-400">&copy; 2026 Ocean View Resort, Galle, Sri Lanka. All rights reserved.</p>
</footer>
    </div>
</div>
</body>
</html>