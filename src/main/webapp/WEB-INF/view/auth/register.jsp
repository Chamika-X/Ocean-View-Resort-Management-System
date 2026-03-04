<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Register - Ocean View Resort</title>
<script src="https://cdn.tailwindcss.com"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
<script>
	tailwind.config = {
		theme : {
			extend : {
				fontFamily: { sans: ['Inter', 'sans-serif'] },
				colors : {
					primary : {
						50 : '#f0f9ff',
						100 : '#e0f2fe',
						200 : '#bae6fd',
						300 : '#7dd3fc',
						400 : '#38bdf8',
						500 : '#0ea5e9',
						600 : '#0284c7',
						700 : '#0369a1',
						800 : '#075985',
						900 : '#0c4a6e'
					}
				}
			}
		}
	}
</script>
<style>body { font-family: 'Inter', sans-serif; }</style>
</head>
<body class="bg-gradient-to-br from-cyan-400 via-cyan-500 to-blue-600 min-h-screen flex items-center justify-center p-4 font-sans">
	<div class="bg-white rounded-2xl shadow-2xl p-8 max-w-xl w-full mx-4">
		<div class="text-center mb-8">
			<div class="w-16 h-16 bg-gradient-to-r from-cyan-500 to-blue-600 rounded-2xl flex items-center justify-center mx-auto mb-4 shadow-lg shadow-cyan-500/25">
				<i class="fas fa-user-plus text-white text-2xl"></i>
			</div>
			<h1 class="text-2xl font-bold text-gray-900 mb-1">Create Account</h1>
			<p class="text-sm text-gray-500">Sign up to start using the system</p>
		</div>

		<c:if test="${not empty success}">
			<div class="mb-6 bg-green-50 border border-green-200 rounded-lg p-4">
				<div class="flex items-center">
					<i class="fas fa-check-circle text-green-500 mr-2"></i><span
						class="text-green-800">${success}</span>
				</div>
			</div>
		</c:if>
		<c:if test="${not empty error}">
			<div class="mb-6 bg-red-50 border border-red-200 rounded-lg p-4">
				<div class="flex items-center">
					<i class="fas fa-exclamation-circle text-red-500 mr-2"></i><span
						class="text-red-800">${error}</span>
				</div>
			</div>
		</c:if>

		<!-- Validation Error Messages -->
		<c:if test="${not empty validationErrors}">
			<div class="mb-6 bg-red-50 border border-red-200 rounded-lg p-4">
				<div class="flex items-start">
					<i class="fas fa-exclamation-triangle text-red-500 mr-2 mt-1"></i>
					<div class="flex-1">
						<h3 class="text-red-800 font-medium mb-2">Please correct the
							following errors:</h3>
						<ul class="list-disc list-inside text-red-700 space-y-1">
							<c:forEach var="error" items="${validationErrors}">
								<li>${error}</li>
							</c:forEach>
						</ul>
					</div>
				</div>
			</div>
		</c:if>

		<!-- Field-specific validation errors -->
		<c:if test="${not empty fieldErrors}">
			<div
				class="mb-6 bg-yellow-50 border border-yellow-200 rounded-lg p-4">
				<div class="flex items-start">
					<i class="fas fa-exclamation-triangle text-yellow-500 mr-2 mt-1"></i>
					<div class="flex-1">
						<h3 class="text-yellow-800 font-medium mb-2">Please check the
							following fields:</h3>
						<ul class="list-disc list-inside text-yellow-700 space-y-1">
							<c:forEach var="fieldError" items="${fieldErrors}">
								<li><strong>${fieldError.key}:</strong> ${fieldError.value}</li>
							</c:forEach>
						</ul>
					</div>
				</div>
			</div>
		</c:if>

		<form action="auth" method="post" class="space-y-6">
			<input type="hidden" name="action" value="register">

			<div class="grid grid-cols-1 md:grid-cols-2 gap-4">
				<div>
					<label for="firstName"
						class="block text-sm font-medium text-gray-700 mb-2"><i
						class="fas fa-id-card mr-2"></i>First Name</label> <input type="text"
						id="firstName" name="firstName" required
						placeholder="Enter first name" value="${firstName}"
						class="w-full px-4 py-3 border ${fieldErrors.firstName != null ? 'border-red-300 focus:ring-red-500 focus:border-red-500' : 'border-gray-300 focus:ring-primary-500 focus:border-primary-500'} rounded-lg">
					<c:if test="${fieldErrors.firstName != null}">
						<p class="mt-1 text-sm text-red-600">${fieldErrors.firstName}</p>
					</c:if>
				</div>
				<div>
					<label for="lastName"
						class="block text-sm font-medium text-gray-700 mb-2"><i
						class="fas fa-id-card mr-2"></i>Last Name</label> <input type="text"
						id="lastName" name="lastName" required
						placeholder="Enter last name" value="${lastName}"
						class="w-full px-4 py-3 border ${fieldErrors.lastName != null ? 'border-red-300 focus:ring-red-500 focus:border-red-500' : 'border-gray-300 focus:ring-primary-500 focus:border-primary-500'} rounded-lg">
					<c:if test="${fieldErrors.lastName != null}">
						<p class="mt-1 text-sm text-red-600">${fieldErrors.lastName}</p>
					</c:if>
				</div>
			</div>

			<div>
				<label for="username"
					class="block text-sm font-medium text-gray-700 mb-2"><i
					class="fas fa-user mr-2"></i>Username</label> <input type="text"
					id="username" name="username" required
					placeholder="Choose a username" value="${username}"
					class="w-full px-4 py-3 border ${fieldErrors.username != null ? 'border-red-300 focus:ring-red-500 focus:border-red-500' : 'border-gray-300 focus:ring-primary-500 focus:border-primary-500'} rounded-lg">
				<c:if test="${fieldErrors.username != null}">
					<p class="mt-1 text-sm text-red-600">${fieldErrors.username}</p>
				</c:if>
			</div>

			<div>
				<label for="email"
					class="block text-sm font-medium text-gray-700 mb-2"><i
					class="fas fa-envelope mr-2"></i>Email</label> <input type="email"
					id="email" name="email" required placeholder="Enter your email"
					value="${email}"
					class="w-full px-4 py-3 border ${fieldErrors.email != null ? 'border-red-300 focus:ring-red-500 focus:border-red-500' : 'border-gray-300 focus:ring-primary-500 focus:border-primary-500'} rounded-lg">
				<c:if test="${fieldErrors.email != null}">
					<p class="mt-1 text-sm text-red-600">${fieldErrors.email}</p>
				</c:if>
			</div>

			<div class="grid grid-cols-1 md:grid-cols-2 gap-4">
				<div>
					<label for="password"
						class="block text-sm font-medium text-gray-700 mb-2"><i
						class="fas fa-lock mr-2"></i>Password</label> <input type="password"
						id="password" name="password" required
						placeholder="Choose a password"
						class="w-full px-4 py-3 border ${fieldErrors.password != null ? 'border-red-300 focus:ring-red-500 focus:border-red-500' : 'border-gray-300 focus:ring-primary-500 focus:border-primary-500'} rounded-lg">
					<c:if test="${fieldErrors.password != null}">
						<p class="mt-1 text-sm text-red-600">${fieldErrors.password}</p>
					</c:if>
				</div>
				<div>
					<label for="confirmPassword"
						class="block text-sm font-medium text-gray-700 mb-2"><i
						class="fas fa-lock mr-2"></i>Confirm Password</label> <input
						type="password" id="confirmPassword" name="confirmPassword"
						required placeholder="Confirm your password"
						class="w-full px-4 py-3 border ${fieldErrors.confirmPassword != null ? 'border-red-300 focus:ring-red-500 focus:border-red-500' : 'border-gray-300 focus:ring-primary-500 focus:border-primary-500'} rounded-lg">
					<c:if test="${fieldErrors.confirmPassword != null}">
						<p class="mt-1 text-sm text-red-600">${fieldErrors.confirmPassword}</p>
					</c:if>
				</div>
			</div>

			<button type="submit"
				class="w-full bg-gradient-to-r from-cyan-500 to-blue-600 text-white font-semibold py-3 px-6 rounded-xl shadow-lg shadow-cyan-500/25 hover:from-cyan-600 hover:to-blue-700 transition-all duration-200 flex items-center justify-center">
				<i class="fas fa-user-plus mr-2"></i>Create Account
			</button>
		</form>

		<div class="mt-8 text-center">
			<p class="text-gray-600 text-sm">
				Already have an account? <a href="auth?action=login"
					class="text-primary-600 hover:text-primary-700 font-medium">Login
					here</a>
			</p>
		</div>

		<div class="mt-6 text-center">
			<a href="index.jsp"
				class="text-gray-500 hover:text-gray-700 text-sm flex items-center justify-center"><i
				class="fas fa-arrow-left mr-2"></i>Back to Home</a>
		</div>
	</div>
</body>
</html>
