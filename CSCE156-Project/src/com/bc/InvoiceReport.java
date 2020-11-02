package com.bc;

import java.util.ArrayList;

import com.bc.ext.DatabaseParser;

/*
 * Date: 11/2/2020
 * CSCE 156, Assignment 3
 * @authors Treyvor Vice, Ann Le
 * This java class outputs summary reports for objects in the Invoice class. The first report is an overview of
 * all Invoices, while the second report contains detailed information for each Invoice.
 */

public class InvoiceReport {
	public static void main(String[] args) {
		
		// Build Person, Customer, Product, and Invoice ArrayLists using methods from the DatabaseParser.java class
		ArrayList<Person> personList = DatabaseParser.parsePersonList();
		ArrayList<Customer> customerList = DatabaseParser.parseCustomerList(personList);
		ArrayList<Product> productList = DatabaseParser.parseProductList();
		ArrayList<Invoice> invoiceList = DatabaseParser.parseInvoiceList(personList, customerList, productList);
		
		
		// Output the Executive Summary Report (a short summary of each invoice entry)
		System.out.println("Executive Summary Report:");
		System.out.println("=========================");
		System.out.printf("%-10s %-20s %-20s %11s %11s %11s %11s %11s\n", "Code", "Owner", "Customer Account", "Subtotal", "Discounts", "Fees", "Taxes", "Total");
		System.out.println("-----------------------------------------------------------------------------------------------------------------");
		float subtotal=0, fees=0, taxes=0, discount=0, total=0;
		float subtotalTotals=0, feesTotals=0, taxesTotals=0, discountTotals=0, totalTotals=0;
		for(Invoice inv : invoiceList){
			System.out.printf("%-10s %-20s %-20s", inv.getInvoiceCode(), inv.getPerson().getLastFirstName(), inv.getCustomer().getName());
			subtotal = inv.calculateSubTotal();
			fees = inv.calculateFees();
			taxes = inv.calculateTax();
			discount = inv.calculatePreTaxDiscounts() + inv.calculatePostTaxDiscounts();
			total = inv.calculateTotalCost();
			subtotalTotals += subtotal;
			feesTotals += fees;
			taxesTotals += taxes;
			discountTotals += discount;
			totalTotals += total;
			System.out.printf(" $%10.2f $%10.2f $%10.2f $%10.2f $%10.2f\n", subtotal, -discount, fees, taxes, total);
		}
		System.out.println("==================================================================================================================");
		System.out.printf("%-52s $%10.2f $%10.2f $%10.2f $%10.2f $%10.2f\n\n\n\n", "TOTALS", subtotalTotals, -discountTotals, feesTotals, taxesTotals, totalTotals);


		// Output detailed reports for each Invoice entry
		System.out.println("Invoice Details:");
		for(Invoice inv: invoiceList){
			System.out.println("=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+");
			System.out.println("Invoice " + inv.getInvoiceCode());
			System.out.println("------------------------------------------");
			
			// Print the account owner's info
			System.out.println("Owner:");
			System.out.println("\t" + inv.getPerson().getLastFirstName());
			System.out.println("\t" + "[" + inv.getPerson().printEmails() + "]");
			System.out.println("\t" + inv.getPerson().getAddress().getStreet());
			System.out.println("\t" + inv.getPerson().getAddress().getCity() + ", " + inv.getPerson().getAddress().getState()
					+ ", " + inv.getPerson().getAddress().getCountry() + " " + inv.getPerson().getAddress().getZip());
			
			// Print the customer account info
			System.out.println("Customer:");
			System.out.printf("\t%s", inv.getCustomer().getName());
			if (inv.getCustomer().getType().equals("B")) {
				System.out.printf(" (Business Account)\n");
			}
			else {
				System.out.printf(" (Personal Account)\n");
			}
			System.out.println("\t" + inv.getCustomer().getAddress().getStreet());
			System.out.println("\t" + inv.getCustomer().getAddress().getCity() + ", " + inv.getCustomer().getAddress().getState()
					+ ", " + inv.getCustomer().getAddress().getCountry() + " " + inv.getCustomer().getAddress().getZip());
			
			// Print the product information for all product purchases on the invoice
			System.out.println("Product:");
			System.out.printf("  %-10s %10s %60s %10s %10s %10s\n", "Code", "Description", "Subtotal", "Discount", "Tax", "Total");
			System.out.println("  ------------------------------------------------------------------------------------------------------------------------");
			
			float invTotal = 0;
			float taxRate = (float) 0.08;
			// Sets the tax rate depending on whether the customer account is personal or business
			if (inv.getCustomer().getType().equals("B")) {
				taxRate = (float)0.0425;
			}
			
			for(Purchase p: inv.getPurchaseList()){
				String code = p.getProduct().getCode();
				String description1 = "";
				String description2 = "";
				float purchaseDiscount = 0;
				
				if(p.getProduct().getType().equals("R")) {
					description1 += p.getProduct().getLabel() + " (" + ((RentalPurchase)p).getDaysRented() + " day(s) @ $"
							+ ((Rental)p.getProduct()).getDailyCost() + "/day)";
					description2 += "(+ $" + ((Rental)p.getProduct()).getCleaningFee() + " cleaning fee, -$"
							+ ((Rental)p.getProduct()).getDeposit() + " deposit refund)";
				}
				else if (p.getProduct().getType().equals("F")) {
					description1 += p.getProduct().getLabel() + " (" + ((RepairPurchase)p).getHoursWorked() + " hour(s) of labor @ $"
							+ ((Repair)p.getProduct()).getLaborRate() + "/hour)";
					description2 += "(+ $" + ((Repair)p.getProduct()).getPartsCost() + "for parts)";
				}
				else if (p.getProduct().getType().equals("C")) {
					description1 += p.getProduct().getLabel() + " (" + ((ConcessionPurchase)p).getQuantity() + " unit(s) @ $"
							+ ((Concession)p.getProduct()).getCost() + "/unit)";
					
					// Apply loyalty discount if applicable
					if (((ConcessionPurchase)p).getAssocRepairDiscount()) {
						purchaseDiscount += ((ConcessionPurchase)p).getPurchaseCost()*0.1;
					}
					
				}
				else if (p.getProduct().getType().equals("T")) {
					description1 += p.getProduct().getLabel() + " (" + ((TowingPurchase)p).getMilesTowed() + " mile(s) @ $"
							+ ((Towing)p.getProduct()).getCostPerMile() + "/mile)";
					
					// Apply discount to towing if applicable
					int towingFlag=0, repairFlag=0, rentalFlag=0;
					for (Purchase purch : inv.getPurchaseList()) {
						if (purch.getProduct().getType().equals("T")) {
							towingFlag = 1;
						}
						if (purch.getProduct().getType().equals("R")) {
							repairFlag = 1;
						}
						if (purch.getProduct().getType().equals("F")) {
							rentalFlag = 1;
						}
					}
					int freeTowing = towingFlag + repairFlag + rentalFlag;
					if (freeTowing == 3) {
						purchaseDiscount += ((TowingPurchase)p).getPurchaseCost();
					}
				}
				
				float purchaseTax = (p.getPurchaseCost() - purchaseDiscount) * taxRate;
				float purchaseTotal = p.getPurchaseCost() - purchaseDiscount + purchaseTax;
				invTotal += purchaseTotal;
				
				System.out.printf("  %-10s %-60s $%10.2f $%10.2f $%10.2f $%10.2f\n", code, description1, p.getPurchaseCost(), -purchaseDiscount, purchaseTax, purchaseTotal);
				if (p.getProduct().getType().equals("F") || p.getProduct().getType().equals("R")) {
					System.out.printf("             %s\n", description2);
				}
				
			}
			System.out.println("===========================================================================================================================");
			System.out.printf("%-73s $%10.2f $%10.2f $%10.2f $%10.2f\n", "ITEM TOTALS", inv.calculateSubTotal(), -inv.calculatePreTaxDiscounts(), inv.calculateTax(), invTotal);
			if (inv.getCustomer().getType().equals("B")) {
				System.out.printf("%-109s $%10.2f\n", "BUSINESS ACCOUNT FEE", 75.50);
			}
			if (inv.calculatePostTaxDiscounts()>0) {
				System.out.printf("%-109s $%10.2f\n", "LOYAL CUSTOMER DISCOUNT (5% OFF)", -inv.calculatePostTaxDiscounts());
			}
			System.out.printf("%-109s $%10.2f\n", "GRAND TOTAL", inv.calculateTotalCost());
			
			System.out.printf("\n\n\t\t%s\n\n\n", "THANK YOU FOR DOING BUSINESS WITH US!");
		}
					
	}
}


