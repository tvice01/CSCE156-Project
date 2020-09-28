package com.bc;

public abstract class Products {
	
	private String code;
	private String type;
	private String label;
		
	public String getCode() {
		return code;
	}
	public String getType() {
		return type;
	}
	public String getLabel() {
		return label;
	}
	
	public Products(String code, String type, String label) {
		this.code = code;
		this.type = type;
		this.label = label;
	}

}