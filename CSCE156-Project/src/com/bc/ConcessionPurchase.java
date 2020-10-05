package com.bc;

/*
 * Date: 10/4/2020
 * CSCE 156, Assignment 3
 * @authors Treyvor Vice, Ann Le
 * This is a subclass that extends the Purchase superclass. In addition to the fields of a Purchase, each 
 * ConcessionPurchase contains a quantity and an associated repair (assocRepair).
 */

public class ConcessionPurchase extends Purchase {

	private int quantity;
	private String assocRepair;
	
	public int getQuantity() {
		return this.quantity;
	}
	public String getAssocRepair() {
		return this.assocRepair;
	}

	public ConcessionPurchase(Products product, int quantity, String assocRepair) {
		super(product);
		this.quantity = quantity;
		this.assocRepair = assocRepair;
	}

	public boolean getAssocRepairDiscount() {
		boolean discount = false;
		if (this.assocRepair.length() > 0) {
			discount = true;
		}
		return discount;
	}
	
	protected float getPurchaseCost() {
		float subtotal = ((Concession)this.getProduct()).getCost() * this.quantity;
		return subtotal;
	}
}
