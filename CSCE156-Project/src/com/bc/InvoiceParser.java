package com.bc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


/*
 * WORK IN PROGRESS
 * THIS CLASS IS INCOMPLETE
 * 
 * when finished, InvoiceParser will be added to the Parsers.java class
 */

public class InvoiceParser {
	
	public static ArrayList<Invoice> parseInvoiceList(ArrayList<Person> personList,
			ArrayList<Customer> customerList, ArrayList<Products> productsList) {
		
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
		
		while (s.hasNext()) {
			String line = s.nextLine();
			if (!line.trim().isEmpty()) {
				Invoice invoice = null;
				String tokens[] = line.split(";");
				String invoiceCode = tokens[0];
				String personCode = tokens[1];
				String customerCode = tokens[2];
				String purchTokens[] = tokens[3].split(",");
				
			//Parses prodTokens[] as objects in the Purchase class and add to an array of Purchases
				ArrayList<Purchase> purchaseList = new ArrayList<Purchase>();
				
				for (int i=0; i<purchTokens.length; i++) {
					
					String purchSubTokens[] = purchTokens[i].split(":");
					
					for (Products p : productsList) {
						if (purchSubTokens[0].equals(p.getCode())) {
							Purchase e = null;
							if (p.getType().equals("F")) {
								int daysRented = Integer.parseInt(purchSubTokens[1]);
								e = new RentalPurchase(p, daysRented);
							}
							else if (p.getType().equals("R")) {
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
				
				invoice = new Invoice(invoiceCode, personCode, customerCode, purchaseList);
				invoiceList.add(invoice);
			}
		}
		
		s.close();
		return invoiceList;
	}

	public static void main(String[] args) {
	// Used to test the InvoiceParser method written above
		ArrayList<Person> personList = Parsers.parsePersonsList();
		ArrayList<Customer> customerList = Parsers.parseCustomerList(personList);
		ArrayList<Products> productsList = Parsers.parseProductsList();
		
		ArrayList<Invoice> invoiceList = parseInvoiceList(personList, customerList, productsList);
		
		for (Invoice p : invoiceList) {
			System.out.println("\n" + p.getInvoiceCode());
			System.out.println(p.getPersonCode());
			System.out.println(p.getCustomerCode());
			for (Purchase c : p.getPurchaseList()) {
				System.out.println(c.getProduct().getLabel());
			}
		}
	}

}
