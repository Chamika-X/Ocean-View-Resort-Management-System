package com.oceanviewresort.model;

import java.math.BigDecimal;

public class BillItem {
	private int billItemId;
	private int billId;
	private int productId;
	private String productName;
	private int quantity;
	private BigDecimal unitPrice;
	private BigDecimal subtotal;

	// Default constructor
	public BillItem() {
		this.quantity = 1;
		this.unitPrice = BigDecimal.ZERO;
		this.subtotal = BigDecimal.ZERO;
	}

	// Constructor with product info
	public BillItem(int productId, String productName, int quantity, BigDecimal unitPrice) {
		this();
		this.productId = productId;
		this.productName = productName;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		calculateSubtotal();
	}

	// Getters and Setters
	public int getBillItemId() {
		return billItemId;
	}

	public void setBillItemId(int billItemId) {
		this.billItemId = billItemId;
	}

	public int getBillId() {
		return billId;
	}

	public void setBillId(int billId) {
		this.billId = billId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
		calculateSubtotal();
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
		calculateSubtotal();
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	// Helper method to calculate subtotal
	public void calculateSubtotal() {
		if (unitPrice != null && quantity > 0) {
			this.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
		} else {
			this.subtotal = BigDecimal.ZERO;
		}
	}

	@Override
	public String toString() {
		return "BillItem{" + "billItemId=" + billItemId + ", billId=" + billId + ", productId=" + productId
				+ ", productName='" + productName + '\'' + ", quantity=" + quantity + ", unitPrice=" + unitPrice
				+ ", subtotal=" + subtotal + '}';
	}
}