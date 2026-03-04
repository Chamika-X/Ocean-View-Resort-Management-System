<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="../shared/layout.jsp"%>
<!-- Main Content -->
<div class="px-8 py-6">
	<!-- Page Header -->
	<div class="flex items-center justify-between mb-6">
		<div>
			<h1 class="text-3xl font-bold text-gray-900">Edit Product</h1>
			<p class="text-gray-600 mt-1">Update product information</p>
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

	<!-- Form Card -->
	<div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
		<!-- Product Info Header -->
		<div class="mb-6 bg-orange-50 border border-cyan-200 rounded-lg p-4">
			<div class="flex items-center justify-between">
				<div class="text-orange-700">
					<i class="fas fa-hashtag mr-2"></i><strong>Product ID:</strong>
					${product.productId}
				</div>
				<div class="text-orange-700">
					<i class="fas fa-tag mr-2"></i><strong>Current Price:</strong>
					<fmt:formatNumber value="${product.price}" type="currency"
						currencySymbol="Rs. " />
				</div>
			</div>
		</div>

		<form action="product?action=update" method="post" class="space-y-6">
			<input type="hidden" name="id" value="${product.productId}">

			<div>
				<label for="name"
					class="block text-sm font-medium text-gray-700 mb-2"> <i
					class="fas fa-box mr-2"></i>Product Name
				</label> <input type="text" id="name" name="name" required
					value="${name != null ? name : product.name}"
					class="w-full px-4 py-3 border ${fieldErrors.name != null ? 'border-red-300 focus:ring-red-500 focus:border-red-500' : 'border-gray-300 focus:ring-cyan-500 focus:border-cyan-500'} rounded-lg transition-colors duration-200">
				<c:if test="${fieldErrors.name != null}">
					<p class="mt-1 text-sm text-red-600">${fieldErrors.name}</p>
				</c:if>
			</div>

			<div class="grid grid-cols-1 md:grid-cols-2 gap-6">
				<div>
					<label for="price"
						class="block text-sm font-medium text-gray-700 mb-2"> <i
						class="fas fa-tag mr-2"></i>Price (Rs.)
					</label> <input type="number" step="0.01" id="price" name="price" required
						value="${price != null ? price : product.price}"
						class="w-full px-4 py-3 border ${fieldErrors.price != null ? 'border-red-300 focus:ring-red-500 focus:border-red-500' : 'border-gray-300 focus:ring-cyan-500 focus:border-cyan-500'} rounded-lg transition-colors duration-200">
					<c:if test="${fieldErrors.price != null}">
						<p class="mt-1 text-sm text-red-600">${fieldErrors.price}</p>
					</c:if>
				</div>

				<div>
					<label for="quantity"
						class="block text-sm font-medium text-gray-700 mb-2"> <i
						class="fas fa-boxes mr-2"></i>Stock Quantity
					</label> <input type="number" id="quantity" name="quantity" required
						value="${quantity != null ? quantity : product.quantity}" min="0"
						class="w-full px-4 py-3 border ${fieldErrors.quantity != null ? 'border-red-300 focus:ring-red-500 focus:border-red-500' : 'border-gray-300 focus:ring-cyan-500 focus:border-cyan-500'} rounded-lg transition-colors duration-200">
					<c:if test="${fieldErrors.quantity != null}">
						<p class="mt-1 text-sm text-red-600">${fieldErrors.quantity}</p>
					</c:if>
				</div>
			</div>





			<div>
				<label for="description"
					class="block text-sm font-medium text-gray-700 mb-2"> <i
					class="fas fa-align-left mr-2"></i>Description
				</label>
				<!-- Fix: Ensure textarea stays inside the card and doesn't overflow -->
				<textarea id="description" name="description"
					class="w-full px-4 py-3 border ${fieldErrors.description != null ? 'border-red-300 focus:ring-red-500 focus:border-red-500' : 'border-gray-300 focus:ring-cyan-500 focus:border-cyan-500'} rounded-lg transition-colors duration-200 min-h-[120px] resize-none box-border"
					style="max-width:100%; box-sizing:border-box;">${description != null ? description : product.description}</textarea>
				<c:if test="${fieldErrors.description != null}">
					<p class="mt-1 text-sm text-red-600">${fieldErrors.description}</p>
				</c:if>
			</div>

			<div class="flex items-center gap-4 pt-4">
				<button type="submit"
					class="bg-gradient-to-r from-cyan-500 to-blue-600 text-white px-6 py-3 rounded-lg hover:from-cyan-600 hover:to-blue-700 transition-colors duration-200 flex items-center">
					<i class="fas fa-save mr-2"></i>Update Product
				</button>
				<a href="product?action=list"
					class="bg-gray-100 text-gray-700 px-6 py-3 rounded-lg hover:bg-gray-200 transition-colors duration-200 flex items-center">
					<i class="fas fa-times mr-2"></i>Cancel
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