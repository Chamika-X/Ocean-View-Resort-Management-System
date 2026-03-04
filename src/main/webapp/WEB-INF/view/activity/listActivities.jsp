<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%
// Create formatters for date formatting
java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy");
java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss");
pageContext.setAttribute("dateFormatter", dateFormatter);
pageContext.setAttribute("timeFormatter", timeFormatter);
%>

<%@ include file="../shared/layout.jsp"%>

<div class="px-8 py-6">
	<!-- Page Header -->
	<div class="mb-8">
		<div class="flex items-center justify-between">
			<div>
				<h1 class="text-3xl font-bold text-gray-900">Activity Log</h1>
				<p class="text-gray-600 mt-2">View and filter system activities and events</p>
			</div>
			<div class="flex items-center space-x-4">
				<a href="dashboard" class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50">
					<i class="fas fa-arrow-left mr-2"></i>
					Back to Dashboard
				</a>
			</div>
		</div>
	</div>

	<!-- Alerts -->
	<c:if test="${not empty success}">
		<div class="mb-6 bg-emerald-50 border border-emerald-200 rounded-xl p-4">
			<div class="flex items-center">
				<i class="fas fa-check-circle text-green-500 mr-2"></i>
				<span class="text-green-800">${success}</span>
			</div>
		</div>
	</c:if>

	<c:if test="${not empty error}">
		<div class="mb-6 bg-red-50 border border-red-200 rounded-xl p-4">
			<div class="flex items-center">
				<i class="fas fa-exclamation-circle text-red-500 mr-2"></i>
				<span class="text-red-800">${error}</span>
			</div>
		</div>
	</c:if>

	<!-- Statistics Cards -->
	<div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
		<div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
			<div class="flex items-center justify-between">
				<div>
					<p class="text-sm font-medium text-gray-600 mb-1">Total Activities</p>
					<p class="text-3xl font-bold text-gray-900">${activityStats.totalActivities}</p>
				</div>
				<div class="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center">
					<i class="fas fa-list text-blue-600 text-xl"></i>
				</div>
			</div>
		</div>

		<div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
			<div class="flex items-center justify-between">
				<div>
					<p class="text-sm font-medium text-gray-600 mb-1">Bill Activities</p>
					<p class="text-3xl font-bold text-gray-900">${activityStats.billActivities}</p>
				</div>
				<div class="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center">
					<i class="fas fa-file-invoice-dollar text-green-600 text-xl"></i>
				</div>
			</div>
		</div>

		<div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
			<div class="flex items-center justify-between">
				<div>
					<p class="text-sm font-medium text-gray-600 mb-1">Customer Activities</p>
					<p class="text-3xl font-bold text-gray-900">${activityStats.customerActivities}</p>
				</div>
				<div class="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center">
					<i class="fas fa-users text-purple-600 text-xl"></i>
				</div>
			</div>
		</div>

		<div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
			<div class="flex items-center justify-between">
				<div>
					<p class="text-sm font-medium text-gray-600 mb-1">Recent Activities</p>
					<p class="text-3xl font-bold text-gray-900">${activityStats.recentActivities}</p>
				</div>
				<div class="w-12 h-12 bg-cyan-100 rounded-lg flex items-center justify-center">
					<i class="fas fa-clock text-cyan-600 text-xl"></i>
				</div>
			</div>
		</div>
	</div>

	<!-- Filters Section -->
	<div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 mb-8">
		<div class="flex items-center justify-between mb-6">
			<h3 class="text-lg font-semibold text-gray-900">Filters</h3>
			<button onclick="clearFilters()" class="text-sm text-gray-500 hover:text-gray-700">
				<i class="fas fa-times mr-1"></i>Clear All
			</button>
		</div>

		<form method="GET" action="activity" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
			<input type="hidden" name="action" value="list">
			
			<!-- Activity Type Filter -->
			<div>
				<label for="activityType" class="block text-sm font-medium text-gray-700 mb-2">Activity Type</label>
				<select name="activityType" id="activityType" class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-cyan-500 focus:border-cyan-500">
					<option value="">All Types</option>
					<c:forEach var="type" items="${activityTypes}">
						<option value="${type}" ${filterActivityType == type ? 'selected' : ''}>
							<c:choose>
								<c:when test="${type == 'bill_created'}">Bill Created</c:when>
								<c:when test="${type == 'bill_updated'}">Bill Updated</c:when>
								<c:when test="${type == 'bill_paid'}">Bill Paid</c:when>
								<c:when test="${type == 'customer_added'}">Customer Added</c:when>
								<c:when test="${type == 'customer_updated'}">Customer Updated</c:when>
								<c:when test="${type == 'product_added'}">Product Added</c:when>
								<c:when test="${type == 'product_updated'}">Product Updated</c:when>
								<c:when test="${type == 'user_login'}">User Login</c:when>
								<c:when test="${type == 'system_backup'}">System Backup</c:when>
								<c:when test="${type == 'system_maintenance'}">System Maintenance</c:when>
								<c:otherwise>${type}</c:otherwise>
							</c:choose>
						</option>
					</c:forEach>
				</select>
			</div>

			<!-- Username Filter -->
			<div>
				<label for="username" class="block text-sm font-medium text-gray-700 mb-2">Username</label>
				<input type="text" name="username" id="username" value="${filterUsername}" 
					   placeholder="Enter username" 
					   class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-cyan-500 focus:border-cyan-500">
			</div>

			<!-- Date Range Filter -->
			<div>
				<label for="startDate" class="block text-sm font-medium text-gray-700 mb-2">Start Date</label>
				<input type="date" name="startDate" id="startDate" value="${filterStartDate}" 
					   class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-cyan-500 focus:border-cyan-500">
			</div>

			<div>
				<label for="endDate" class="block text-sm font-medium text-gray-700 mb-2">End Date</label>
				<input type="date" name="endDate" id="endDate" value="${filterEndDate}" 
					   class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-cyan-500 focus:border-cyan-500">
			</div>

			<!-- Search Filter -->
			<div class="md:col-span-2">
				<label for="search" class="block text-sm font-medium text-gray-700 mb-2">Search</label>
				<input type="text" name="search" id="search" value="${filterSearchTerm}" 
					   placeholder="Search in description, entity name, or username" 
					   class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-cyan-500 focus:border-cyan-500">
			</div>

			<!-- Page Size Selector -->
			<div>
				<label for="pageSize" class="block text-sm font-medium text-gray-700 mb-2">Items per page</label>
				<select name="pageSize" id="pageSize" class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-cyan-500 focus:border-cyan-500">
					<option value="10" ${empty pagination ? 'selected' : (pagination.pageSize == 10 ? 'selected' : '')}>10</option>
					<option value="25" ${empty pagination ? '' : (pagination.pageSize == 25 ? 'selected' : '')}>25</option>
					<option value="50" ${empty pagination ? '' : (pagination.pageSize == 50 ? 'selected' : '')}>50</option>
					<option value="100" ${empty pagination ? '' : (pagination.pageSize == 100 ? 'selected' : '')}>100</option>
				</select>
			</div>

			<!-- Filter Buttons -->
			<div class="md:col-span-2 flex items-end space-x-4">
				<button type="submit" class="inline-flex items-center px-4 py-2 border border-transparent rounded-xl shadow-sm text-sm font-medium text-white bg-gradient-to-r from-cyan-500 to-blue-600 hover:from-cyan-600 hover:to-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-cyan-400 shadow-lg shadow-cyan-500/25">
					<i class="fas fa-search mr-2"></i>
					Apply Filters
				</button>
				<button type="button" onclick="clearFilters()" class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-cyan-500">
					<i class="fas fa-times mr-2"></i>
					Clear
				</button>
			</div>
		</form>
	</div>

	<!-- Activities List -->
	<div class="bg-white rounded-2xl shadow-sm border border-gray-100">
		<div class="px-6 py-4 border-b border-gray-200">
			<div class="flex items-center justify-between">
				<h3 class="text-lg font-semibold text-gray-900">
					Activities 
					<c:if test="${not empty pagination}">
						<span class="text-sm font-normal text-gray-500">(${pagination.totalItems} results)</span>
					</c:if>
				</h3>
				<div class="flex items-center space-x-2">
					<button onclick="exportActivities()" class="inline-flex items-center px-3 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50">
						<i class="fas fa-download mr-2"></i>
						Export
					</button>
				</div>
			</div>
		</div>

		<div class="overflow-x-auto">
			<c:choose>
				<c:when test="${not empty activities}">
					<table class="min-w-full divide-y divide-gray-200">
						<thead class="bg-gray-50/80">
							<tr>
								<th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Activity</th>
								<th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Entity</th>
								<th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">User</th>
								<th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date & Time</th>
								<th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
							</tr>
						</thead>
						<tbody class="bg-white divide-y divide-gray-200">
							<c:forEach var="activity" items="${activities}">
								<tr class="hover:bg-gray-50">
									<td class="px-6 py-4 whitespace-nowrap">
										<div class="flex items-center">
											<div class="w-8 h-8 bg-${activity.activityColor}-100 rounded-lg flex items-center justify-center mr-3">
												<i class="${activity.activityIcon} text-${activity.activityColor}-600"></i>
											</div>
											<div>
												<div class="text-sm font-medium text-gray-900">${activity.description}</div>
												<div class="text-xs text-gray-500 capitalize">${activity.activityType.replace('_', ' ')}</div>
											</div>
										</div>
									</td>
									<td class="px-6 py-4 whitespace-nowrap">
										<c:choose>
											<c:when test="${not empty activity.entityName}">
												<span class="text-sm text-gray-900">${activity.entityName}</span>
												<div class="text-xs text-gray-500">${activity.entityType}</div>
											</c:when>
											<c:otherwise>
												<span class="text-sm text-gray-400">-</span>
											</c:otherwise>
										</c:choose>
									</td>
									<td class="px-6 py-4 whitespace-nowrap">
										<div class="flex items-center">
											<div class="w-8 h-8 bg-gray-100 rounded-full flex items-center justify-center mr-2">
												<i class="fas fa-user text-gray-600 text-sm"></i>
											</div>
											<span class="text-sm text-gray-900">${activity.username}</span>
										</div>
									</td>
									<td class="px-6 py-4 whitespace-nowrap">
										<div class="text-sm text-gray-900">
											${activity.timestamp.format(dateFormatter)}
										</div>
										<div class="text-xs text-gray-500">
											${activity.timestamp.format(timeFormatter)}
										</div>
										<div class="text-xs text-gray-400">${activity.timeAgo}</div>
									</td>
									<td class="px-6 py-4 whitespace-nowrap">
										<c:choose>
											<c:when test="${activity.status == 'completed'}">
												<span class="px-2 py-1 text-xs bg-green-100 text-green-800 rounded-full">Completed</span>
											</c:when>
											<c:when test="${activity.status == 'pending'}">
												<span class="px-2 py-1 text-xs bg-yellow-100 text-yellow-800 rounded-full">Pending</span>
											</c:when>
											<c:when test="${activity.status == 'failed'}">
												<span class="px-2 py-1 text-xs bg-red-100 text-red-800 rounded-full">Failed</span>
											</c:when>
											<c:otherwise>
												<span class="px-2 py-1 text-xs bg-gray-100 text-gray-800 rounded-full">${activity.status}</span>
											</c:otherwise>
										</c:choose>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:when>
				<c:otherwise>
					<div class="text-center py-12">
						<i class="fas fa-info-circle text-gray-400 text-4xl mb-4"></i>
						<h3 class="text-lg font-medium text-gray-900 mb-2">No activities found</h3>
						<p class="text-gray-500">Try adjusting your filters or check back later for new activities.</p>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
		

		
		<!-- Pagination Controls -->
		<c:if test="${not empty pagination and pagination.totalPages > 1}">
			<%@ include file="./pagination.jsp" %>
		</c:if>
	</div>
</div>

<script>
function clearFilters() {
	document.getElementById('activityType').value = '';
	document.getElementById('username').value = '';
	document.getElementById('startDate').value = '';
	document.getElementById('endDate').value = '';
	document.getElementById('search').value = '';
	// Preserve current page size when clearing filters
	const currentPageSize = document.getElementById('pageSize').value;
	window.location.href = 'activity?action=list&pageSize=' + currentPageSize;
}

function exportActivities() {
	// Get current filter parameters
	const params = new URLSearchParams(window.location.search);
	params.set('action', 'list');
	
	// Create export URL
	const exportUrl = 'activity?' + params.toString() + '&export=true';
	
	// For now, just show an alert. In a real implementation, you'd create an export endpoint
	alert('Export functionality would be implemented here. Current filters would be applied to the export.');
}
</script>
<footer class="border-t border-gray-200 bg-white mt-auto px-8 py-4">
    <p class="text-center text-xs text-gray-400">&copy; 2026 Ocean View Resort, Galle, Sri Lanka. All rights reserved.</p>
</footer>
    </div>
</div>
</body>
</html>