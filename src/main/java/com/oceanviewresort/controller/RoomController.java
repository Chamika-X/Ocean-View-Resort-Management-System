package com.oceanviewresort.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oceanviewresort.model.Room;
import com.oceanviewresort.model.RoomType;
import com.oceanviewresort.model.Reservation;
import com.oceanviewresort.service.RoomService;
import com.oceanviewresort.service.ReservationService;
import com.oceanviewresort.service.ActivityService;
import com.oceanviewresort.validation.ValidationUtils;
import com.oceanviewresort.util.PaginationUtil;
import com.oceanviewresort.util.PaginationUtil.PaginationData;

/**
 * Controller for handling room-related requests at Ocean View Resort.
 * Manages room listing, status updates, and room type management.
 */
@WebServlet("/room")
public class RoomController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private RoomService roomService;
    private ReservationService reservationService;
    private ActivityService activityService;

    /**
     * Initializes the controller
     */
    public void init() throws ServletException {
        roomService = RoomService.getInstance();
        reservationService = ReservationService.getInstance();
        activityService = new ActivityService();

        try {
            activityService.initialize();
        } catch (Exception e) {
            System.err.println("Failed to initialize activity system: " + e.getMessage());
        }
    }

    /**
     * Handles GET requests for room management
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("auth?action=login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null || action.equals("list")) {
            listRooms(request, response);
        } else if (action.equals("add")) {
            showAddForm(request, response);
        } else if (action.equals("edit")) {
            showEditForm(request, response);
        } else if (action.equals("view")) {
            viewRoom(request, response);
        } else if (action.equals("available")) {
            listAvailableRooms(request, response);
        } else if (action.equals("types")) {
            listRoomTypes(request, response);
        }
    }

    /**
     * Handles POST requests for room management
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("auth?action=login");
            return;
        }

        String action = request.getParameter("action");
        if (action.equals("add")) {
            addRoom(request, response);
        } else if (action.equals("update")) {
            updateRoom(request, response);
        } else if (action.equals("updateStatus")) {
            updateRoomStatus(request, response);
        }
    }

    /**
     * Lists all rooms with pagination
     */
    private void listRooms(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int page = PaginationUtil.parsePageNumber(request.getParameter("page"));
            int pageSize = PaginationUtil.parsePageSize(request.getParameter("pageSize"));
            String statusFilter = request.getParameter("status");

            List<Room> roomList;
            int totalItems;

            if (statusFilter != null && !statusFilter.trim().isEmpty()) {
                roomList = roomService.getRoomsByStatus(statusFilter);
                totalItems = roomList.size();
            } else {
                int offset = PaginationUtil.calculateOffset(page, pageSize);
                roomList = roomService.getRoomsPaginated(offset, pageSize);
                totalItems = roomService.getRoomCount();
            }

            PaginationData paginationData = PaginationUtil.createPaginationData(roomList, page, pageSize, totalItems);

            // Get room types for filter dropdown
            List<RoomType> roomTypes = roomService.getAllRoomTypes();

            request.setAttribute("rooms", roomList);
            request.setAttribute("roomTypes", roomTypes);
            request.setAttribute("pagination", paginationData);
            request.setAttribute("statusFilter", statusFilter);

            // Room status counts
            request.setAttribute("availableCount", roomService.getRoomCountByStatus(Room.STATUS_AVAILABLE));
            request.setAttribute("occupiedCount", roomService.getRoomCountByStatus(Room.STATUS_OCCUPIED));
            request.setAttribute("reservedCount", roomService.getRoomCountByStatus(Room.STATUS_RESERVED));
            request.setAttribute("maintenanceCount", roomService.getRoomCountByStatus(Room.STATUS_MAINTENANCE));

        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("WEB-INF/view/room/listRooms.jsp").forward(request, response);
    }

    /**
     * Lists available rooms
     */
    private void listAvailableRooms(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Room> availableRooms = roomService.getAvailableRooms();
            List<RoomType> roomTypes = roomService.getAllRoomTypes();

            request.setAttribute("rooms", availableRooms);
            request.setAttribute("roomTypes", roomTypes);

        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
        }

        request.getRequestDispatcher("WEB-INF/view/room/availableRooms.jsp").forward(request, response);
    }

    /**
     * Lists room types
     */
    private void listRoomTypes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<RoomType> roomTypes = roomService.getAllRoomTypes();
            request.setAttribute("roomTypes", roomTypes);
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
        }

        request.getRequestDispatcher("WEB-INF/view/room/roomTypes.jsp").forward(request, response);
    }

    /**
     * Shows the form for adding a new room
     */
    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<RoomType> roomTypes = roomService.getAllRoomTypes();
            request.setAttribute("roomTypes", roomTypes);
        } catch (SQLException e) {
            request.setAttribute("error", "Failed to load room types: " + e.getMessage());
        }

        request.getRequestDispatcher("WEB-INF/view/room/addRoom.jsp").forward(request, response);
    }

    /**
     * Processes the addition of a new room
     */
    private void addRoom(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String roomNumber = ValidationUtils.sanitizeString(request.getParameter("roomNumber"));
        String roomTypeIdStr = request.getParameter("roomTypeId");
        String floorNumberStr = request.getParameter("floorNumber");
        String description = ValidationUtils.sanitizeString(request.getParameter("description"));

        int roomTypeId = 0;
        int floorNumber = 1;

        try {
            roomTypeId = Integer.parseInt(roomTypeIdStr);
            floorNumber = Integer.parseInt(floorNumberStr);
        } catch (NumberFormatException e) {
            // Will be caught by validation
        }

        Map<String, String> validationErrors = ValidationUtils.validateRoom(roomNumber, roomTypeId, 
                floorNumber, description);

        if (!validationErrors.isEmpty()) {
            request.setAttribute("fieldErrors", validationErrors);
            try {
                List<RoomType> roomTypes = roomService.getAllRoomTypes();
                request.setAttribute("roomTypes", roomTypes);
            } catch (SQLException e) {
                // Ignore
            }
            request.setAttribute("roomNumber", roomNumber);
            request.setAttribute("roomTypeId", roomTypeId);
            request.setAttribute("floorNumber", floorNumber);
            request.setAttribute("description", description);
            request.getRequestDispatcher("WEB-INF/view/room/addRoom.jsp").forward(request, response);
            return;
        }

        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.setRoomTypeId(roomTypeId);
        room.setFloorNumber(floorNumber);
        room.setDescription(description);
        room.setStatus(Room.STATUS_AVAILABLE);

        try {
            int roomId = roomService.addRoom(room);

            if (roomId > 0) {
                response.sendRedirect("room?action=list&success=Room added successfully");
            } else {
                request.setAttribute("error", "Room number already exists");
                List<RoomType> roomTypes = roomService.getAllRoomTypes();
                request.setAttribute("roomTypes", roomTypes);
                request.setAttribute("roomNumber", roomNumber);
                request.setAttribute("roomTypeId", roomTypeId);
                request.setAttribute("floorNumber", floorNumber);
                request.setAttribute("description", description);
                request.getRequestDispatcher("WEB-INF/view/room/addRoom.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("WEB-INF/view/room/addRoom.jsp").forward(request, response);
        }
    }

    /**
     * Shows the form for editing a room
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String roomIdStr = request.getParameter("id");

        try {
            int roomId = Integer.parseInt(roomIdStr);
            Room room = roomService.getRoomById(roomId);

            if (room == null) {
                request.setAttribute("errorMessage", "Room not found");
                request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
                return;
            }

            List<RoomType> roomTypes = roomService.getAllRoomTypes();
            request.setAttribute("room", room);
            request.setAttribute("roomTypes", roomTypes);
            request.getRequestDispatcher("WEB-INF/view/room/editRoom.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid Room ID");
            request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
        }
    }

    /**
     * Updates a room
     */
    private void updateRoom(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String roomIdStr = request.getParameter("roomId");
        String roomNumber = ValidationUtils.sanitizeString(request.getParameter("roomNumber"));
        String roomTypeIdStr = request.getParameter("roomTypeId");
        String floorNumberStr = request.getParameter("floorNumber");
        String status = request.getParameter("status");
        String description = ValidationUtils.sanitizeString(request.getParameter("description"));

        try {
            int roomId = Integer.parseInt(roomIdStr);
            int roomTypeId = Integer.parseInt(roomTypeIdStr);
            int floorNumber = Integer.parseInt(floorNumberStr);

            Room room = new Room();
            room.setRoomId(roomId);
            room.setRoomNumber(roomNumber);
            room.setRoomTypeId(roomTypeId);
            room.setFloorNumber(floorNumber);
            room.setStatus(status);
            room.setDescription(description);

            boolean success = roomService.updateRoom(room);

            if (success) {
                response.sendRedirect("room?action=list&success=Room updated successfully");
            } else {
                request.setAttribute("error", "Failed to update room. Room number may already exist.");
                List<RoomType> roomTypes = roomService.getAllRoomTypes();
                request.setAttribute("room", room);
                request.setAttribute("roomTypes", roomTypes);
                request.getRequestDispatcher("WEB-INF/view/room/editRoom.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid input");
            request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("WEB-INF/view/room/editRoom.jsp").forward(request, response);
        }
    }

    /**
     * Updates room status
     */
    private void updateRoomStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String roomIdStr = request.getParameter("roomId");
        String status = request.getParameter("status");

        try {
            int roomId = Integer.parseInt(roomIdStr);
            Room room = roomService.getRoomById(roomId);
            
            if (room == null) {
                response.sendRedirect("room?action=list&error=Room not found");
                return;
            }

            boolean success = roomService.updateRoomStatus(roomId, status);

            if (success) {
                HttpSession session = request.getSession(false);
                String username = session != null ? (String) session.getAttribute("username") : "system";
                try {
                    activityService.logRoomStatusChanged(roomId, room.getRoomNumber(), status, username);
                } catch (Exception e) {
                    System.err.println("Failed to log room status change: " + e.getMessage());
                }

                response.sendRedirect("room?action=list&success=Room status updated");
            } else {
                response.sendRedirect("room?action=list&error=Failed to update room status");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("room?action=list&error=Invalid Room ID");
        } catch (SQLException e) {
            response.sendRedirect("room?action=list&error=Database error: " + e.getMessage());
        }
    }

    /**
     * Views a single room's details
     */
    private void viewRoom(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String roomIdStr = request.getParameter("id");

        try {
            int roomId = Integer.parseInt(roomIdStr);
            Room room = roomService.getRoomById(roomId);

            if (room == null) {
                request.setAttribute("errorMessage", "Room not found");
                request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
                return;
            }

            request.setAttribute("room", room);

            // Look up active reservation for this room
            try {
                Reservation activeReservation = reservationService.getActiveReservationByRoomId(roomId);
                if (activeReservation != null) {
                    request.setAttribute("activeReservation", activeReservation);
                }
            } catch (SQLException ex) {
                // Non-critical, continue without reservation info
            }

            request.getRequestDispatcher("WEB-INF/view/room/viewRoom.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid Room ID");
            request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
        }
    }
}
