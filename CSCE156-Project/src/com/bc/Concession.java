package com.bc;

public class Concession extends Products {
	
	private float cost;

	public float getCost() {
		return cost;
	}
	
	public Concession(String code, String type, String label, float cost) {
		super(code, type, label);
		this.cost = cost;
	}
}
