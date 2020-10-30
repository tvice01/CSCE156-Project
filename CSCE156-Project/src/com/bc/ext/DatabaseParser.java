package com.bc.ext;

import java.util.ArrayList;

import com.bc.Customer;
import com.bc.Invoice;
import com.bc.Person;
import com.bc.Product;

/*
 * Date: 10/30/2020
 * CSCE 156
 * @authors Treyvor Vice, Ann Le
 * This class contains methods that parse the elements of tables in an SQL database into appropriate classes.
 * Each parser outputs an ArrayList of the appropriate class type.
 */

public class DatabaseParser {
	
	public static ArrayList<Person> parsePersonList(){
		// Connects to the database and generates an ArrayList of Persons (from the Person.java class)
		// Similar to parsePersonsList in Parser.java
		
		//TODO
	}
	
	public static ArrayList<Customer> parseCustomerList(ArrayList<Person> personList){
		// Connects to the database and generates an ArrayList of Customers (from the Customer.java class)
		// Similar to parseCustomerList in Parser.java
		
		//TODO
	}
	
	public static ArrayList<Product> parseProductList(){
		// Connects to the database and generates an ArrayList of Products (from the Product.java class)
		// Similar to parseProductsList in Parser.java
		
		//TODO
	}
	
	public static ArrayList<Invoice> parseInvoiceList(ArrayList<Person> personList,
			ArrayList<Customer> customerList, ArrayList<Product> productsList){
		// Connects to the database and generates an ArrayList of Invoices (from the Invoice.java class)
		// Similar to parseInvoiceList in Parser.java
		
		//TODO
	}
}
