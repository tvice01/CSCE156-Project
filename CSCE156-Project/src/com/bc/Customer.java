package com.bc;

public class Customer {
	
	private String code;
	private String type;
	private String name;
	private Person primaryContact;
	private Address address;
	
	public String getCode() {
		return code;
	}
	public String getType() {
		return type;
	}
	public String getName() {
		return name;
	}
	public Person getContact() {
		return primaryContact;
	}
	public Address getAddress() {
		return  address;
	}
	
	public Customer(String code, String type, String name, Person primaryContact, Address address) {
		super();
		this.code = code;
		this.type = type;
		this.name = name;
		this.primaryContact = primaryContact;
		this.address = address;
	}
	
}