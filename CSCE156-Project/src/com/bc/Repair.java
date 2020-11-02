package com.bc;

/*
 * Date: 10/4/2020
 * CSCE 156, Assignment 3
 * @authors Treyvor Vice, Ann Le
 * This is a subclass that extends the Product superclass. In addition to the fields of a Product, each Repair contains
 * a partsCost and a laborRate.
 */

public class Repair extends Product {
	
	private float partsCost;
	private float laborRate;
	
	public float getPartsCost() {
		return partsCost;
	}
	public float getLaborRate() {
		return laborRate;
	}
	
	public Repair(String code, String type, String label, float partsCost, float laborRate) {
		super(code, type, label);
		this.partsCost = partsCost;
		this.laborRate = laborRate;
	}
}
