<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="../shared/layout.jsp"%>
<!-- Main Content -->
<div class="px-8 py-6">
	<!-- Page Header -->
	<div class="flex items-center justify-between mb-6">
		<div>
			<h1 class="text-3xl font-bold text-gray-900">Bill Management</h1>
			<p class="text-gray-600 mt-1">Create and manage customer bills</p>
		</div>
		<a href="bill?action=create"
			class="bg-gradient-to-r from-cyan-500 to-blue-600 text-white px-4 py-2 rounded-lg hover:from-cyan-600 hover:to-blue-700 transition-colors duration-200 flex items-center">
			<i class="fas fa-plus mr-2"></i>Create Bill
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

	<!-- Search Form -->
	<div
		class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 mb-6">
		<form method="get" action="bill"
			class="flex flex-col md:flex-row gap-4">
			<input type="hidden" name="action" value="list">
			<div class="flex-1">
				<input type="text" name="search" value="${param.search}"
					placeholder="Search bills by customer or bill number..."
					class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-cyan-500 focus:border-cyan-500 transition-colors duration-200">
			</div>
			<div class="flex items-center space-x-2">
				<select name="pageSize" class="px-3 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-cyan-500 focus:border-cyan-500">
					<option value="10" ${param.pageSize == '10' || empty param.pageSize ? 'selected' : ''}>10 per page</option>
					<option value="25" ${param.pageSize == '25' ? 'selected' : ''}>25 per page</option>
					<option value="50" ${param.pageSize == '50' ? 'selected' : ''}>50 per page</option>
					<option value="100" ${param.pageSize == '100' ? 'selected' : ''}>100 per page</option>
				</select>
				<button type="submit"
					class="bg-gradient-to-r from-cyan-500 to-blue-600 text-white px-6 py-3 rounded-lg hover:from-cyan-600 hover:to-blue-700 transition-colors duration-200 flex items-center justify-center">
					<i class="fas fa-search mr-2"></i>Search
				</button>
				<c:if test="${not empty param.search}">
					<a href="bill?action=list"
						class="bg-gray-500 text-white px-6 py-3 rounded-lg hover:bg-gray-600 transition-colors duration-200 flex items-center justify-center">
						<i class="fas fa-times mr-2"></i>Clear
					</a>
				</c:if>
			</div>
		</form>
	</div>

	<!-- Search Results Info -->
	<c:if test="${not empty param.search}">
		<div class="mb-4 bg-blue-50 border border-blue-200 rounded-lg p-4">
			<div class="flex items-center">
				<i class="fas fa-search text-blue-500 mr-2"></i>
				<span class="text-blue-800">
					Search results for "<strong>${param.search}</strong>" - 
					<c:choose>
						<c:when test="${empty bills}">No bills found</c:when>
						<c:otherwise>${pagination.totalItems} bill(s) found</c:otherwise>
					</c:choose>
				</span>
			</div>
		</div>
	</c:if>

	<!-- Bills Table -->
	<div
		class="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
		<div class="overflow-x-auto">
			<table class="min-w-full divide-y divide-gray-200">
				<thead class="bg-gray-50/80">
					<tr>
						<th
							class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Bill
							Info</th>
						<th
							class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Customer</th>
						<th
							class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Amount</th>
						<th
							class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date</th>
						<th
							class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
					</tr>
				</thead>
				<tbody class="bg-white divide-y divide-gray-200">
					<c:forEach var="bill" items="${bills}">
						<tr class="hover:bg-gray-50 transition-colors duration-200">
							<td class="px-6 py-4 whitespace-nowrap">
								<div class="flex items-center">
									<div
										class="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center mr-3">
										<i class="fas fa-file-invoice-dollar text-purple-600 text-lg"></i>
									</div>
									<div>
										<div class="text-sm font-medium text-gray-900">Bill
											#${bill.billId}</div>
										<div class="text-sm text-gray-500 capitalize">${bill.status}</div>
									</div>
								</div>
							</td>
							<td class="px-6 py-4 whitespace-nowrap">
								<div class="text-sm font-medium text-gray-900">${bill.customerName}</div>
								<div class="text-sm text-gray-500">Acc:
									${bill.accountNumber}</div>
							</td>
							<td class="px-6 py-4 whitespace-nowrap">
								<div class="text-sm font-medium text-gray-900">
									<fmt:formatNumber value="${bill.totalAmount}" type="currency"
										currencySymbol="Rs. " />
								</div>
							</td>
							<td class="px-6 py-4 whitespace-nowrap">
								<div class="text-sm text-gray-900">${bill.billDate}</div>
							</td>
							<td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
								<div class="flex items-center space-x-2">
									<a href="bill?action=view&id=${bill.billId}"
										class="text-cyan-600 hover:text-cyan-900 transition-colors duration-200">
										<i class="fas fa-eye"></i>
									</a>
									<a href="bill?action=delete&id=${bill.billId}"
										onclick="return confirm('Are you sure you want to delete this bill?')"
										class="text-red-600 hover:text-red-900 transition-colors duration-200">
										<i class="fas fa-trash"></i>
									</a>
								</div>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>

		<c:if test="${empty bills}">
			<div class="text-center py-12">
				<div
					class="w-16 h-16 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-4">
					<i class="fas fa-file-invoice-dollar text-gray-400 text-2xl"></i>
				</div>
				<h3 class="text-lg font-medium text-gray-900 mb-2">No bills
					found</h3>
				<p class="text-gray-500 mb-4">Get started by creating your first
					bill.</p>
				<a href="bill?action=create"
					class="bg-gradient-to-r from-cyan-500 to-blue-600 text-white px-4 py-2 rounded-lg hover:from-cyan-600 hover:to-blue-700 transition-colors duration-200">
					Create Bill </a>
			</div>
		</c:if>
	</div>
	
	<!-- Pagination -->
	<c:set var="paginationUrl" value="bill" />
	<%@ include file="../shared/pagination.jsp" %>
</div>
<footer class="border-t border-gray-200 bg-white mt-auto px-8 py-4">
    <p class="text-center text-xs text-gray-400">&copy; 2026 Ocean View Resort, Galle, Sri Lanka. All rights reserved.</p>
</footer>
    </div>
</div>
</body>
</html>