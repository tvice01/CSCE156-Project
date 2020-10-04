package com.bc;

/*
 * Date: 10/4/2020
 * CSCE 156, Assignment 3
 * @authors Treyvor Vice, Ann Le
 * This is a subclass that extends the Purchase superclass. In addition to the fields of a Purchase, each 
 * RentalPurchase contains a daysRented integer.
 */

public class RentalPurchase extends Purchase {

	private int daysRented;
	
	public int getDaysRented() {
		return this.daysRented;
	}
	public void setDaysRented(int daysRented) {
		this.daysRented = daysRented;
	}

	public RentalPurchase(Products product, int daysRented) {
		super(product);
		this.daysRented = daysRented;
	}
	
	
}
