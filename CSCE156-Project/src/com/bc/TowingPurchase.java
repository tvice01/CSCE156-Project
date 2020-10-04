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
	public void setMilesTowed(float milesTowed) {
		this.milesTowed = milesTowed;
	}

	public TowingPurchase(Products product, float milesTowed) {
		super(product);
		this.milesTowed = milesTowed;
	}

}
