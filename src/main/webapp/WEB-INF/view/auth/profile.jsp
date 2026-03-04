<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../shared/layout.jsp"%>
<!-- Main Content -->
<div class="px-8 py-6 max-w-4xl">
	<!-- Page Header -->
	<div class="flex items-center justify-between mb-6">
		<div>
			<h1 class="text-2xl font-bold text-gray-900">User Profile</h1>
			<p class="text-sm text-gray-500 mt-1">Update your account information</p>
		</div>
		<a href="dashboard"
			class="bg-white border border-gray-200 text-gray-700 px-4 py-2.5 rounded-xl hover:bg-gray-50 transition-colors duration-200 flex items-center text-sm font-medium">
			<i class="fas fa-home mr-2"></i>Back to Dashboard
		</a>
	</div>

	<!-- Profile Card -->
	<div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-8">
		<!-- Profile Header -->
		<div class="text-center mb-8">
			<div
				class="w-16 h-16 bg-gradient-to-r from-cyan-500 to-blue-600 rounded-2xl flex items-center justify-center mx-auto mb-4 shadow-lg shadow-cyan-500/25">
				<i class="fas fa-user text-white text-2xl"></i>
			</div>
			<h2 class="text-xl font-bold text-gray-900 mb-1">Update Profile</h2>
			<p class="text-sm text-gray-500">Modify your account details</p>
		</div>

		<form action="auth?action=updateProfile" method="post"
			class="space-y-6">
			<input type="hidden" name="userId" value="${user.userId}">

			<div class="grid grid-cols-1 md:grid-cols-2 gap-6">
				<!-- Username -->
				<div>
					<label for="username"
						class="block text-sm font-medium text-gray-700 mb-1.5"> <i
						class="fas fa-user mr-2 text-cyan-600"></i>Username
					</label> <input type="text" id="username" name="username" required
						value="${username != null ? username : user.username}"
						class="w-full px-4 py-3 border ${fieldErrors.username != null ? 'border-red-300 focus:ring-red-500 focus:border-red-500' : 'border-gray-200 focus:ring-cyan-400 focus:border-cyan-400'} rounded-xl transition-colors duration-200 text-sm">
					<c:if test="${fieldErrors.username != null}">
						<p class="mt-1 text-sm text-red-600">${fieldErrors.username}</p>
					</c:if>
				</div>

				<!-- Role (Read-only) -->
				<div>
					<label class="block text-sm font-medium text-gray-700 mb-1.5">
						<i class="fas fa-user-tag mr-2 text-cyan-600"></i>Role
					</label>
					<div
						class="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl text-gray-900 capitalize text-sm">
						${user.role}</div>
				</div>

				<!-- User ID (Read-only) -->
				<div>
					<label class="block text-sm font-medium text-gray-700 mb-1.5">
						<i class="fas fa-id-card mr-2 text-cyan-600"></i>User ID
					</label>
					<div
						class="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl text-gray-900 text-sm">
						${user.userId}</div>
				</div>

				<!-- New Password (Optional) -->
				<div>
					<label for="newPassword"
						class="block text-sm font-medium text-gray-700 mb-1.5"> <i
						class="fas fa-lock mr-2 text-cyan-600"></i>New Password
						(Optional)
					</label> <input type="password" id="newPassword" name="newPassword"
						placeholder="Leave empty to keep current password"
						class="w-full px-4 py-3 border ${fieldErrors.newPassword != null ? 'border-red-300 focus:ring-red-500 focus:border-red-500' : 'border-gray-200 focus:ring-cyan-400 focus:border-cyan-400'} rounded-xl transition-colors duration-200 text-sm">
					<c:if test="${fieldErrors.newPassword != null}">
						<p class="mt-1 text-sm text-red-600">${fieldErrors.newPassword}</p>
					</c:if>
				</div>
			</div>

			<!-- Action Buttons -->
			<div
				class="flex items-center justify-center gap-4 mt-8 pt-6 border-t border-gray-100">
				<button type="submit"
					class="bg-gradient-to-r from-cyan-500 to-blue-600 text-white px-6 py-2.5 rounded-xl hover:from-cyan-600 hover:to-blue-700 transition-all duration-200 flex items-center text-sm font-semibold shadow-lg shadow-cyan-500/25">
					<i class="fas fa-save mr-2"></i>Update Profile
				</button>
				<a href="dashboard"
					class="bg-white border border-gray-200 text-gray-700 px-6 py-2.5 rounded-xl hover:bg-gray-50 transition-colors duration-200 flex items-center text-sm font-medium">
					<i class="fas fa-home mr-2"></i>Dashboard
				</a>
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