package com.bc;

/*
 * Date: 11/2/2020
 * CSCE 156
 * @authors Treyvor Vice, Ann Le
 * This is a subclass that extends the Purchase superclass. In addition to the fields of a Purchase, each 
 * RentalPurchase contains a daysRented integer.
 */

public class RentalPurchase extends Purchase {

	private float daysRented;
	
	public float getDaysRented() {
		return this.daysRented;
	}

	public RentalPurchase(Product product, float daysRented) {
		super(product);
		this.daysRented = daysRented;
	}

	protected float getPurchaseCost() {
		float subtotal = 0;
		
		float daysCost = this.daysRented * ((Rental)this.getProduct()).getDailyCost();
		float deposit = ((Rental)this.getProduct()).getDeposit();
		float cleaningFee = ((Rental)this.getProduct()).getCleaningFee();
		
		subtotal = daysCost - deposit + cleaningFee;
		return subtotal;
	}
	
	
}
