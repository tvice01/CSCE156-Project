package com.bc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Date: 10/4/2020
 * CSCE 156, Assignment 3
 * @authors Treyvor Vice, Ann Le
 * This class contains methods that parse the contents of the Persons.dat, Customers.dat, Products.dat, & Invoices.dat
 * files into appropriate classes. Each parser outputs an ArrayList of the appropriate class type.
 */

public class Parsers {

	public static ArrayList<Person> parsePersonsList(){
	//A method that builds a Person ArrayList from the contents of the 'Persons.dat' file
		
		//Open the "Persons.dat" file for scanning and throw an exception if not found
		String fileName = "data/Persons.dat";
		Scanner s = null;
		try {
			s = new Scanner (new File(fileName));
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}
		
		ArrayList<Person> personsList = new ArrayList<Person>();
		s.nextLine();
		
		//Parse the entries of each line into Persons and add them to the 'personsList' list
		while (s.hasNext()) {
			String line = s.nextLine();
			if (!line.trim().isEmpty()) {
				Person p = null;
				String tokens[] = line.split(";");
				String code = tokens[0];
				String name = tokens[1];
				String address = tokens[2];
				String emails[] = null;
				if (tokens.length == 4) {
					emails = tokens[3].split(",");
				}
				else {
					emails = new String[1]; 
					emails[0] = "";
				}
				
				String nameTokens[] = name.split(",");
				String firstName = nameTokens[1];
				String lastName = nameTokens[0];
				
				String addressTokens[] = address.split(",");
				String street = addressTokens[0];
				String city = addressTokens[1];
				String state = addressTokens[2];
				String zip = addressTokens[3];
				String country = addressTokens[4];
				
				Address a = new Address(street, city, state, zip, country);
				
				p = new Person(code, firstName, lastName, a, emails);
				
				personsList.add(p);
			}
		}
		
		s.close();
		
		return personsList;
	}
	
	public static ArrayList<Customer> parseCustomerList(ArrayList<Person> personList) {
	//A method that builds a Customer ArrayList from the contents of the 'Customer.dat' file
		
		//Open the "Customers.dat" file for scanning and throw an exception if not found
		String fileName = "data/Customers.dat";
		Scanner s = null;
		try {
			s = new Scanner(new File(fileName));
		} 
		catch (FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}
		
		ArrayList<Customer> customerList = new ArrayList<Customer>();
		s.nextLine();
				
		//Parse the entries of each line into Customers and add them to the 'customerList' list
		while (s.hasNext()) {
			String line = s.nextLine();
			if(!line.trim().isEmpty()) {
				Customer c = null;
				String tokens[] = line.split(";");
				String code = tokens[0];
				String type = tokens[1];
				String name = tokens[2];
				String contactCode = tokens[3];
				String address = tokens[4];
				
				Person primaryContact = null;
				for (Person p : personList) {
					if (p.getCode().equals(contactCode)) {
						primaryContact = p;
					}
				}
				
				String addressTokens[] = address.split(",");
				String street = addressTokens[0];
				String city = addressTokens[1];
				String state = addressTokens[2];
				String zip = addressTokens[3];
				String country = addressTokens[4];
				Address a = new Address(street, city, state, zip, country);
				
				c = new Customer(code, type, name, primaryContact, a);
				
				customerList.add(c);
			}
			
		}
		s.close();
		
		return customerList;
	 }
	
