package com.oceanviewresort.model;

import java.sql.Timestamp;

/**
 * Customer model class representing customers in the system. Used for customer
 * account management and billing.
 */
public class Customer {
	private int customerId;
	private String accountNumber;
	private String name;
	private String address;
	private String telephone;
	private Timestamp createdAt;
	private Timestamp updatedAt;

	/**
	 * Default constructor
	 */
	public Customer() {
	}

	/**
	 * Constructor with all fields except timestamps
	 */
	public Customer(int customerId, String accountNumber, String name, String address, String telephone) {
		this.customerId = customerId;
		this.accountNumber = accountNumber;
		this.name = name;
		this.address = address;
		this.telephone = telephone;
	}

	/**
	 * Constructor with all fields
	 */
	public Customer(int customerId, String accountNumber, String name, String address, String telephone,
			Timestamp createdAt, Timestamp updatedAt) {
		this.customerId = customerId;
		this.accountNumber = accountNumber;
		this.name = name;
		this.address = address;
		this.telephone = telephone;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	/**
	 * Constructor for creating a new customer (without customerId and timestamps)
	 */
	public Customer(String accountNumber, String name, String address, String telephone) {
		this.accountNumber = accountNumber;
		this.name = name;
		this.address = address;
		this.telephone = telephone;
	}

	// Getters and Setters
	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", accountNumber=" + accountNumber + ", name=" + name
				+ ", telephone=" + telephone + "]";
	}
}