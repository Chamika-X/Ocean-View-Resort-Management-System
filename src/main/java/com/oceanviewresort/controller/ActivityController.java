package com.oceanviewresort.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oceanviewresort.model.Activity;
import com.oceanviewresort.service.ActivityService;
import com.oceanviewresort.util.PaginationUtil;
import com.oceanviewresort.util.PaginationUtil.PaginationData;

/**
 * Controller for handling activity viewing and filtering operations.
 */
@WebServlet("/activity")
public class ActivityController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ActivityService activityService;

	/**
	 * Initializes the controller
	 */
	public void init() throws ServletException {
		activityService = new ActivityService();
	}

	/**
	 * Handles GET requests for activity operations
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Check if user is logged in and has admin role
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			response.sendRedirect("auth?action=login");
			return;
		}

		String role = (String) session.getAttribute("role");
		if (!"admin".equals(role)) {
			request.setAttribute("error", "Access denied. Admin privileges required.");
			request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
			return;
		}

		String action = request.getParameter("action");

		if (action == null || action.equals("list")) {
			listActivities(request, response);
		} else {
			listActivities(request, response);
		}
	}

	/**
	 * Lists all activities with optional filters and pagination
	 */
	private void listActivities(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// Parse pagination parameters
			int page = PaginationUtil.parsePageNumber(request.getParameter("page"));
			int pageSize = PaginationUtil.parsePageSize(request.getParameter("pageSize"));
			int offset = PaginationUtil.calculateOffset(page, pageSize);

			// Get filter parameters
			String activityType = request.getParameter("activityType");
			String username = request.getParameter("username");
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			String searchTerm = request.getParameter("search");

			List<Activity> activities;
			int totalItems;

			// Apply filters with pagination
			if (activityType != null && !activityType.trim().isEmpty()) {
				activities = activityService.getActivitiesByType(activityType.trim());
				totalItems = activities.size();
				// Apply pagination to filtered results
				int startIndex = offset;
				int endIndex = Math.min(startIndex + pageSize, activities.size());
				if (startIndex < activities.size()) {
					activities = activities.subList(startIndex, endIndex);
				} else {
					activities = List.of();
				}
			} else if (username != null && !username.trim().isEmpty()) {
				activities = activityService.getActivitiesByUsername(username.trim());
				totalItems = activities.size();
				// Apply pagination to filtered results
				int startIndex = offset;
				int endIndex = Math.min(startIndex + pageSize, activities.size());
				if (startIndex < activities.size()) {
					activities = activities.subList(startIndex, endIndex);
				} else {
					activities = List.of();
				}
			} else if (startDate != null && !startDate.trim().isEmpty() && endDate != null
					&& !endDate.trim().isEmpty()) {
				try {
					LocalDateTime startDateTime = LocalDateTime.parse(startDate + "T00:00:00");
					LocalDateTime endDateTime = LocalDateTime.parse(endDate + "T23:59:59");
					activities = activityService.getActivitiesByDateRange(startDateTime, endDateTime);
					totalItems = activities.size();
					// Apply pagination to filtered results
					int startIndex = offset;
					int endIndex = Math.min(startIndex + pageSize, activities.size());
					if (startIndex < activities.size()) {
						activities = activities.subList(startIndex, endIndex);
					} else {
						activities = List.of();
					}
				} catch (DateTimeParseException e) {
					request.setAttribute("error", "Invalid date format. Please use YYYY-MM-DD format.");
					activities = activityService.getActivitiesPaginated(offset, pageSize);
					totalItems = activityService.getActivityCount();
				}
			} else if (searchTerm != null && !searchTerm.trim().isEmpty()) {
				// Use search without pagination for simplicity
				activities = activityService.searchActivities(searchTerm);
				totalItems = activities.size();
				// Apply pagination to filtered results
				int startIndex = offset;
				int endIndex = Math.min(startIndex + pageSize, activities.size());
				if (startIndex < activities.size()) {
					activities = activities.subList(startIndex, endIndex);
				} else {
					activities = List.of();
				}
			} else {
				// Get paginated activities
				activities = activityService.getActivitiesPaginated(offset, pageSize);
				totalItems = activityService.getActivityCount();
			}

			// Create pagination data
			PaginationData pagination = PaginationUtil.createPaginationData(activities, page, pageSize, totalItems);

			// Set attributes
			request.setAttribute("activities", activities);
			request.setAttribute("pagination", pagination);
			request.setAttribute("filterActivityType", activityType);
			request.setAttribute("filterUsername", username);
			request.setAttribute("filterStartDate", startDate);
			request.setAttribute("filterEndDate", endDate);
			request.setAttribute("filterSearchTerm", searchTerm);

			// Get unique activity types for filter dropdown (Ocean View Resort types)
			List<String> activityTypes = List.of(
					Activity.TYPE_USER_LOGIN, Activity.TYPE_USER_LOGOUT,
					Activity.TYPE_GUEST_ADDED, Activity.TYPE_GUEST_UPDATED, Activity.TYPE_GUEST_DELETED,
					Activity.TYPE_ROOM_ADDED, Activity.TYPE_ROOM_UPDATED, Activity.TYPE_ROOM_STATUS_CHANGED,
					Activity.TYPE_RESERVATION_CREATED, Activity.TYPE_RESERVATION_UPDATED, Activity.TYPE_RESERVATION_CANCELLED,
					Activity.TYPE_CHECK_IN, Activity.TYPE_CHECK_OUT,
					Activity.TYPE_BILL_GENERATED, Activity.TYPE_BILL_PAID,
					Activity.TYPE_SYSTEM_BACKUP);
			request.setAttribute("activityTypes", activityTypes);

			request.getRequestDispatcher("WEB-INF/view/activity/listActivities.jsp").forward(request, response);
		} catch (SQLException e) {
			request.setAttribute("error", "Error loading activities: " + e.getMessage());
			request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
		}
	}
}