package com.oceanviewresort.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oceanviewresort.model.Reservation;
import com.oceanviewresort.model.Room;
import com.oceanviewresort.service.GuestService;
import com.oceanviewresort.service.RoomService;
import com.oceanviewresort.service.ReservationService;
import com.oceanviewresort.service.BillService;
import com.oceanviewresort.service.UserService;
import com.oceanviewresort.service.ActivityService;
import com.oceanviewresort.model.Activity;

/**
 * Controller for the Dashboard at Ocean View Resort.
 * Displays overview statistics and recent activities.
 */
@WebServlet("/dashboard")
public class DashboardController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private GuestService guestService;
    private RoomService roomService;
    private ReservationService reservationService;
    private BillService billService;
    private UserService userService;
    private ActivityService activityService;

    /**
     * Initializes the controller
     */
    public void init() throws ServletException {
        guestService = GuestService.getInstance();
        roomService = RoomService.getInstance();
        reservationService = ReservationService.getInstance();
        billService = BillService.getInstance();
        userService = UserService.getInstance();
        activityService = new ActivityService();
    }

    /**
     * Handles GET requests for the dashboard
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("auth?action=login");
            return;
        }

        // Handle help action
        String action = request.getParameter("action");
        if ("help".equals(action)) {
            request.getRequestDispatcher("WEB-INF/view/help.jsp").forward(request, response);
            return;
        }

        try {
            // Get statistics
            int totalGuests = guestService.getGuestCount();
            int totalRooms = roomService.getRoomCount();
            int totalReservations = reservationService.getReservationCount();
            int totalUsers = userService.getUserCount();

            // Get room availability stats
            int availableRooms = roomService.getRoomCountByStatus(Room.STATUS_AVAILABLE);
            int occupiedRooms = roomService.getRoomCountByStatus(Room.STATUS_OCCUPIED);
            int reservedRooms = roomService.getRoomCountByStatus(Room.STATUS_RESERVED);

            // Get reservation stats
            int confirmedReservations = reservationService.getReservationCountByStatus(Reservation.STATUS_CONFIRMED);
            int checkedInReservations = reservationService.getReservationCountByStatus(Reservation.STATUS_CHECKED_IN);

            // Get today's check-ins and check-outs
            List<Reservation> todayCheckIns = reservationService.getTodayCheckIns();
            List<Reservation> todayCheckOuts = reservationService.getTodayCheckOuts();

            // Get today's revenue
            BigDecimal todayRevenue = billService.getTodayRevenue();

            // Get monthly revenue (current month)
            LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
            LocalDate endOfMonth = LocalDate.now();
            BigDecimal monthlyRevenue = billService.getRevenueForDateRange(startOfMonth, endOfMonth);

            // Get recent activities (admin only)
            String role = (String) session.getAttribute("role");
            List<Activity> recentActivities = null;
            if ("admin".equals(role)) {
                recentActivities = activityService.getRecentActivities(10);
            }

            // Get all rooms for the room grid display
            List<Room> allRooms = roomService.getAllRooms();

            // Build a map of room ID -> active reservation for occupied/reserved rooms
            Map<Integer, Reservation> roomReservationMap = new HashMap<>();
            List<Reservation> checkedInList = reservationService.getReservationsByStatus(Reservation.STATUS_CHECKED_IN);
            List<Reservation> confirmedList = reservationService.getReservationsByStatus(Reservation.STATUS_CONFIRMED);
            if (checkedInList != null) {
                for (Reservation res : checkedInList) {
                    if (res.getRoom() != null) {
                        roomReservationMap.put(res.getRoom().getRoomId(), res);
                    }
                }
            }
            if (confirmedList != null) {
                for (Reservation res : confirmedList) {
                    if (res.getRoom() != null && !roomReservationMap.containsKey(res.getRoom().getRoomId())) {
                        roomReservationMap.put(res.getRoom().getRoomId(), res);
                    }
                }
            }

            // Set attributes for JSP
            request.setAttribute("totalGuests", totalGuests);
            request.setAttribute("totalRooms", totalRooms);
            request.setAttribute("totalReservations", totalReservations);
            request.setAttribute("totalUsers", totalUsers);

            request.setAttribute("availableRooms", availableRooms);
            request.setAttribute("occupiedRooms", occupiedRooms);
            request.setAttribute("reservedRooms", reservedRooms);

            request.setAttribute("confirmedReservations", confirmedReservations);
            request.setAttribute("checkedInReservations", checkedInReservations);

            request.setAttribute("todayCheckIns", todayCheckIns);
            request.setAttribute("todayCheckOuts", todayCheckOuts);
            request.setAttribute("todayCheckInCount", todayCheckIns.size());
            request.setAttribute("todayCheckOutCount", todayCheckOuts.size());

            request.setAttribute("todayRevenue", todayRevenue);
            request.setAttribute("monthlyRevenue", monthlyRevenue);

            request.setAttribute("recentActivities", recentActivities);

            request.setAttribute("allRooms", allRooms);
            request.setAttribute("roomReservationMap", roomReservationMap);

        } catch (SQLException e) {
            request.setAttribute("error", "Error loading dashboard data: " + e.getMessage());
        }

        request.getRequestDispatcher("WEB-INF/view/dashboard.jsp").forward(request, response);
    }
}