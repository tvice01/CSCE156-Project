package com.bc;

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
