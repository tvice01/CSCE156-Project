package com.bc;

/*
 * Date: 10/4/2020
 * CSCE 156, Assignment 3
 * @authors Treyvor Vice, Ann Le
 * This is a subclass that extends the Product superclass. In addition to the fields of a Product, each Rental contains
 * a dailyCost, deposit, and a cleaningFee.
 */

public class Rental extends Product {
	
	private float dailyCost;
	private float deposit;
	private float cleaningFee;
	
	public float getDailyCost() {
		return dailyCost;
	}
	public float getDeposit() {
		return deposit;
	}
	public float getCleaningFee() {
		return cleaningFee;
	}

	public Rental(String code, String type, String label, float dailyCost, float deposit, 
			float cleaningFee) {
		super(code, type, label);
		this.dailyCost = dailyCost;
		this.deposit = deposit;
		this.cleaningFee = cleaningFee;
	}
}
