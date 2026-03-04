<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${pagination.totalPages > 1}">
	<div class="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6">
		<div class="flex-1 flex justify-between sm:hidden">
			<c:if test="${pagination.hasPrevious}">
				<a href="${paginationUrl}?action=list&page=${pagination.currentPage - 1}&pageSize=${pagination.pageSize}${not empty searchTerm ? '&search='.concat(searchTerm) : ''}${not empty filterActivityType ? '&activityType='.concat(filterActivityType) : ''}${not empty filterUsername ? '&username='.concat(filterUsername) : ''}${not empty filterStartDate ? '&startDate='.concat(filterStartDate) : ''}${not empty filterEndDate ? '&endDate='.concat(filterEndDate) : ''}"
					class="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50">
					Previous
				</a>
			</c:if>
			<c:if test="${pagination.hasNext}">
				<a href="${paginationUrl}?action=list&page=${pagination.currentPage + 1}&pageSize=${pagination.pageSize}${not empty searchTerm ? '&search='.concat(searchTerm) : ''}${not empty filterActivityType ? '&activityType='.concat(filterActivityType) : ''}${not empty filterUsername ? '&username='.concat(filterUsername) : ''}${not empty filterStartDate ? '&startDate='.concat(filterStartDate) : ''}${not empty filterEndDate ? '&endDate='.concat(filterEndDate) : ''}"
					class="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50">
					Next
				</a>
			</c:if>
		</div>
		<div class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
			<div>
				<p class="text-sm text-gray-700">
					Showing
					<span class="font-medium">${pagination.startItem}</span>
					to
					<span class="font-medium">${pagination.endItem}</span>
					of
					<span class="font-medium">${pagination.totalItems}</span>
					results
				</p>
			</div>
			<div>
				<nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px" aria-label="Pagination">
					<!-- Previous Page -->
					<c:if test="${pagination.hasPrevious}">
						<a href="${paginationUrl}?action=list&page=${pagination.currentPage - 1}&pageSize=${pagination.pageSize}${not empty searchTerm ? '&search='.concat(searchTerm) : ''}${not empty filterActivityType ? '&activityType='.concat(filterActivityType) : ''}${not empty filterUsername ? '&username='.concat(filterUsername) : ''}${not empty filterStartDate ? '&startDate='.concat(filterStartDate) : ''}${not empty filterEndDate ? '&endDate='.concat(filterEndDate) : ''}"
							class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50">
							<span class="sr-only">Previous</span>
							<i class="fas fa-chevron-left"></i>
						</a>
					</c:if>
					
					<!-- Page Numbers -->
					<c:choose>
						<c:when test="${pagination.totalPages <= 7}">
							<!-- Show all pages if 7 or fewer -->
							<c:forEach begin="1" end="${pagination.totalPages}" var="pageNum">
								<c:choose>
									<c:when test="${pageNum == pagination.currentPage}">
										<span class="relative inline-flex items-center px-4 py-2 border border-cyan-500 bg-cyan-50 text-sm font-medium text-cyan-600">
											${pageNum}
										</span>
									</c:when>
									<c:otherwise>
										<a href="${paginationUrl}?page=${pageNum}&pageSize=${pagination.pageSize}${not empty searchTerm ? '&search='.concat(searchTerm) : ''}"
											class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50">
											${pageNum}
										</a>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<!-- Show smart pagination for more than 7 pages -->
							<c:choose>
								<c:when test="${pagination.currentPage <= 4}">
									<!-- Show first 5 pages + ellipsis + last page -->
									<c:forEach begin="1" end="5" var="pageNum">
										<c:choose>
											<c:when test="${pageNum == pagination.currentPage}">
												<span class="relative inline-flex items-center px-4 py-2 border border-cyan-500 bg-cyan-50 text-sm font-medium text-cyan-600">
													${pageNum}
												</span>
											</c:when>
											<c:otherwise>
												<a href="${paginationUrl}?page=${pageNum}&pageSize=${pagination.pageSize}${not empty searchTerm ? '&search='.concat(searchTerm) : ''}"
													class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50">
													${pageNum}
												</a>
											</c:otherwise>
										</c:choose>
									</c:forEach>
									<span class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-700">
										...
									</span>
									<a href="${paginationUrl}?page=${pagination.totalPages}&pageSize=${pagination.pageSize}${not empty searchTerm ? '&search='.concat(searchTerm) : ''}"
										class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50">
										${pagination.totalPages}
									</a>
								</c:when>
								<c:when test="${pagination.currentPage >= pagination.totalPages - 3}">
									<!-- Show first page + ellipsis + last 5 pages -->
									<a href="${paginationUrl}?page=1&pageSize=${pagination.pageSize}${not empty searchTerm ? '&search='.concat(searchTerm) : ''}"
										class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50">
										1
									</a>
									<span class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-700">
										...
									</span>
									<c:forEach begin="${pagination.totalPages - 4}" end="${pagination.totalPages}" var="pageNum">
										<c:choose>
											<c:when test="${pageNum == pagination.currentPage}">
												<span class="relative inline-flex items-center px-4 py-2 border border-cyan-500 bg-cyan-50 text-sm font-medium text-cyan-600">
													${pageNum}
												</span>
											</c:when>
											<c:otherwise>
												<a href="${paginationUrl}?page=${pageNum}&pageSize=${pagination.pageSize}${not empty searchTerm ? '&search='.concat(searchTerm) : ''}"
													class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50">
													${pageNum}
												</a>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<!-- Show first page + ellipsis + current-1, current, current+1 + ellipsis + last page -->
									<a href="${paginationUrl}?page=1&pageSize=${pagination.pageSize}${not empty searchTerm ? '&search='.concat(searchTerm) : ''}"
										class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50">
										1
									</a>
									<span class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-700">
										...
									</span>
									<a href="${paginationUrl}?page=${pagination.currentPage - 1}&pageSize=${pagination.pageSize}${not empty searchTerm ? '&search='.concat(searchTerm) : ''}"
										class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50">
										${pagination.currentPage - 1}
									</a>
									<span class="relative inline-flex items-center px-4 py-2 border border-cyan-500 bg-cyan-50 text-sm font-medium text-cyan-600">
										${pagination.currentPage}
									</span>
									<a href="${paginationUrl}?page=${pagination.currentPage + 1}&pageSize=${pagination.pageSize}${not empty searchTerm ? '&search='.concat(searchTerm) : ''}"
										class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50">
										${pagination.currentPage + 1}
									</a>
									<span class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-700">
										...
									</span>
									<a href="${paginationUrl}?page=${pagination.totalPages}&pageSize=${pagination.pageSize}${not empty searchTerm ? '&search='.concat(searchTerm) : ''}"
										class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50">
										${pagination.totalPages}
									</a>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
					
					<!-- Next Page -->
					<c:if test="${pagination.hasNext}">
						<a href="${paginationUrl}?page=${pagination.currentPage + 1}&pageSize=${pagination.pageSize}${not empty searchTerm ? '&search='.concat(searchTerm) : ''}"
							class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50">
							<span class="sr-only">Next</span>
							<i class="fas fa-chevron-right"></i>
						</a>
					</c:if>
				</nav>
			</div>
		</div>
	</div>
</c:if>
