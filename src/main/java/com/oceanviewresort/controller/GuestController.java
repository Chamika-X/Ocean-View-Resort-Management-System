package com.oceanviewresort.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oceanviewresort.model.Guest;
import com.oceanviewresort.service.GuestService;
import com.oceanviewresort.service.ActivityService;
import com.oceanviewresort.validation.ValidationUtils;
import com.oceanviewresort.util.PaginationUtil;
import com.oceanviewresort.util.PaginationUtil.PaginationData;

/**
 * Controller for handling guest-related requests at Ocean View Resort.
 * Manages guest registration, listing, editing, and deletion.
 */
@WebServlet("/guest")
public class GuestController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private GuestService guestService;
    private ActivityService activityService;

    /**
     * Initializes the controller
     */
    public void init() throws ServletException {
        guestService = GuestService.getInstance();
        activityService = new ActivityService();

        try {
            activityService.initialize();
        } catch (Exception e) {
            System.err.println("Failed to initialize activity system: " + e.getMessage());
        }
    }

    /**
     * Handles GET requests for guest management
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("auth?action=login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null || action.equals("list")) {
            listGuests(request, response);
        } else if (action.equals("add")) {
            showAddForm(request, response);
        } else if (action.equals("edit")) {
            showEditForm(request, response);
        } else if (action.equals("delete")) {
            deleteGuest(request, response);
        } else if (action.equals("view")) {
            viewGuest(request, response);
        } else if (action.equals("search")) {
            searchGuests(request, response);
        }
    }

    /**
     * Handles POST requests for guest management
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("auth?action=login");
            return;
        }

        String action = request.getParameter("action");
        if (action.equals("add")) {
            addGuest(request, response);
        } else if (action.equals("update")) {
            updateGuest(request, response);
        }
    }

    /**
     * Lists all guests with pagination
     */
    private void listGuests(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int page = PaginationUtil.parsePageNumber(request.getParameter("page"));
            int pageSize = PaginationUtil.parsePageSize(request.getParameter("pageSize"));
            String searchTerm = request.getParameter("search");

            List<Guest> guestList;
            int totalItems;

            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                int offset = PaginationUtil.calculateOffset(page, pageSize);
                guestList = guestService.searchGuestsPaginated(searchTerm.trim(), offset, pageSize);
                totalItems = guestService.getGuestSearchCount(searchTerm.trim());
            } else {
                int offset = PaginationUtil.calculateOffset(page, pageSize);
                guestList = guestService.getGuestsPaginated(offset, pageSize);
                totalItems = guestService.getGuestCount();
            }

            PaginationData paginationData = PaginationUtil.createPaginationData(guestList, page, pageSize, totalItems);

            request.setAttribute("guests", guestList);
            request.setAttribute("pagination", paginationData);
            request.setAttribute("searchTerm", searchTerm != null ? searchTerm.trim() : "");

        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("WEB-INF/view/guest/listGuests.jsp").forward(request, response);
    }

    /**
     * Shows the form for adding a new guest
     */
    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/view/guest/addGuest.jsp").forward(request, response);
    }

    /**
     * Processes the addition of a new guest
     */
    private void addGuest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String guestName = ValidationUtils.sanitizeString(request.getParameter("guestName"));
        String address = ValidationUtils.sanitizeString(request.getParameter("address"));
        String contactNumber = ValidationUtils.sanitizeString(request.getParameter("contactNumber"));
        String email = ValidationUtils.sanitizeString(request.getParameter("email"));
        String nicPassport = ValidationUtils.sanitizeString(request.getParameter("nicPassport"));
        String nationality = ValidationUtils.sanitizeString(request.getParameter("nationality"));

        // Validate input
        Map<String, String> validationErrors = ValidationUtils.validateGuest(guestName, address, 
                contactNumber, email, nicPassport);

        if (!validationErrors.isEmpty()) {
            request.setAttribute("fieldErrors", validationErrors);
            setGuestFormAttributes(request, guestName, address, contactNumber, email, nicPassport, nationality);
            request.getRequestDispatcher("WEB-INF/view/guest/addGuest.jsp").forward(request, response);
            return;
        }

        Guest guest = new Guest();
        guest.setGuestName(guestName);
        guest.setAddress(address);
        guest.setContactNumber(contactNumber);
        guest.setEmail(email);
        guest.setNicPassport(nicPassport);
        guest.setNationality(nationality != null && !nationality.isEmpty() ? nationality : "Sri Lankan");

        try {
            int guestId = guestService.addGuest(guest);

            if (guestId > 0) {
                // Log activity
                HttpSession session = request.getSession(false);
                String username = session != null ? (String) session.getAttribute("username") : "system";
                try {
                    activityService.logGuestAdded(guestId, guestName, username);
                } catch (Exception e) {
                    System.err.println("Failed to log guest addition activity: " + e.getMessage());
                }

                response.sendRedirect("guest?action=list&success=" + URLEncoder.encode("Guest registered successfully", "UTF-8"));
            } else {
                request.setAttribute("error", "Failed to register guest");
                setGuestFormAttributes(request, guestName, address, contactNumber, email, nicPassport, nationality);
                request.getRequestDispatcher("WEB-INF/view/guest/addGuest.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            setGuestFormAttributes(request, guestName, address, contactNumber, email, nicPassport, nationality);
            request.getRequestDispatcher("WEB-INF/view/guest/addGuest.jsp").forward(request, response);
        }
    }

    /**
     * Shows the form for editing a guest
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String guestIdStr = request.getParameter("id");
        if (guestIdStr == null || guestIdStr.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Guest ID is required");
            request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
            return;
        }

        try {
            int guestId = Integer.parseInt(guestIdStr);
            Guest guest = guestService.getGuestById(guestId);

            if (guest == null) {
                request.setAttribute("errorMessage", "Guest not found");
                request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
                return;
            }

            request.setAttribute("guest", guest);
            request.getRequestDispatcher("WEB-INF/view/guest/editGuest.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid Guest ID");
            request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
        }
    }

    /**
     * Updates a guest
     */
    private void updateGuest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String guestIdStr = request.getParameter("guestId");
        String guestName = ValidationUtils.sanitizeString(request.getParameter("guestName"));
        String address = ValidationUtils.sanitizeString(request.getParameter("address"));
        String contactNumber = ValidationUtils.sanitizeString(request.getParameter("contactNumber"));
        String email = ValidationUtils.sanitizeString(request.getParameter("email"));
        String nicPassport = ValidationUtils.sanitizeString(request.getParameter("nicPassport"));
        String nationality = ValidationUtils.sanitizeString(request.getParameter("nationality"));

        // Validate input
        Map<String, String> validationErrors = ValidationUtils.validateGuest(guestName, address, 
                contactNumber, email, nicPassport);

        if (!validationErrors.isEmpty()) {
            request.setAttribute("fieldErrors", validationErrors);
            try {
                int guestId = Integer.parseInt(guestIdStr);
                Guest guest = guestService.getGuestById(guestId);
                request.setAttribute("guest", guest);
            } catch (Exception e) {
                // Ignore
            }
            request.getRequestDispatcher("WEB-INF/view/guest/editGuest.jsp").forward(request, response);
            return;
        }

        try {
            int guestId = Integer.parseInt(guestIdStr);
            Guest guest = new Guest();
            guest.setGuestId(guestId);
            guest.setGuestName(guestName);
            guest.setAddress(address);
            guest.setContactNumber(contactNumber);
            guest.setEmail(email);
            guest.setNicPassport(nicPassport);
            guest.setNationality(nationality != null && !nationality.isEmpty() ? nationality : "Sri Lankan");

            boolean success = guestService.updateGuest(guest);

            if (success) {
                HttpSession session = request.getSession(false);
                String username = session != null ? (String) session.getAttribute("username") : "system";
                try {
                    activityService.logGuestUpdated(guestId, guestName, username);
                } catch (Exception e) {
                    System.err.println("Failed to log guest update activity: " + e.getMessage());
                }

                response.sendRedirect("guest?action=list&success=" + URLEncoder.encode("Guest updated successfully", "UTF-8"));
            } else {
                request.setAttribute("error", "Failed to update guest");
                request.setAttribute("guest", guest);
                request.getRequestDispatcher("WEB-INF/view/guest/editGuest.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid Guest ID");
            request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("WEB-INF/view/guest/editGuest.jsp").forward(request, response);
        }
    }

    /**
     * Deletes a guest
     */
    private void deleteGuest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String guestIdStr = request.getParameter("id");

        try {
            int guestId = Integer.parseInt(guestIdStr);
            Guest guest = guestService.getGuestById(guestId);
            
            if (guest == null) {
                response.sendRedirect("guest?action=list&error=" + URLEncoder.encode("Guest not found", "UTF-8"));
                return;
            }

            boolean success = guestService.deleteGuest(guestId);

            if (success) {
                HttpSession session = request.getSession(false);
                String username = session != null ? (String) session.getAttribute("username") : "system";
                try {
                    activityService.logGuestDeleted(guestId, guest.getGuestName(), username);
                } catch (Exception e) {
                    System.err.println("Failed to log guest deletion activity: " + e.getMessage());
                }

                response.sendRedirect("guest?action=list&success=" + URLEncoder.encode("Guest deleted successfully", "UTF-8"));
            } else {
                response.sendRedirect("guest?action=list&error=" + URLEncoder.encode("Failed to delete guest. Guest may have existing reservations.", "UTF-8"));
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("guest?action=list&error=" + URLEncoder.encode("Invalid Guest ID", "UTF-8"));
        } catch (SQLException e) {
            String errorMessage;
            if (e.getMessage() != null && e.getMessage().contains("foreign key constraint")) {
                errorMessage = "Cannot delete this guest because they have existing reservations. Please delete the reservations first.";
            } else {
                errorMessage = "Database error: " + e.getMessage();
            }
            response.sendRedirect("guest?action=list&error=" + URLEncoder.encode(errorMessage, "UTF-8"));
        }
    }

    /**
     * Views a single guest's details
     */
    private void viewGuest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String guestIdStr = request.getParameter("id");

        try {
            int guestId = Integer.parseInt(guestIdStr);
            Guest guest = guestService.getGuestById(guestId);

            if (guest == null) {
                request.setAttribute("errorMessage", "Guest not found");
                request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
                return;
            }

            request.setAttribute("guest", guest);
            request.getRequestDispatcher("WEB-INF/view/guest/viewGuest.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid Guest ID");
            request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
        }
    }

    /**
     * Searches guests (for AJAX calls)
     */
    private void searchGuests(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchTerm = request.getParameter("term");
        
        try {
            List<Guest> guests = guestService.searchGuests(searchTerm);
            request.setAttribute("guests", guests);
            request.setAttribute("searchTerm", searchTerm);
            // For AJAX, could return JSON here
            request.getRequestDispatcher("WEB-INF/view/guest/listGuests.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Search failed: " + e.getMessage());
            request.getRequestDispatcher("WEB-INF/view/guest/listGuests.jsp").forward(request, response);
        }
    }

    /**
     * Helper method to set form attributes for guest data
     */
    private void setGuestFormAttributes(HttpServletRequest request, String guestName, String address,
            String contactNumber, String email, String nicPassport, String nationality) {
        request.setAttribute("guestName", guestName);
        request.setAttribute("address", address);
        request.setAttribute("contactNumber", contactNumber);
        request.setAttribute("email", email);
        request.setAttribute("nicPassport", nicPassport);
        request.setAttribute("nationality", nationality);
    }
}
