package com.oceanviewresort.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oceanviewresort.model.Reservation;
import com.oceanviewresort.model.Guest;
import com.oceanviewresort.model.Room;
import com.oceanviewresort.model.RoomType;
import com.oceanviewresort.model.Bill;
import com.oceanviewresort.service.ReservationService;
import com.oceanviewresort.service.GuestService;
import com.oceanviewresort.service.RoomService;
import com.oceanviewresort.service.BillService;
import com.oceanviewresort.service.ActivityService;
import com.oceanviewresort.validation.ValidationUtils;
import com.oceanviewresort.util.PaginationUtil;
import com.oceanviewresort.util.PaginationUtil.PaginationData;

/**
 * Controller for handling reservation-related requests at Ocean View Resort.
 * Manages reservations, check-in, check-out, and billing.
 */
@WebServlet("/reservation")
public class ReservationController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private ReservationService reservationService;
    private GuestService guestService;
    private RoomService roomService;
    private BillService billService;
    private ActivityService activityService;

    public void init() throws ServletException {
        reservationService = ReservationService.getInstance();
        guestService = GuestService.getInstance();
        roomService = RoomService.getInstance();
        billService = BillService.getInstance();
        activityService = new ActivityService();

        try {
            activityService.initialize();
        } catch (Exception e) {
            System.err.println("Failed to initialize activity system: " + e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("auth?action=login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null || action.equals("list")) {
            listReservations(request, response);
        } else if (action.equals("add")) {
            showAddForm(request, response);
        } else if (action.equals("edit")) {
            showEditForm(request, response);
        } else if (action.equals("view")) {
            viewReservation(request, response);
        } else if (action.equals("checkin")) {
            showCheckInForm(request, response);
        } else if (action.equals("checkout")) {
            showCheckOutForm(request, response);
        } else if (action.equals("bill")) {
            viewBill(request, response);
        } else if (action.equals("printBill")) {
            printBill(request, response);
        } else if (action.equals("delete")) {
            deleteReservation(request, response);
        } else if (action.equals("search")) {
            searchReservation(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("auth?action=login");
            return;
        }

        String action = request.getParameter("action");
        if (action.equals("add")) {
            addReservation(request, response);
        } else if (action.equals("update")) {
            updateReservation(request, response);
        } else if (action.equals("checkin")) {
            processCheckIn(request, response);
        } else if (action.equals("checkout")) {
            processCheckOut(request, response);
        } else if (action.equals("cancel")) {
            cancelReservation(request, response);
        } else if (action.equals("processPayment")) {
            processPayment(request, response);
        }
    }

    private void listReservations(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int page = PaginationUtil.parsePageNumber(request.getParameter("page"));
            int pageSize = PaginationUtil.parsePageSize(request.getParameter("pageSize"));
            String searchTerm = request.getParameter("search");
            String statusFilter = request.getParameter("status");

            List<Reservation> reservationList;
            int totalItems;

            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                int offset = PaginationUtil.calculateOffset(page, pageSize);
                reservationList = reservationService.searchReservationsPaginated(searchTerm.trim(), offset, pageSize);
                totalItems = reservationService.getReservationSearchCount(searchTerm.trim());
            } else if (statusFilter != null && !statusFilter.trim().isEmpty()) {
                reservationList = reservationService.getReservationsByStatus(statusFilter);
                totalItems = reservationList.size();
            } else {
                int offset = PaginationUtil.calculateOffset(page, pageSize);
                reservationList = reservationService.getReservationsPaginated(offset, pageSize);
                totalItems = reservationService.getReservationCount();
            }

            PaginationData paginationData = PaginationUtil.createPaginationData(reservationList, page, pageSize, totalItems);

            request.setAttribute("reservations", reservationList);
            request.setAttribute("pagination", paginationData);
            request.setAttribute("searchTerm", searchTerm != null ? searchTerm.trim() : "");
            request.setAttribute("statusFilter", statusFilter);

        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("WEB-INF/view/reservation/listReservations.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Guest> guests = guestService.getAllGuests();
            List<Room> availableRooms = roomService.getAvailableRooms();
            List<RoomType> roomTypes = roomService.getAllRoomTypes();

            request.setAttribute("guests", guests);
            request.setAttribute("availableRooms", availableRooms);
            request.setAttribute("roomTypes", roomTypes);
            request.setAttribute("minDate", LocalDate.now().format(DATE_FORMATTER));

        } catch (SQLException e) {
            request.setAttribute("error", "Failed to load form data: " + e.getMessage());
        }

        request.getRequestDispatcher("WEB-INF/view/reservation/addReservation.jsp").forward(request, response);
    }

    private void addReservation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        int userId = session != null ? (Integer) session.getAttribute("userId") : 0;
        String username = session != null ? (String) session.getAttribute("username") : "system";

        String guestIdStr = request.getParameter("guestId");
        String roomIdStr = request.getParameter("roomId");
        String checkInDateStr = request.getParameter("checkInDate");
        String checkOutDateStr = request.getParameter("checkOutDate");
        String numberOfGuestsStr = request.getParameter("numberOfGuests");
        String advancePaymentStr = request.getParameter("advancePayment");
        String specialRequests = ValidationUtils.sanitizeString(request.getParameter("specialRequests"));

        try {
            int guestId = Integer.parseInt(guestIdStr);
            int roomId = Integer.parseInt(roomIdStr);
            LocalDate checkInDate = LocalDate.parse(checkInDateStr, DATE_FORMATTER);
            LocalDate checkOutDate = LocalDate.parse(checkOutDateStr, DATE_FORMATTER);
            int numberOfGuests = Integer.parseInt(numberOfGuestsStr);
            BigDecimal advancePayment = new BigDecimal(advancePaymentStr != null && !advancePaymentStr.isEmpty() ? advancePaymentStr : "0");

            Map<String, String> validationErrors = ValidationUtils.validateReservation(guestId, roomId, 
                    checkInDate, checkOutDate, numberOfGuests);

            if (!validationErrors.isEmpty()) {
                request.setAttribute("fieldErrors", validationErrors);
                loadAddFormData(request);
                request.getRequestDispatcher("WEB-INF/view/reservation/addReservation.jsp").forward(request, response);
                return;
            }

            Reservation reservation = new Reservation(guestId, roomId, checkInDate, checkOutDate);
            reservation.setNumberOfGuests(numberOfGuests);
            reservation.setAdvancePayment(advancePayment);
            reservation.setSpecialRequests(specialRequests);
            reservation.setCreatedBy(userId);

            int reservationId = reservationService.createReservation(reservation);

            if (reservationId > 0) {
                Guest guest = guestService.getGuestById(guestId);
                try {
                    activityService.logReservationCreated(reservationId, reservation.getReservationNumber(), 
                            guest != null ? guest.getGuestName() : "Unknown", username);
                } catch (Exception e) {
                    System.err.println("Failed to log reservation activity: " + e.getMessage());
                }

                response.sendRedirect("reservation?action=view&id=" + reservationId + "&success=Reservation created successfully");
            } else {
                request.setAttribute("error", "Failed to create reservation");
                loadAddFormData(request);
                request.getRequestDispatcher("WEB-INF/view/reservation/addReservation.jsp").forward(request, response);
            }
        } catch (NumberFormatException | DateTimeParseException e) {
            request.setAttribute("error", "Invalid input format");
            loadAddFormData(request);
            request.getRequestDispatcher("WEB-INF/view/reservation/addReservation.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            loadAddFormData(request);
            request.getRequestDispatcher("WEB-INF/view/reservation/addReservation.jsp").forward(request, response);
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String reservationIdStr = request.getParameter("id");

        try {
            int reservationId = Integer.parseInt(reservationIdStr);
            Reservation reservation = reservationService.getReservationById(reservationId);

            if (reservation == null) {
                request.setAttribute("errorMessage", "Reservation not found");
                request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
                return;
            }

            List<Guest> guests = guestService.getAllGuests();
            List<Room> rooms = roomService.getAllRooms();
            List<RoomType> roomTypes = roomService.getAllRoomTypes();

            request.setAttribute("reservation", reservation);
            request.setAttribute("guests", guests);
            request.setAttribute("rooms", rooms);
            request.setAttribute("roomTypes", roomTypes);

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid Reservation ID");
            request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
        }

        request.getRequestDispatcher("WEB-INF/view/reservation/editReservation.jsp").forward(request, response);
    }

    private void updateReservation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String reservationIdStr = request.getParameter("reservationId");
        String username = (String) request.getSession().getAttribute("username");

        try {
            int reservationId = Integer.parseInt(reservationIdStr);
            Reservation reservation = reservationService.getReservationById(reservationId);

            if (reservation == null) {
                response.sendRedirect("reservation?action=list&error=Reservation not found");
                return;
            }

            int roomId = Integer.parseInt(request.getParameter("roomId"));
            LocalDate checkInDate = LocalDate.parse(request.getParameter("checkInDate"), DATE_FORMATTER);
            LocalDate checkOutDate = LocalDate.parse(request.getParameter("checkOutDate"), DATE_FORMATTER);
            int numberOfGuests = Integer.parseInt(request.getParameter("numberOfGuests"));
            String specialRequests = ValidationUtils.sanitizeString(request.getParameter("specialRequests"));

            reservation.setRoomId(roomId);
            reservation.setCheckInDate(checkInDate);
            reservation.setCheckOutDate(checkOutDate);
            reservation.setNumberOfGuests(numberOfGuests);
            reservation.setSpecialRequests(specialRequests);

            boolean success = reservationService.updateReservation(reservation);

            if (success) {
                try {
                    activityService.logReservationUpdated(reservationId, reservation.getReservationNumber(), username);
                } catch (Exception e) {
                    System.err.println("Failed to log reservation update: " + e.getMessage());
                }
                response.sendRedirect("reservation?action=view&id=" + reservationId + "&success=Reservation updated");
            } else {
                response.sendRedirect("reservation?action=edit&id=" + reservationId + "&error=Failed to update");
            }
        } catch (Exception e) {
            response.sendRedirect("reservation?action=list&error=" + e.getMessage());
        }
    }

    private void viewReservation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String reservationIdStr = request.getParameter("id");

        try {
            int reservationId = Integer.parseInt(reservationIdStr);
            Reservation reservation = reservationService.getReservationById(reservationId);

            if (reservation == null) {
                request.setAttribute("errorMessage", "Reservation not found");
                request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
                return;
            }

            // Check if bill exists
            Bill bill = billService.getBillByReservationId(reservationId);

            request.setAttribute("reservation", reservation);
            request.setAttribute("bill", bill);

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid Reservation ID");
            request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
            return;
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("WEB-INF/view/reservation/viewReservation.jsp").forward(request, response);
    }

    private void showCheckInForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String reservationIdStr = request.getParameter("id");

        try {
            int reservationId = Integer.parseInt(reservationIdStr);
            Reservation reservation = reservationService.getReservationById(reservationId);

            if (reservation == null) {
                request.setAttribute("errorMessage", "Reservation not found");
                request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
                return;
            }

            request.setAttribute("reservation", reservation);

        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("WEB-INF/view/reservation/checkIn.jsp").forward(request, response);
    }

    private void processCheckIn(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String reservationIdStr = request.getParameter("reservationId");
        String username = (String) request.getSession().getAttribute("username");

        try {
            int reservationId = Integer.parseInt(reservationIdStr);
            Reservation reservation = reservationService.getReservationById(reservationId);

            if (reservation == null) {
                response.sendRedirect("reservation?action=list&error=Reservation not found");
                return;
            }

            boolean success = reservationService.checkIn(reservationId);

            if (success) {
                try {
                    activityService.logCheckIn(reservationId, reservation.getReservationNumber(),
                            reservation.getGuest().getGuestName(), reservation.getRoom().getRoomNumber(), username);
                } catch (Exception e) {
                    System.err.println("Failed to log check-in: " + e.getMessage());
                }
                response.sendRedirect("reservation?action=view&id=" + reservationId + "&success=Guest checked in successfully");
            } else {
                response.sendRedirect("reservation?action=view&id=" + reservationId + "&error=Check-in failed");
            }
        } catch (Exception e) {
            response.sendRedirect("reservation?action=list&error=" + e.getMessage());
        }
    }

    private void showCheckOutForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String reservationIdStr = request.getParameter("id");

        try {
            int reservationId = Integer.parseInt(reservationIdStr);
            Reservation reservation = reservationService.getReservationById(reservationId);

            if (reservation == null) {
                request.setAttribute("errorMessage", "Reservation not found");
                request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
                return;
            }

            // Calculate bill preview
            Bill billPreview = billService.calculateBillPreview(reservation, BigDecimal.ZERO, BigDecimal.ZERO);

            request.setAttribute("reservation", reservation);
            request.setAttribute("billPreview", billPreview);

        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("WEB-INF/view/reservation/checkOut.jsp").forward(request, response);
    }

    private void processCheckOut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String reservationIdStr = request.getParameter("reservationId");
        String additionalChargesStr = request.getParameter("additionalCharges");
        String discountAmountStr = request.getParameter("discountAmount");
        String username = (String) request.getSession().getAttribute("username");

        try {
            int reservationId = Integer.parseInt(reservationIdStr);
            BigDecimal additionalCharges = new BigDecimal(additionalChargesStr != null && !additionalChargesStr.isEmpty() ? additionalChargesStr : "0");
            BigDecimal discountAmount = new BigDecimal(discountAmountStr != null && !discountAmountStr.isEmpty() ? discountAmountStr : "0");

            Reservation reservation = reservationService.getReservationById(reservationId);

            if (reservation == null) {
                response.sendRedirect("reservation?action=list&error=Reservation not found");
                return;
            }

            Bill bill = reservationService.checkOut(reservationId, additionalCharges, discountAmount);

            if (bill != null) {
                try {
                    activityService.logCheckOut(reservationId, reservation.getReservationNumber(),
                            reservation.getGuest().getGuestName(), reservation.getRoom().getRoomNumber(), username);
                    activityService.logBillGenerated(bill.getBillId(), bill.getBillNumber(),
                            reservation.getGuest().getGuestName(), username);
                } catch (Exception e) {
                    System.err.println("Failed to log check-out: " + e.getMessage());
                }
                response.sendRedirect("reservation?action=bill&id=" + reservationId + "&success=Check-out completed. Bill generated.");
            } else {
                response.sendRedirect("reservation?action=view&id=" + reservationId + "&error=Check-out failed");
            }
        } catch (Exception e) {
            response.sendRedirect("reservation?action=list&error=" + e.getMessage());
        }
    }

    private void viewBill(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String reservationIdStr = request.getParameter("id");

        try {
            int reservationId = Integer.parseInt(reservationIdStr);
            Reservation reservation = reservationService.getReservationById(reservationId);
            Bill bill = billService.getBillByReservationId(reservationId);

            if (reservation == null) {
                request.setAttribute("errorMessage", "Reservation not found");
                request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
                return;
            }

            if (bill == null) {
                request.setAttribute("errorMessage", "Bill not found for this reservation");
                request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
                return;
            }

            bill.setReservation(reservation);
            request.setAttribute("reservation", reservation);
            request.setAttribute("bill", bill);

        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("WEB-INF/view/reservation/viewBill.jsp").forward(request, response);
    }

    private void printBill(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String reservationIdStr = request.getParameter("id");

        try {
            int reservationId = Integer.parseInt(reservationIdStr);
            Reservation reservation = reservationService.getReservationById(reservationId);
            Bill bill = billService.getBillByReservationId(reservationId);

            if (bill == null) {
                request.setAttribute("errorMessage", "Bill not found");
                request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
                return;
            }

            bill.setReservation(reservation);
            request.setAttribute("reservation", reservation);
            request.setAttribute("bill", bill);

        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("WEB-INF/view/reservation/printBill.jsp").forward(request, response);
    }

    private void processPayment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String billIdStr = request.getParameter("billId");
        String paymentMethod = request.getParameter("paymentMethod");
        String username = (String) request.getSession().getAttribute("username");

        try {
            int billId = Integer.parseInt(billIdStr);
            Bill bill = billService.getBillById(billId);

            if (bill == null) {
                response.sendRedirect("reservation?action=list&error=Bill not found");
                return;
            }

            boolean success = billService.processPayment(billId, paymentMethod);

            if (success) {
                try {
                    activityService.logBillPaid(billId, bill.getBillNumber(), paymentMethod, username);
                } catch (Exception e) {
                    System.err.println("Failed to log payment: " + e.getMessage());
                }
                response.sendRedirect("reservation?action=bill&id=" + bill.getReservationId() + "&success=Payment processed successfully");
            } else {
                response.sendRedirect("reservation?action=bill&id=" + bill.getReservationId() + "&error=Payment failed");
            }
        } catch (Exception e) {
            response.sendRedirect("reservation?action=list&error=" + e.getMessage());
        }
    }

    private void cancelReservation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String reservationIdStr = request.getParameter("reservationId");
        String username = (String) request.getSession().getAttribute("username");

        try {
            int reservationId = Integer.parseInt(reservationIdStr);
            Reservation reservation = reservationService.getReservationById(reservationId);

            if (reservation == null) {
                response.sendRedirect("reservation?action=list&error=Reservation not found");
                return;
            }

            boolean success = reservationService.cancelReservation(reservationId);

            if (success) {
                try {
                    activityService.logReservationCancelled(reservationId, reservation.getReservationNumber(), username);
                } catch (Exception e) {
                    System.err.println("Failed to log cancellation: " + e.getMessage());
                }
                response.sendRedirect("reservation?action=list&success=Reservation cancelled");
            } else {
                response.sendRedirect("reservation?action=view&id=" + reservationId + "&error=Cancellation failed");
            }
        } catch (Exception e) {
            response.sendRedirect("reservation?action=list&error=" + e.getMessage());
        }
    }

    /**
     * Deletes a reservation permanently
     */
    private void deleteReservation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String reservationIdStr = request.getParameter("id");
        String username = (String) request.getSession().getAttribute("username");

        try {
            int reservationId = Integer.parseInt(reservationIdStr);
            Reservation reservation = reservationService.getReservationById(reservationId);

            if (reservation == null) {
                response.sendRedirect("reservation?action=list&error=Reservation+not+found");
                return;
            }

            // Delete associated bill first (foreign key constraint)
            try {
                Bill bill = billService.getBillByReservationId(reservationId);
                if (bill != null) {
                    billService.deleteBill(bill.getBillId());
                }
            } catch (Exception e) {
                System.err.println("Failed to delete associated bill: " + e.getMessage());
            }

            boolean success = reservationService.deleteReservation(reservationId);

            if (success) {
                try {
                    activityService.logReservationCancelled(reservationId, reservation.getReservationNumber(), username);
                } catch (Exception e) {
                    System.err.println("Failed to log reservation deletion activity: " + e.getMessage());
                }
                response.sendRedirect("reservation?action=list&success=Reservation+deleted+successfully");
            } else {
                response.sendRedirect("reservation?action=list&error=Failed+to+delete+reservation");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("reservation?action=list&error=Invalid+Reservation+ID");
        } catch (Exception e) {
            response.sendRedirect("reservation?action=list&error=Database+error");
        }
    }

    private void searchReservation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchTerm = request.getParameter("term");

        try {
            // First try to find by reservation number
            Reservation reservation = reservationService.getReservationByNumber(searchTerm);

            if (reservation != null) {
                response.sendRedirect("reservation?action=view&id=" + reservation.getReservationId());
                return;
            }

            // Otherwise do a general search
            List<Reservation> results = reservationService.searchReservations(searchTerm);
            request.setAttribute("reservations", results);
            request.setAttribute("searchTerm", searchTerm);

        } catch (SQLException e) {
            request.setAttribute("error", "Search failed: " + e.getMessage());
        }

        request.getRequestDispatcher("WEB-INF/view/reservation/listReservations.jsp").forward(request, response);
    }

    private void loadAddFormData(HttpServletRequest request) {
        try {
            List<Guest> guests = guestService.getAllGuests();
            List<Room> availableRooms = roomService.getAvailableRooms();
            List<RoomType> roomTypes = roomService.getAllRoomTypes();

            request.setAttribute("guests", guests);
            request.setAttribute("availableRooms", availableRooms);
            request.setAttribute("roomTypes", roomTypes);
            request.setAttribute("minDate", LocalDate.now().format(DATE_FORMATTER));
        } catch (SQLException e) {
            System.err.println("Failed to load form data: " + e.getMessage());
        }
    }
}
