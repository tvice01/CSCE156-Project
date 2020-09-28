package com.bc;

public class Repair extends Products {
	
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
