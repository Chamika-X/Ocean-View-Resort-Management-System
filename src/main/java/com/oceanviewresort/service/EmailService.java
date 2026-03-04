package com.oceanviewresort.service;

import com.oceanviewresort.model.Bill;
import com.oceanviewresort.model.Customer;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 * Service class for handling email operations, specifically for sending bills
 * to customers. Uses JavaMail API with Mailtrap configuration.
 */
public class EmailService {
    
    private Properties mailProperties;
    private Properties configProperties;
    
    private String smtpHost;
    private int smtpPort;
    private String smtpUsername;
    private String smtpPassword;
    private String fromEmail;
    private String fromName;
    private String companyName;
    private String companyAddress;
    private String companyPhone;
    private String companyEmail;
    private String companyWebsite;
    private String companyTagline;
    
    public EmailService() {
        loadConfiguration();
        initializeMailProperties();
    }
    
    /**
     * Load configuration from properties file
     */
    private void loadConfiguration() {
        configProperties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("email-config.properties")) {
            if (input != null) {
                configProperties.load(input);
                
                // Load SMTP settings
                smtpHost = configProperties.getProperty("mail.smtp.host", "smtp.mailtrap.io");
                smtpPort = Integer.parseInt(configProperties.getProperty("mail.smtp.port", "2525"));
                smtpUsername = configProperties.getProperty("mail.smtp.username", "your_mailtrap_username");
                smtpPassword = configProperties.getProperty("mail.smtp.password", "your_mailtrap_password");
                
                // Load email settings
                fromEmail = configProperties.getProperty("mail.from.email", "billing@oceanviewresort.com");
                fromName = configProperties.getProperty("mail.from.name", "Ocean View Resort");

                // Load company information
                companyName = configProperties.getProperty("company.name", "Ocean View Resort");
                companyAddress = configProperties.getProperty("company.address", "Colombo City, Sri Lanka");
                companyPhone = configProperties.getProperty("company.phone", "+94 11 2345678");
                companyEmail = configProperties.getProperty("company.email", "info@oceanviewresort.com");
                companyWebsite = configProperties.getProperty("company.website", "www.oceanviewresort.com");
                companyTagline = configProperties.getProperty("company.tagline", "Your Trusted Destination for a Perfect Getaway");

            } else {
                System.err.println("Email configuration file not found. Using default values.");
                setDefaultValues();
            }
        } catch (IOException e) {
            System.err.println("Error loading email configuration: " + e.getMessage());
            setDefaultValues();
        }
    }
    
    /**
     * Set default values if configuration file is not available
     */
    private void setDefaultValues() {
        smtpHost = "smtp.mailtrap.io";
        smtpPort = 2525;
        smtpUsername = "your_mailtrap_username";
        smtpPassword = "your_mailtrap_password";
        fromEmail = "billing@oceanviewresort.com";
        fromName = "Ocean View Resort";
        companyName = "Ocean View Resort";
        companyAddress = "Colombo City, Sri Lanka";
        companyPhone = "+94 11 2345678";
        companyEmail = "info@oceanviewresort.com";
        companyWebsite = "www.oceanviewresort.com";
        companyTagline = "Your Trusted Destination for a Perfect Getaway";
    }
    
    /**
     * Initialize mail properties for Mailtrap
     */
    private void initializeMailProperties() {
        mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", "true");
        mailProperties.put("mail.smtp.starttls.enable", "true");
        mailProperties.put("mail.smtp.host", smtpHost);
        mailProperties.put("mail.smtp.port", smtpPort);
        mailProperties.put("mail.smtp.ssl.trust", smtpHost);
    }
    
    /**
     * Send bill email to customer
     * @param bill The bill to send
     * @param customer The customer to send the bill to
     * @param recipientEmail The email address to send the bill to
     * @return true if email sent successfully, false otherwise
     */
    public boolean sendBillEmail(Bill bill, Customer customer, String recipientEmail) {
        try {
            Session session = Session.getInstance(mailProperties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(smtpUsername, smtpPassword);
                }
            });
            
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail, fromName));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Your Bill #" + bill.getBillId() + " - Ocean View Resort");

            // Create the message body
            MimeMultipart multipart = new MimeMultipart();
            
            // Add HTML content
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(generateBillEmailHTML(bill, customer), "text/html; charset=UTF-8");
            multipart.addBodyPart(htmlPart);
            
            message.setContent(multipart);
            
            // Send the message
            Transport.send(message);
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Send bill email for walk-in customers
     * @param bill The bill to send
     * @param recipientEmail The email address to send the bill to
     * @return true if email sent successfully, false otherwise
     */
    public boolean sendBillEmailForWalkIn(Bill bill, String recipientEmail) {
        try {
            Session session = Session.getInstance(mailProperties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(smtpUsername, smtpPassword);
                }
            });
            
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail, fromName));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Your Bill #" + bill.getBillId() + " - Ocean View Resort");

            // Create the message body
            MimeMultipart multipart = new MimeMultipart();
            
            // Add HTML content for walk-in customer
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(generateBillEmailHTMLForWalkIn(bill), "text/html; charset=UTF-8");
            multipart.addBodyPart(htmlPart);
            
            message.setContent(multipart);
            
            // Send the message
            Transport.send(message);
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Generate HTML content for bill email
     * @param bill The bill details
     * @param customer The customer details
     * @return HTML string for the email
     */
    private String generateBillEmailHTML(Bill bill, Customer customer) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
        String billDate = bill.getBillDate() != null ? 
            bill.getBillDate().toLocalDateTime().format(formatter) : "N/A";
        
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<title>Bill #").append(bill.getBillId()).append("</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 800px; margin: 0 auto; padding: 20px; }");
        html.append(".header { text-align: center; margin-bottom: 30px; padding: 20px; border-bottom: 3px solid #ea580c; background: #fff7ed; }");
        html.append(".company-name { font-size: 32px; font-weight: bold; color: #ea580c; margin-bottom: 8px; }");
        html.append(".company-tagline { font-size: 16px; color: #c2410c; margin-bottom: 12px; font-style: italic; }");
        html.append(".company-info { font-size: 13px; color: #7c2d12; line-height: 1.8; }");
        html.append(".bill-header { display: flex; justify-content: space-between; margin-bottom: 30px; padding: 25px; background: #fff7ed; border-radius: 8px; border: 1px solid #fdba74; }");
        html.append(".bill-section { flex: 1; }");
        html.append(".section-title { color: #ea580c; margin-bottom: 18px; font-size: 20px; border-bottom: 2px solid #fdba74; padding-bottom: 8px; }");
        html.append(".bill-info { font-size: 18px; font-weight: bold; color: #ea580c; margin-bottom: 5px; }");
        html.append(".info-text { margin-bottom: 8px; font-size: 14px; color: #7c2d12; }");
        html.append(".status-paid { padding: 6px 12px; border-radius: 20px; font-size: 12px; font-weight: 600; background: #d1fae5; color: #065f46; border: 1px solid #10b981; display: inline-block; text-transform: uppercase; letter-spacing: 0.5px; }");
        html.append(".status-pending { padding: 6px 12px; border-radius: 20px; font-size: 12px; font-weight: 600; background: #fef3c7; color: #92400e; border: 1px solid #f59e0b; display: inline-block; text-transform: uppercase; letter-spacing: 0.5px; }");
        html.append(".status-cancelled { padding: 6px 12px; border-radius: 20px; font-size: 12px; font-weight: 600; background: #fee2e2; color: #991b1b; border: 1px solid #ef4444; display: inline-block; text-transform: uppercase; letter-spacing: 0.5px; }");
        html.append("table { width: 100%; border-collapse: collapse; margin-bottom: 30px; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1); }");
        html.append("th { padding: 15px; text-align: left; font-weight: 600; font-size: 14px; text-transform: uppercase; letter-spacing: 0.5px; background: #ea580c; color: white; }");
        html.append("td { padding: 15px; text-align: left; color: #7c2d12; font-size: 14px; }");
        html.append("tr:nth-child(even) { background: #fff7ed; }");
        html.append("tr:nth-child(odd) { background: white; }");
        html.append(".bill-summary { margin: 30px 0; padding: 25px; background: #fff7ed; border-radius: 8px; border: 1px solid #fdba74; }");
        html.append(".summary-row { display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; }");
        html.append(".summary-label { color: #7c2d12; font-size: 14px; }");
        html.append(".summary-value { color: #7c2d12; font-size: 14px; font-weight: bold; }");
        html.append(".discount-value { color: #059669; font-size: 14px; font-weight: bold; }");
        html.append(".total-row { display: flex; justify-content: space-between; align-items: center; }");
        html.append(".total-label { color: #ea580c; font-size: 18px; font-weight: bold; }");
        html.append(".total-value { color: #ea580c; font-size: 24px; font-weight: bold; }");
        html.append(".footer { margin-top: 50px; padding: 25px; border-top: 2px solid #fdba74; text-align: center; font-size: 13px; color: #7c2d12; background: #fff7ed; border-radius: 8px; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        
        // Header
        html.append("<div class='header'>");
        html.append("<div class='company-name'>").append(companyName).append("</div>");
        html.append("<div class='company-tagline'>").append(companyTagline).append("</div>");
        html.append("<div class='company-info'>");
        html.append("<strong>Address:</strong> ").append(companyAddress).append("<br>");
        html.append("<strong>Phone:</strong> ").append(companyPhone).append(" | <strong>Email:</strong> ").append(companyEmail).append("<br>");
        html.append("<strong>Website:</strong> ").append(companyWebsite);
        html.append("</div>");
        html.append("</div>");
        
        // Bill Header
        html.append("<div class='bill-header'>");
        html.append("<div class='bill-section'>");
        html.append("<h3 class='section-title'>Bill To:</h3>");
        html.append("<p class='info-text'><strong>Name:</strong> ").append(customer.getName()).append("</p>");
        html.append("<p class='info-text'><strong>Account Number:</strong> ").append(customer.getAccountNumber()).append("</p>");
        html.append("<p class='info-text'><strong>Address:</strong> ").append(customer.getAddress()).append("</p>");
        html.append("<p class='info-text'><strong>Phone:</strong> ").append(customer.getTelephone()).append("</p>");
        html.append("</div>");
        html.append("<div class='bill-section'>");
        html.append("<h3 class='section-title'>Bill Information:</h3>");
        html.append("<div class='bill-info'>Bill #").append(bill.getBillId()).append("</div>");
        html.append("<p class='info-text'><strong>Date:</strong> ").append(billDate).append("</p>");
        html.append("<p class='info-text'><strong>Status:</strong> ");
        
        // Status styling
        if ("paid".equals(bill.getStatus())) {
            html.append("<span class='status-paid'>").append(bill.getStatus()).append("</span>");
        } else if ("cancelled".equals(bill.getStatus())) {
            html.append("<span class='status-cancelled'>").append(bill.getStatus()).append("</span>");
        } else {
            html.append("<span class='status-pending'>").append(bill.getStatus()).append("</span>");
        }
        html.append("</p>");
        html.append("</div>");
        html.append("</div>");
        
        // Items Table
        html.append("<table>");
        html.append("<thead>");
        html.append("<tr>");
        html.append("<th>#</th>");
        html.append("<th>Product</th>");
        html.append("<th>Quantity</th>");
        html.append("<th>Unit Price</th>");
        html.append("<th>Subtotal</th>");
        html.append("</tr>");
        html.append("</thead>");
        html.append("<tbody>");
        
        if (bill.getItems() != null) {
            for (int i = 0; i < bill.getItems().size(); i++) {
                var item = bill.getItems().get(i);
                html.append("<tr>");
                html.append("<td><strong>").append(i + 1).append("</strong></td>");
                html.append("<td><strong>").append(item.getProductName()).append("</strong></td>");
                html.append("<td>").append(item.getQuantity()).append("</td>");
                html.append("<td>Rs. ").append(item.getUnitPrice()).append("</td>");
                html.append("<td><strong>Rs. ").append(item.getSubtotal()).append("</strong></td>");
                html.append("</tr>");
            }
        }
        
        html.append("</tbody>");
        html.append("</table>");
        
        // Bill Summary
        html.append("<div class='bill-summary'>");
        html.append("<h3 class='section-title'>Bill Summary</h3>");
        
        BigDecimal subtotal = bill.getTotalAmount();
        if (bill.getDiscount().compareTo(BigDecimal.ZERO) > 0) {
            subtotal = bill.getTotalAmount().add(bill.getTotalAmount().multiply(bill.getDiscount()).divide(BigDecimal.valueOf(100)));
        }
        
        html.append("<div class='summary-row'>");
        html.append("<span class='summary-label'>Subtotal:</span>");
        html.append("<span class='summary-value'>Rs. ").append(subtotal).append("</span>");
        html.append("</div>");
        
        if (bill.getDiscount().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discountAmount = bill.getTotalAmount().multiply(bill.getDiscount()).divide(BigDecimal.valueOf(100));
            html.append("<div class='summary-row'>");
            html.append("<span class='summary-label'>Discount (").append(bill.getDiscount()).append("%):</span>");
            html.append("<span class='discount-value'>- Rs. ").append(discountAmount).append("</span>");
            html.append("</div>");
        }
        
        html.append("<hr style='margin: 15px 0; border: none; border-top: 1px solid #fdba74;'>");
        html.append("<div class='total-row'>");
        html.append("<span class='total-label'>Total Amount:</span>");
        html.append("<span class='total-value'>Rs. ").append(bill.getTotalAmount()).append("</span>");
        html.append("</div>");
        html.append("</div>");
        
        // Footer
        html.append("<div class='footer'>");
        html.append("<p><strong>Thank you for your business!</strong></p>");
        html.append("<p>For any queries, please contact us at ").append(companyEmail).append(" or call ").append(companyPhone).append("</p>");
        html.append("<p>This is a computer generated bill. No signature required.</p>");
        html.append("<p style='margin-top: 15px; font-size: 11px; color: #9a3412;'>");
        html.append("<strong>").append(companyName).append("</strong> - ").append(companyTagline);
        html.append("</p>");
        html.append("</div>");
        
        html.append("</body>");
        html.append("</html>");
        
        return html.toString();
    }
    
    /**
     * Generate HTML content for walk-in customer bill email
     * @param bill The bill details
     * @return HTML string for the email
     */
    private String generateBillEmailHTMLForWalkIn(Bill bill) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
        String billDate = bill.getBillDate() != null ? 
            bill.getBillDate().toLocalDateTime().format(formatter) : "N/A";
        
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<title>Bill #").append(bill.getBillId()).append("</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 800px; margin: 0 auto; padding: 20px; }");
        html.append(".header { text-align: center; margin-bottom: 30px; padding: 20px; border-bottom: 3px solid #ea580c; background: #fff7ed; }");
        html.append(".company-name { font-size: 32px; font-weight: bold; color: #ea580c; margin-bottom: 8px; }");
        html.append(".company-tagline { font-size: 16px; color: #c2410c; margin-bottom: 12px; font-style: italic; }");
        html.append(".company-info { font-size: 13px; color: #7c2d12; line-height: 1.8; }");
        html.append(".bill-header { display: flex; justify-content: space-between; margin-bottom: 30px; padding: 25px; background: #fff7ed; border-radius: 8px; border: 1px solid #fdba74; }");
        html.append(".bill-section { flex: 1; }");
        html.append(".section-title { color: #ea580c; margin-bottom: 18px; font-size: 20px; border-bottom: 2px solid #fdba74; padding-bottom: 8px; }");
        html.append(".bill-info { font-size: 18px; font-weight: bold; color: #ea580c; margin-bottom: 5px; }");
        html.append(".info-text { margin-bottom: 8px; font-size: 14px; color: #7c2d12; }");
        html.append(".status-paid { padding: 6px 12px; border-radius: 20px; font-size: 12px; font-weight: 600; background: #d1fae5; color: #065f46; border: 1px solid #10b981; display: inline-block; text-transform: uppercase; letter-spacing: 0.5px; }");
        html.append(".status-pending { padding: 6px 12px; border-radius: 20px; font-size: 12px; font-weight: 600; background: #fef3c7; color: #92400e; border: 1px solid #f59e0b; display: inline-block; text-transform: uppercase; letter-spacing: 0.5px; }");
        html.append(".status-cancelled { padding: 6px 12px; border-radius: 20px; font-size: 12px; font-weight: 600; background: #fee2e2; color: #991b1b; border: 1px solid #ef4444; display: inline-block; text-transform: uppercase; letter-spacing: 0.5px; }");
        html.append("table { width: 100%; border-collapse: collapse; margin-bottom: 30px; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1); }");
        html.append("th { padding: 15px; text-align: left; font-weight: 600; font-size: 14px; text-transform: uppercase; letter-spacing: 0.5px; background: #ea580c; color: white; }");
        html.append("td { padding: 15px; text-align: left; color: #7c2d12; font-size: 14px; }");
        html.append("tr:nth-child(even) { background: #fff7ed; }");
        html.append("tr:nth-child(odd) { background: white; }");
        html.append(".bill-summary { margin: 30px 0; padding: 25px; background: #fff7ed; border-radius: 8px; border: 1px solid #fdba74; }");
        html.append(".summary-row { display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; }");
        html.append(".summary-label { color: #7c2d12; font-size: 14px; }");
        html.append(".summary-value { color: #7c2d12; font-size: 14px; font-weight: bold; }");
        html.append(".discount-value { color: #059669; font-size: 14px; font-weight: bold; }");
        html.append(".total-row { display: flex; justify-content: space-between; align-items: center; }");
        html.append(".total-label { color: #ea580c; font-size: 18px; font-weight: bold; }");
        html.append(".total-value { color: #ea580c; font-size: 24px; font-weight: bold; }");
        html.append(".footer { margin-top: 50px; padding: 25px; border-top: 2px solid #fdba74; text-align: center; font-size: 13px; color: #7c2d12; background: #fff7ed; border-radius: 8px; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        
        // Header
        html.append("<div class='header'>");
        html.append("<div class='company-name'>").append(companyName).append("</div>");
        html.append("<div class='company-tagline'>").append(companyTagline).append("</div>");
        html.append("<div class='company-info'>");
        html.append("<strong>Address:</strong> ").append(companyAddress).append("<br>");
        html.append("<strong>Phone:</strong> ").append(companyPhone).append(" | <strong>Email:</strong> ").append(companyEmail).append("<br>");
        html.append("<strong>Website:</strong> ").append(companyWebsite);
        html.append("</div>");
        html.append("</div>");
        
        // Bill Header - Modified for walk-in customer
        html.append("<div class='bill-header'>");
        html.append("<div class='bill-section'>");
        html.append("<h3 class='section-title'>Bill To:</h3>");
        html.append("<p class='info-text'><strong>Customer:</strong> Walk-in Customer</p>");
        html.append("<p class='info-text'><strong>Type:</strong> Walk-in Purchase</p>");
        html.append("<p class='info-text'><strong>Account:</strong> WALK-IN</p>");
        html.append("<p class='info-text'><em>No customer account information available</em></p>");
        html.append("</div>");
        html.append("<div class='bill-section'>");
        html.append("<h3 class='section-title'>Bill Information:</h3>");
        html.append("<div class='bill-info'>Bill #").append(bill.getBillId()).append("</div>");
        html.append("<p class='info-text'><strong>Date:</strong> ").append(billDate).append("</p>");
        html.append("<p class='info-text'><strong>Status:</strong> ");
        
        // Status styling
        if ("paid".equals(bill.getStatus())) {
            html.append("<span class='status-paid'>").append(bill.getStatus()).append("</span>");
        } else if ("cancelled".equals(bill.getStatus())) {
            html.append("<span class='status-cancelled'>").append(bill.getStatus()).append("</span>");
        } else {
            html.append("<span class='status-pending'>").append(bill.getStatus()).append("</span>");
        }
        html.append("</p>");
        html.append("</div>");
        html.append("</div>");
        
        // Items Table
        html.append("<h3 class='section-title'>Items Purchased:</h3>");
        html.append("<table>");
        html.append("<thead>");
        html.append("<tr>");
        html.append("<th>#</th>");
        html.append("<th>Product</th>");
        html.append("<th>Quantity</th>");
        html.append("<th>Unit Price</th>");
        html.append("<th>Subtotal</th>");
        html.append("</tr>");
        html.append("</thead>");

        html.append("<tbody>");
        
        if (bill.getItems() != null) {
            for (int i = 0; i < bill.getItems().size(); i++) {
                var item = bill.getItems().get(i);
                html.append("<tr>");
                html.append("<td><strong>").append(i + 1).append("</strong></td>");
                html.append("<td><strong>").append(item.getProductName()).append("</strong></td>");
                html.append("<td>").append(item.getQuantity()).append("</td>");
                html.append("<td>Rs. ").append(item.getUnitPrice()).append("</td>");
                html.append("<td><strong>Rs. ").append(item.getSubtotal()).append("</strong></td>");
                html.append("</tr>");
            }
        }
        
        html.append("</tbody>");
        html.append("</table>");
        
        // Bill Summary
        html.append("<div class='bill-summary'>");
        html.append("<h3 class='section-title'>Bill Summary</h3>");
        
        java.math.BigDecimal subtotal = bill.getTotalAmount();
        if (bill.getDiscount().compareTo(java.math.BigDecimal.ZERO) > 0) {
            subtotal = bill.getTotalAmount().add(bill.getTotalAmount().multiply(bill.getDiscount()).divide(java.math.BigDecimal.valueOf(100)));
        }
        
        html.append("<div class='summary-row'>");
        html.append("<span class='summary-label'>Subtotal:</span>");
        html.append("<span class='summary-value'>Rs. ").append(subtotal).append("</span>");
        html.append("</div>");
        
        if (bill.getDiscount().compareTo(java.math.BigDecimal.ZERO) > 0) {
            java.math.BigDecimal discountAmount = bill.getTotalAmount().multiply(bill.getDiscount()).divide(java.math.BigDecimal.valueOf(100));
            html.append("<div class='summary-row'>");
            html.append("<span class='summary-label'>Discount (").append(bill.getDiscount()).append("%):</span>");
            html.append("<span class='discount-value'>- Rs. ").append(discountAmount).append("</span>");
            html.append("</div>");
        }
        
        html.append("<hr style='margin: 15px 0; border: none; border-top: 1px solid #fdba74;'>");
        html.append("<div class='total-row'>");
        html.append("<span class='total-label'>Total Amount:</span>");
        html.append("<span class='total-value'>Rs. ").append(bill.getTotalAmount()).append("</span>");
        html.append("</div>");
        html.append("</div>");
        
        // Footer
        html.append("<div class='footer'>");
        html.append("<strong>Thank you for your business!</strong><br>");
        html.append("If you have any questions about this bill, please contact us at ").append(companyPhone).append(" or ").append(companyEmail).append(".<br>");
        html.append("<em>This is an automated email. Please do not reply to this email address.</em><br><br>");
        html.append("Visit us at ").append(companyWebsite).append(" for more information.");
        html.append("</div>");
        
        html.append("</body>");
        html.append("</html>");
        
        return html.toString();
    }
    
    /**
     * Test email configuration
     * @param testEmail Email address to send test to
     * @return true if test email sent successfully, false otherwise
     */
    public boolean sendTestEmail(String testEmail) {
        try {
            Session session = Session.getInstance(mailProperties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(smtpUsername, smtpPassword);
                }
            });
            
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail, fromName));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(testEmail));
            message.setSubject("Test Email - " + companyName);
            message.setText("This is a test email from " + companyName + " billing system.");
            
            Transport.send(message);
            return true;
            
        } catch (Exception e) {
            System.err.println("Error sending test email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
