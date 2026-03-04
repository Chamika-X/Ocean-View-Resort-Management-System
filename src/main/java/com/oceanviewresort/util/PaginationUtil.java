package com.oceanviewresort.util;

import java.util.List;

/**
 * Utility class for handling pagination logic across the application. Provides
 * methods to calculate pagination parameters and create pagination data.
 */
public class PaginationUtil {

	/**
	 * Default page size for pagination
	 */
	public static final int DEFAULT_PAGE_SIZE = 10;

	/**
	 * Maximum page size allowed
	 */
	public static final int MAX_PAGE_SIZE = 100;

	/**
	 * Pagination data holder class
	 */
	public static class PaginationData {
		private List<?> items;
		private int currentPage;
		private int pageSize;
		private int totalItems;
		private int totalPages;
		private int startItem;
		private int endItem;
		private boolean hasNext;
		private boolean hasPrevious;

		public PaginationData(List<?> items, int currentPage, int pageSize, int totalItems) {
			this.items = items;
			this.currentPage = currentPage;
			this.pageSize = pageSize;
			this.totalItems = totalItems;
			this.totalPages = (int) Math.ceil((double) totalItems / pageSize);
			this.startItem = (currentPage - 1) * pageSize + 1;
			this.endItem = Math.min(currentPage * pageSize, totalItems);
			this.hasNext = currentPage < totalPages;
			this.hasPrevious = currentPage > 1;
		}

		// Getters
		public List<?> getItems() {
			return items;
		}

		public int getCurrentPage() {
			return currentPage;
		}

		public int getPageSize() {
			return pageSize;
		}

		public int getTotalItems() {
			return totalItems;
		}

		public int getTotalPages() {
			return totalPages;
		}

		public int getStartItem() {
			return startItem;
		}

		public int getEndItem() {
			return endItem;
		}

		public boolean getHasNext() {
			return hasNext;
		}

		public boolean getHasPrevious() {
			return hasPrevious;
		}
	}

	/**
	 * Parse and validate page number from request parameter
	 * 
	 * @param pageParam The page parameter from request
	 * @return Valid page number (1 if invalid or null)
	 */
	public static int parsePageNumber(String pageParam) {
		if (pageParam == null || pageParam.trim().isEmpty()) {
			return 1;
		}

		try {
			int page = Integer.parseInt(pageParam.trim());
			return page > 0 ? page : 1;
		} catch (NumberFormatException e) {
			return 1;
		}
	}

	/**
	 * Parse and validate page size from request parameter
	 * 
	 * @param pageSizeParam The page size parameter from request
	 * @return Valid page size (default if invalid or null)
	 */
	public static int parsePageSize(String pageSizeParam) {
		if (pageSizeParam == null || pageSizeParam.trim().isEmpty()) {
			return DEFAULT_PAGE_SIZE;
		}

		try {
			int pageSize = Integer.parseInt(pageSizeParam.trim());
			if (pageSize > 0 && pageSize <= MAX_PAGE_SIZE) {
				return pageSize;
			}
			return DEFAULT_PAGE_SIZE;
		} catch (NumberFormatException e) {
			return DEFAULT_PAGE_SIZE;
		}
	}

	/**
	 * Calculate the offset for SQL LIMIT clause
	 * 
	 * @param page     The current page number
	 * @param pageSize The page size
	 * @return The offset value
	 */
	public static int calculateOffset(int page, int pageSize) {
		return (page - 1) * pageSize;
	}

	/**
	 * Create pagination data object
	 * 
	 * @param items       The items for current page
	 * @param currentPage Current page number
	 * @param pageSize    Page size
	 * @param totalItems  Total number of items
	 * @return PaginationData object
	 */
	public static PaginationData createPaginationData(List<?> items, int currentPage, int pageSize, int totalItems) {
		return new PaginationData(items, currentPage, pageSize, totalItems);
	}

	/**
	 * Generate pagination URL with parameters
	 * 
	 * @param baseUrl     The base URL
	 * @param page        The page number
	 * @param pageSize    The page size
	 * @param searchTerm  The search term (can be null)
	 * @param otherParams Additional parameters as key-value pairs
	 * @return Formatted URL with pagination parameters
	 */
	public static String buildPaginationUrl(String baseUrl, int page, int pageSize, String searchTerm,
			String... otherParams) {
		StringBuilder url = new StringBuilder(baseUrl);
		url.append("?page=").append(page);
		url.append("&pageSize=").append(pageSize);

		if (searchTerm != null && !searchTerm.trim().isEmpty()) {
			url.append("&search=")
					.append(java.net.URLEncoder.encode(searchTerm.trim(), java.nio.charset.StandardCharsets.UTF_8));
		}

		// Add other parameters
		if (otherParams != null && otherParams.length >= 2) {
			for (int i = 0; i < otherParams.length; i += 2) {
				if (i + 1 < otherParams.length && otherParams[i] != null && otherParams[i + 1] != null) {
					url.append("&").append(otherParams[i]).append("=").append(
							java.net.URLEncoder.encode(otherParams[i + 1], java.nio.charset.StandardCharsets.UTF_8));
				}
			}
		}

		return url.toString();
	}
}
