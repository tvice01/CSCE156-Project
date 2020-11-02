package com.bc;

/*
 * Date: 10/4/2020
 * CSCE 156, Assignment 3
 * @authors Treyvor Vice, Ann Le
 * This class stores objects of the type Product, and is a superclass for the Rental, Repair, Concession, and
 * Towing classes. Every Product contains a code, type, and label.
 */

public abstract class Product {
	
	private String code;
	private String type;
	private String label;
		
	public String getCode() {
		return code;
	}
	public String getType() {
		return type;
	}
	public String getLabel() {
		return label;
	}
	
	public Product(String code, String type, String label) {
		this.code = code;
		this.type = type;
		this.label = label;
	}

}