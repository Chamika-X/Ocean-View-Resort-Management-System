<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="../shared/layout.jsp"%>
<!-- Main Content -->
<div class="px-8 py-6">
	<!-- Page Header -->
	<div class="flex items-center justify-between mb-6">
		<div>
			<h1 class="text-3xl font-bold text-gray-900">Product Details</h1>
			<p class="text-gray-600 mt-1">View product information and manage
				inventory</p>
		</div>
		<a href="product?action=list"
			class="bg-gray-100 text-gray-700 px-4 py-2 rounded-lg hover:bg-gray-200 transition-colors duration-200 flex items-center">
			<i class="fas fa-list mr-2"></i>Back to Products
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

	<!-- Product Details Card -->
	<div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
		<!-- Product Header -->
		<div
			class="flex flex-col md:flex-row md:items-center md:justify-between mb-6 border-b pb-4">
			<div class="flex items-center mb-4 md:mb-0">
				<div
					class="w-12 h-12 bg-cyan-100 rounded-lg flex items-center justify-center mr-4">
					<i class="fas fa-box text-cyan-600 text-xl"></i>
				</div>
				<h2 class="text-2xl font-semibold text-gray-900">${product.name}</h2>
			</div>
			<span
				class="inline-flex items-center bg-gradient-to-r from-cyan-500 to-blue-600 text-white px-3 py-1 rounded-full text-sm">
				<i class="fas fa-hashtag mr-1"></i>ID: ${product.productId}
			</span>
		</div>

		<!-- Product Information Grid -->
		<div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
			<div class="bg-orange-50 border border-cyan-200 rounded-lg p-4">
				<div class="flex items-center mb-2">
					<i class="fas fa-tag text-cyan-600 mr-2"></i>
					<div class="text-orange-700 text-sm font-medium">Price</div>
				</div>
				<div class="text-2xl font-bold text-orange-800">
					<fmt:formatNumber value="${product.price}" type="currency"
						currencySymbol="Rs. " />
				</div>
			</div>
			<div class="bg-emerald-50 border border-emerald-200 rounded-xl p-4">
				<div class="flex items-center mb-2">
					<i class="fas fa-boxes text-green-600 mr-2"></i>
					<div class="text-green-700 text-sm font-medium">Stock Quantity</div>
				</div>
				<div class="text-xl font-semibold text-green-800">
					<c:choose>
						<c:when test="${product.quantity > 0}">
							${product.quantity} units in stock
						</c:when>
						<c:otherwise>
							<span class="text-red-600">Out of stock</span>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>



		<!-- Description Section -->
		<div class="bg-gray-50 border border-gray-200 rounded-lg p-4">
			<div class="flex items-center mb-3">
				<i class="fas fa-align-left text-gray-600 mr-2"></i>
				<div class="text-gray-700 text-sm font-medium">Description</div>
			</div>
			<div class="text-gray-800 whitespace-pre-line min-h-[80px]">
				<c:choose>
					<c:when test="${not empty product.description}">${product.description}</c:when>
					<c:otherwise>
						<em class="text-gray-500">No description available.</em>
					</c:otherwise>
				</c:choose>
			</div>
		</div>

		<!-- Action Buttons -->
		<div class="flex items-center gap-4 mt-8 justify-center">
			<a href="product?action=edit&id=${product.productId}"
				class="bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700 transition-colors duration-200 flex items-center">
				<i class="fas fa-edit mr-2"></i>Edit Product
			</a> <a href="product?action=delete&id=${product.productId}"
				onclick="return confirm('Are you sure you want to delete this product?')"
				class="bg-red-600 text-white px-6 py-3 rounded-lg hover:bg-red-700 transition-colors duration-200 flex items-center">
				<i class="fas fa-trash mr-2"></i>Delete
			</a> <a href="product?action=list"
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