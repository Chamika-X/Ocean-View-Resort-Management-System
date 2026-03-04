<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="../shared/layout.jsp"%>
<!-- Main Content -->
<div class="px-8 py-6">
	<!-- Page Header -->
	<div class="flex items-center justify-between mb-6">
		<div>
			<h1 class="text-3xl font-bold text-gray-900">User Details</h1>
			<p class="text-gray-600 mt-1">View user information and account
				details</p>
		</div>
		<a href="user-management?action=list"
			class="bg-gray-100 text-gray-700 px-4 py-2 rounded-lg hover:bg-gray-200 transition-colors duration-200 flex items-center">
			<i class="fas fa-list mr-2"></i>Back to Users
		</a>
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

	<!-- User Details Card -->
	<div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
		<!-- User Header -->
		<div
			class="flex flex-col md:flex-row md:items-center md:justify-between mb-6 border-b border-gray-200 pb-4">
			<div class="flex items-center mb-4 md:mb-0">
				<div
					class="w-12 h-12 bg-cyan-100 rounded-full flex items-center justify-center mr-4">
					<i class="fas fa-user text-cyan-600 text-xl"></i>
				</div>
				<div>
					<h2 class="text-2xl font-semibold text-gray-900">${user.username}</h2>
					<p class="text-gray-600">User Account</p>
				</div>
			</div>
			<span
				class="inline-flex items-center bg-gradient-to-r from-cyan-500 to-blue-600 text-white px-3 py-1 rounded-full text-sm font-medium">
				<i class="fas fa-hashtag mr-1"></i>ID: ${user.userId}
			</span>
		</div>

		<!-- User Information Grid -->
		<div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
			<div class="bg-orange-50 border border-cyan-200 rounded-lg p-4">
				<div class="flex items-center mb-2">
					<i class="fas fa-user-tag text-cyan-600 mr-2"></i>
					<div class="text-gray-600 text-sm font-medium">Role</div>
				</div>
				<div class="text-gray-900 font-semibold capitalize">${user.role}</div>
			</div>

			<div class="bg-orange-50 border border-cyan-200 rounded-lg p-4">
				<div class="flex items-center mb-2">
					<i class="fas fa-calendar text-cyan-600 mr-2"></i>
					<div class="text-gray-600 text-sm font-medium">Created At</div>
				</div>
				<div class="text-gray-900 font-semibold">
					<fmt:formatDate value="${user.createdAt}"
						pattern="dd/MM/yyyy HH:mm" />
				</div>
			</div>
		</div>

		<!-- Status Section -->
		<div class="bg-emerald-50 border border-emerald-200 rounded-xl p-4 mb-6">
			<div class="flex items-center">
				<i class="fas fa-check-circle text-green-600 mr-2"></i>
				<div class="text-gray-600 text-sm font-medium mr-2">Status</div>
				<div class="text-green-800 font-semibold">Active</div>
			</div>
		</div>

		<!-- Action Buttons -->
		<div class="flex items-center gap-3 justify-center">
			<a href="user-management?action=edit&id=${user.userId}"
				class="bg-gradient-to-r from-cyan-500 to-blue-600 text-white px-6 py-3 rounded-lg hover:from-cyan-600 hover:to-blue-700 transition-colors duration-200 flex items-center">
				<i class="fas fa-edit mr-2"></i>Edit User
			</a>

			<c:if test="${user.userId != sessionScope.userId}">
				<a href="user-management?action=delete&id=${user.userId}"
					onclick="return confirm('Are you sure you want to delete this user?')"
					class="bg-red-600 text-white px-6 py-3 rounded-lg hover:bg-red-700 transition-colors duration-200 flex items-center">
					<i class="fas fa-trash mr-2"></i>Delete
				</a>
			</c:if>

			<a href="user-management?action=list"
				class="bg-gray-100 text-gray-700 px-6 py-3 rounded-lg hover:bg-gray-200 transition-colors duration-200 flex items-center">
				<i class="fas fa-arrow-left mr-2"></i>Back
			</a>
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