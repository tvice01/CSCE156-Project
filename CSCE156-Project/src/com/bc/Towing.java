package com.bc;

/*
 * Date: 10/4/2020
 * CSCE 156, Assignment 3
 * @authors Treyvor Vice, Ann Le
 * This is a subclass that extends the Products superclass. In addition to the fields of a Product, each Towing object 
 * contains a costPerMile.
 */

public class Towing extends Products {
	
	private float costPerMile;

	public float getCostPerMile() {
		return costPerMile;
	}
	
	public Towing(String code, String type, String label, float costPerMile) {
		super(code, type, label);
		this.costPerMile = costPerMile;
	}
}
