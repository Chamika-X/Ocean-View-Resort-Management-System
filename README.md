# 🏖️ Ocean View Resort — Hotel Reservation Management System

A web-based Hotel Management System built with **Java (Servlet/JSP)** and **MySQL** for Ocean View Resort, Galle, Sri Lanka. Manages reservations, guests, rooms, billing, and staff operations.

---

## ✨ Features

- **Dashboard** — Real-time stats, revenue overview, today's check-ins/check-outs
- **Room Management** — Add/edit rooms, track availability & status
- **Reservations** — Book, check-in, check-out, cancel reservations
- **Guest Profiles** — Register & manage guest information
- **Billing** — Generate invoices, print/email bills, apply discounts
- **Products** — Manage hotel services & additional charges
- **User Management** — Role-based access (Admin / Staff)
- **Activity Log** — Track all system activities (admin only)

---

## 🛠️ Tech Stack

| Component | Technology |
|-----------|------------|
| Backend | Java 17, Servlet 4.0, JSP, JSTL |
| Frontend | Tailwind CSS, Font Awesome |
| Database | MySQL 8.0 |
| Build | Apache Maven |
| Server | Apache Tomcat 9+ |
| Testing | JUnit 4, Mockito |

---

## 🚀 Setup & Run

### Prerequisites
- Java JDK 17+
- Apache Maven 3.6+
- Apache Tomcat 9+
- MySQL 8.0+

### 1. Database Setup
```bash
mysql -u root -p < database.sql
```

### 2. Configure DB Connection
Edit `src/main/java/com/oceanviewresort/dao/DBConnection.java`:
```java
private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/oceanviewresort";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "1234";
```

### 3. Build & Deploy
```bash
mvn clean package
```
Deploy `target/ocean-view-resort.war` to Tomcat, or run directly from IntelliJ IDEA.

### 4. Access
```
http://localhost:8080/ocean-view-resort/
```

---

## 🔑 Default Credentials

| Role | Username | Password |
|------|----------|----------|
| Admin | `admin` | `admin123` |
| Staff | `receptionist` | `staff123` |

---

## 📁 Project Structure

```
src/main/java/com/oceanviewresort/
├── controller/    # Servlets (AuthController, RoomController, etc.)
├── model/         # POJOs (Room, Guest, Reservation, Bill, etc.)
├── service/       # Business logic (Singleton services)
├── dao/           # Database access (JDBC)
├── util/          # Pagination utilities
└── validation/    # Input validation

src/main/webapp/WEB-INF/view/
├── auth/          # Login, Register, Profile
├── room/          # Room management views
├── reservation/   # Reservation & check-in/out views
├── guest/         # Guest management views
├── bill/          # Billing views
├── product/       # Product views
├── activity/      # Activity log (admin)
└── user-management/  # User management (admin)
```

---

## 🎨 Design Patterns

| Pattern | Usage |
|---------|-------|
| **MVC** | Model-View-Controller architecture |
| **Singleton** | DBConnection & all Service classes |
| **Front Controller** | Single servlet per module with action routing |
| **DAO** | Abstracts database operations |

---

## 🧪 Testing

```bash
mvn test
```
Tests cover: `UserService`, `BillService`, `CustomerService`, `ProductService`, `ValidationUtils`

---

## 📧 Email Setup (Optional)

Copy and configure `src/main/resources/email-config.properties.template` with your SMTP credentials for bill emailing.

---

## 👨‍💻 Author

**Chamika-X** — chamika9933@gmail.com

---

> *Ocean View Resort, Galle, Sri Lanka — 2026*

