package com.bc;

public class Person {
	
	private String code;
	private String firstName;
	private String lastName;
	private Address address;
	private String[] emails;
	
	public String getCode() {
		return code;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public Address getAddress() {
		return address;
	}
	public String[] getEmailAddresses() {
		return emails;
	}
	
	public Person(String code, String firstName, String lastName, Address address, String[] emails) {
		super();
		this.code = code;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.emails = emails;
	}
	
	
	
}