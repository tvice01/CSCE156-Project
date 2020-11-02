package com.bc;

/*
 * Date: 10/4/2020
 * CSCE 156, Assignment 3
 * @authors Treyvor Vice, Ann Le
 * This is a subclass that extends the Product superclass. In addition to the fields of a Product, each Concession 
 * contains a cost.
 */

public class Concession extends Product {
	
	private float cost;

	public float getCost() {
		return cost;
	}
	
	public Concession(String code, String type, String label, float cost) {
		super(code, type, label);
		this.cost = cost;
	}
}
