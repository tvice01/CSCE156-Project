package com.bc;

import java.util.ArrayList;

/*
 * Date: 11/2/2020
 * CSCE 156
 * @authors Treyvor Vice, Ann Le
 * This class stores objects of the type Invoice. Every Invoice contains an invoiceCode, person, customer, 
 * and an ArrayList of purchases.
 */

public class Invoice {
	
	private String invoiceCode;
	private Person person;
	private Customer customer;
	private ArrayList<Purchase> purchaseList;
	
	public String getInvoiceCode() {
		return this.invoiceCode;
	}
	public Person getPerson() {
		return this.person;
	}
	public Customer getCustomer() {
		return this.customer;
	}
	public ArrayList<Purchase> getPurchaseList() {
		return this.purchaseList;
	}
	
	public void setPurchaseList(ArrayList<Purchase> purchaseList) {
		this.purchaseList = purchaseList;
	}
	
	public Invoice(String invoiceCode, Person person, Customer customer, ArrayList<Purchase> purchaseList) {
		this.invoiceCode = invoiceCode;
		this.person = person;
		this.customer = customer;
		this.purchaseList = purchaseList;
	}
	
	// Calculates the subtotal price for all purchases in a specified Invoice
	public float calculateSubTotal() {
		
		float subtotal = 0;
		
		// Iterate through the purchaseList and add each purchase cost to the subtotal
		for (Purchase purch : this.purchaseList) {
				
			// If this was a rental (R) purchase
			if (purch.getProduct().getType().equals("R")) {
				subtotal += purch.getPurchaseCost();
			}
			
			// If this was a repair (F) purchase
			else if (purch.getProduct().getType().equals("F")) {
				subtotal += purch.getPurchaseCost();
			}
			
			// If this was a concession (C) purchase
			else if (purch.getProduct().getType().equals("C")) {
				subtotal += purch.getPurchaseCost();
			}
			
			// If this was a towing (T) purchase
			else if (purch.getProduct().getType().equals("T")) {
				subtotal += purch.getPurchaseCost();
			}
		}
		
		return subtotal;
	}
	
	public float calculatePreTaxDiscounts() {
	//Calculate any pre tax discounts for the invoice	
		float indItemDiscount = 0;
		
		// Check if the purchaseList contains a towing, repair, & rental
		// If so, add cost of towing to indItemDiscount to make towing free
		int towingFlag=0, repairFlag=0, rentalFlag=0;
		for (Purchase purch : this.purchaseList) {
			
			if (purch.getProduct().getType().equals("T")) {
				towingFlag = 1;
			}
			if (purch.getProduct().getType().equals("R")) {
				repairFlag = 1;
			}
			if (purch.getProduct().getType().equals("F")) {
				rentalFlag = 1;
			}
		}
		int freeTowing = towingFlag + repairFlag + rentalFlag;
		if (freeTowing == 3) {
			for (Purchase purch : this.purchaseList) {
				if (purch.getProduct().getType().equals("T")) {
					indItemDiscount += ((TowingPurchase)purch).getPurchaseCost();
				}
			}
		}
		
		// Check if the purchaseList contains concessions with associated repairs
		// If so, add 10% of concession cost to indItemDiscount
		for (Purchase purch : this.purchaseList) {
			if (purch.getProduct().getType().equals("C")) {
				if (((ConcessionPurchase)purch).getAssocRepairDiscount()) {
					indItemDiscount += ((ConcessionPurchase)purch).getPurchaseCost()*0.1;
				}
			}
		}
		
		return indItemDiscount;
	}
	
	public float calculateTax() {
	// Calculate tax for the invoice based on whether the customer account is a business
	// or personal account
		
		float tax = 0;
		
		if (this.customer.getType().equals("B")) {
			tax += (this.calculateSubTotal() - this.calculatePreTaxDiscounts()) * .0425;
		}
		else {
			tax += (this.calculateSubTotal() - this.calculatePreTaxDiscounts()) * .08;
		}
		
		return tax;
	}
	
	public float calculateFees() {
	// Calculate the additional fees for the invoice. If the customer account is a 
	// business account, apply a $75.50 fee
		float fees = 0;
		
		if (this.customer.getType().equals("B")) {
			fees = (float)75.5;
		}
		
		return fees;
	}
	
	public float calculatePostTaxDiscounts() {
	// Calculate any post tax discounts for the invoice
		float invoiceDiscount = 0;
		
		// Check if the customer is a personal account and has 2 or more email addresses
		// If so, add a 5% loyalty discount to invoiceDiscount
		if (this.customer.getType().equals("P")
				&& this.customer.getPrimaryContact().getEmails().length >= 2) {
			invoiceDiscount += (this.calculateSubTotal() - this.calculatePreTaxDiscounts()
					+ this.calculateTax() + this.calculateFees()) * 0.05;
		}
		return invoiceDiscount;
	}
	
	public float calculateTotalCost() {
	// Calculate this totalCost for the invoice (sum of subtotal, fees, & tax, minus discounts)
		float totalCost = this.calculateSubTotal() - this.calculatePreTaxDiscounts()
				- this.calculatePostTaxDiscounts() + this.calculateFees() + this.calculateTax();
		
		return totalCost;
	}
}
