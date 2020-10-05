package com.bc;

/*
 * Date: 10/4/2020
 * CSCE 156, Assignment 3
 * @authors Treyvor Vice, Ann Le
 * This class stores objects of the type Purchase, and is a superclass for the RentalPurchase, RepairPurchase, 
 * ConcessionPurchase, and TowingPurchase classes. Every Purchase contains a product.
 */

public abstract class Purchase {
	
	private Products product;

	public Products getProduct() {
		return this.product;
	}
	
	public Purchase(Products product) {
		this.product = product;
	}
	
	protected abstract float getPurchaseCost();
}
