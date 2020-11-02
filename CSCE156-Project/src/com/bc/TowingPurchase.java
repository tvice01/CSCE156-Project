package com.bc;

/*
 * Date: 10/4/2020
 * CSCE 156, Assignment 3
 * @authors Treyvor Vice, Ann Le
 * This is a subclass that extends the Purchase superclass. In addition to the fields of a Purchase, each 
 * TowingPurchase contains a milesTowed float.
 */

public class TowingPurchase extends Purchase {

	private float milesTowed;
	
	public float getMilesTowed() {
		return this.milesTowed;
	}

	public TowingPurchase(Product product, float milesTowed) {
		super(product);
		this.milesTowed = milesTowed;
	}

	protected float getPurchaseCost() {
		float subtotal = this.milesTowed * ((Towing)this.getProduct()).getCostPerMile();
		return subtotal;
	}
}
