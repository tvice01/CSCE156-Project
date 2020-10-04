package com.bc;

import java.util.ArrayList;

public class Invoice {
	
	private String invoiceCode;
	private String personCode;
	private String customerCode;
	private ArrayList<Purchase> purchaseList;
	
	public String getInvoiceCode() {
		return this.invoiceCode;
	}
	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}
	public String getPersonCode() {
		return this.personCode;
	}
	public void setPersonCode(String personCode) {
		this.personCode = personCode;
	}
	public String getCustomerCode() {
		return this.customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public ArrayList<Purchase> getPurchaseList() {
		return this.purchaseList;
	}
	public void setPurchaseList(ArrayList<Purchase> purchaseList) {
		this.purchaseList = purchaseList;
	}
	
	public Invoice(String invoiceCode, String personCode, String customerCode, ArrayList<Purchase> purchaseList) {
		this.invoiceCode = invoiceCode;
		this.personCode = personCode;
		this.customerCode = customerCode;
		this.purchaseList = purchaseList;
	}
	
}
