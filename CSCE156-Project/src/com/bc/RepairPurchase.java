package com.bc;

/*
 * Date: 10/4/2020
 * CSCE 156, Assignment 3
 * @authors Treyvor Vice, Ann Le
 * This is a subclass that extends the Purchase superclass. In addition to the fields of a Purchase, each 
 * RepairPurchase contains an hoursWorked float.
 */

public class RepairPurchase extends Purchase {

	private float hoursWorked;
	
	public float getHoursWorked() {
		return this.hoursWorked;
	}

	public RepairPurchase(Products product, float hoursWorked) {
		super(product);
		this.hoursWorked = hoursWorked;
	}
	
	protected float getPurchaseCost() {
		float subtotal = 0;
		
		float partsCost = ((Repair)this.getProduct()).getPartsCost();
		float laborCost = this.hoursWorked * ((Repair)this.getProduct()).getLaborRate();
		
		subtotal = partsCost + laborCost;
		return subtotal;
	}
}
