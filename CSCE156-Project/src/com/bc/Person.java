package com.bc;

/*
 * Date: 10/4/2020
 * CSCE 156, Assignment 3
 * @authors Treyvor Vice, Ann Le
 * This class stores objects of the type Person. Every Person contains a code, firstName, lastName, address, 
 * and an array of emails.
 */

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
		this.code = code;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.emails = emails;
	}
	
	public String getLastFirstName() {
		String name = this.lastName + "," + this.firstName;
		return name;
	}
	
	public String printEmails () {
		String emails = "";
		int check = 0;
		for (String s : this.emails) {
			if (check == 0) {
				emails += s;
			}
			else {
				emails += ", " + s;
			}
			check++;
		}
		return emails;
	}
}