	public static ArrayList<Products> parseProductsList() {
	//A method that builds a Products ArrayList from the contents of the 'Products.dat' file
			
		//Open the "Products.dat" file for scanning and throw an exception if not found
		String fileName = "data/Products.dat";
		Scanner s = null;
		try {
			s = new Scanner(new File(fileName));
		} 
		catch (FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}
		
		ArrayList<Products> productsList = new ArrayList<Products>();
		s.nextLine();
				
		//Parse the entries of each line into Products and add them to the 'productsList' list
		while (s.hasNext()) {
			String line = s.nextLine();
			if(!line.trim().isEmpty()) {
				Products p = null;
				String tokens[] = line.split(";");
				String code = tokens[0];
				String type = tokens[1];
				String label = tokens[2];
				
				if (type.equals("R")) {
					float dailyCost = Float.parseFloat(tokens[3]);
					float deposit = Float.parseFloat(tokens[4]);
					float cleaningFee = Float.parseFloat(tokens[5]);
					p = new Rental(code, type, label, dailyCost, deposit, cleaningFee);
				}
				else if (type.equals("F")) {
					float partsCost = Float.parseFloat(tokens[3]);
					float laborRate = Float.parseFloat(tokens[4]);
					p = new Repair(code, type, label, partsCost, laborRate);
				}
				else if (type.equals("C")) {
					float cost = Float.parseFloat(tokens[3]);
					p = new Concession(code, type, label, cost);
				}
				else if (type.equals("T")) {
					float costPerMile = Float.parseFloat(tokens[3]);
					p = new Towing(code, type, label, costPerMile);
				}
				
				productsList.add(p);
			}
			
		}
		s.close();
		
		return productsList;
	}
	
	public static ArrayList<Invoice> parseInvoiceList(ArrayList<Person> personList,
			ArrayList<Customer> customerList, ArrayList<Products> productsList) {
	//A method that builds an Invoices ArrayList from the contents of the 'Invoices.dat' file
		
		//Open the file for scanning and throw an exception if not found
		String fileName = "data/Invoices.dat";
		Scanner s = null;
		try {
			s = new Scanner (new File(fileName));
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}
		
		ArrayList<Invoice> invoiceList = new ArrayList<Invoice>();
		s.nextLine();
		
		//Parse the entries of each line into Invoices and add them to the 'invoiceList' list
		while (s.hasNext()) {
			String line = s.nextLine();
			if (!line.trim().isEmpty()) {
				Invoice invoice = null;
				Person person = null;
				Customer customer = null;
				String tokens[] = line.split(";");
				String invoiceCode = tokens[0];
				
				// Iterate through personList to find a Person that matches tokens[1]
				for (Person p : personList) {
					if (tokens[1].equals(p.getCode())) {
						person = p;
					}
				}
				
				// Iterate through customerList to find a Customer that matches tokens[2]
				for (Customer c : customerList) {
					if (tokens[2].equals(c.getCode())) {
						customer = c;
					}
				}
				
				String purchTokens[] = tokens[3].split(",");
				
				//Parse entries of prodTokens[] as objects in the Purchase class and adds them to an array of Purchases
				ArrayList<Purchase> purchaseList = new ArrayList<Purchase>();
				
				for (int i=0; i<purchTokens.length; i++) {
					
					String purchSubTokens[] = purchTokens[i].split(":");
					
					for (Products p : productsList) {
						if (purchSubTokens[0].equals(p.getCode())) {
							Purchase e = null;
							if (p.getType().equals("R")) {
								int daysRented = Integer.parseInt(purchSubTokens[1]);
								e = new RentalPurchase(p, daysRented);
							}
							else if (p.getType().equals("F")) {
								float hoursWorked = Float.parseFloat(purchSubTokens[1]);
								e = new RepairPurchase(p, hoursWorked);
							}
							else if (p.getType().equals("C")) {
								int quantity = Integer.parseInt(purchSubTokens[1]);
								String assocRepair = "";
								if (purchSubTokens.length == 3) {
									assocRepair = purchSubTokens[2];
								}
								e = new ConcessionPurchase(p, quantity, assocRepair);
							}
							else if (p.getType().equals("T")) {
								float milesTowed = Float.parseFloat(purchSubTokens[1]);
								e = new TowingPurchase(p, milesTowed);
							}
							
							purchaseList.add(e);
						}
					}
				}
				
				invoice = new Invoice(invoiceCode, person, customer, purchaseList);
				invoiceList.add(invoice);
			}
		}
		
		s.close();
		return invoiceList;
	}
}
