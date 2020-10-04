package com.bc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;

/*
 * Date: 9/18/2020
 * CSCE 156, Assignment 2
 * @authors Treyvor Vice, Ann Le
 * This java class uses the parsers from Parsers.java to build ArrayLists of Products, Customers, and Persons and then
 * converts this data to the xml and json formats. It outputs a .xml and a .json file for each of the 3 .dat files.
 */

public class DataConverter {
	
	public static void xmlPersonsBuilder (ArrayList <Person> personsList) {
	//A method used for converting a Person ArrayList to xml format and printing this to an output file
		File outFile = new File ("data/Persons.xml");
		PrintWriter out;
		
		try {
			out = new PrintWriter(outFile);
			out.println("<?xml version=\"1.0\"?>\n<persons>");
			
			//Use XStream to convert each of the Persons in 'personsList' to xml and print to the output file
			XStream xstream = new XStream();
			xstream.alias("person", Person.class);
			
			for (Person p : personsList) {
				String xml = xstream.toXML (p);
				out.println(xml);
			}
			
			out.println("<persons>");
			out.close();
		}
		catch (FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}
	}
	
	public static void jsonPersonsBuilder (ArrayList<Person> personsList) {
	//A method used for converting a Person ArrayList to json format and printing this to an output file
		File outFile = new File("data/Persons.json");
		PrintWriter out;
		try {
			out = new PrintWriter(outFile);
			out.printf("{\n\"persons\": [\n");
			
			//Use GSON to convert each of the Persons in 'personsList' to json and print to the output file
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			int i = 0;
			for (Person p: personsList) {
				if (i == (personsList.size()-1)) {
					String json= gson.toJson(p);
					out.println(json);
				} 
				else {
					String json = gson.toJson(p) + ",";
					out.println(json);
				}
				i++;
			}
			out.printf("]}");
			out.close();
		}
		catch (FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}
	}
	
	public static void xmlCustomerBuilder(ArrayList<Customer> customerList) {
	//A method used for converting a Customer ArrayList to xml format and printing this to an output file
		
		File outFile = new File("data/Customers.xml");
		PrintWriter out;
		try {
			out = new PrintWriter(outFile);
			
			out.println("<?xml version=\"1.0\"?>\n<customers>");
			
			//Use XStream to convert each of the Customers in 'customerList' to xml and print to the output file
			XStream xstream = new XStream();
			xstream.alias("customer", Customer.class);
			for (Customer c : customerList) {
				String xml = xstream.toXML(c);
				out.println(xml);
			}
			
			out.println("</customers>");
			out.close();
		}
		catch (FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}
	}
	
	public static void jsonCustomerBuilder(ArrayList<Customer> customerList) {
	//A method used for converting a Customer ArrayList to xml format and printing this to an output file
		File outFile = new File("data/Customers.json");
		PrintWriter out;
		try {
			out = new PrintWriter(outFile);
			out.printf("{\n\"customers\": [\n");
			
			//Use GSON to convert each of the Customers in 'customerList' to json and print to the output file
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			int i = 0;
			for (Customer c : customerList) {
				if (i == (customerList.size()-1)) {
					String json = gson.toJson(c);
					out.println(json);
				}
				else {
					String json = gson.toJson(c) + ",";
					out.println(json);
				}
				i++;
			}
			out.printf("]}");
			out.close();
		}
		catch (FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}
	}
	
	
	public static void xmlProductsBuilder(ArrayList<Products> productsList) {
	//A method used for converting a Product ArrayList to xml format and printing this to an output file
		
		File outFile = new File("data/Products.xml");
		PrintWriter out;
		try {
			out = new PrintWriter(outFile);
			
			out.println("<?xml version=\"1.0\"?>");
			
			//Use XStream to convert each of the Products in 'productsList' to xml and print to the output file
			XStream xstream = new XStream();
			xstream.alias("product", Rental.class);
			xstream.alias("product", Repair.class);
			xstream.alias("product", Concession.class);
			xstream.alias("product", Towing.class);
			for (Products p : productsList) {
				String xml = xstream.toXML(p);
				out.println(xml);
			}
			out.close();
		}
		catch (FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}
	}
	
	public static void jsonProductsBuilder(ArrayList<Products> productsList) {
	//A method used for converting a Product ArrayList to json format and printing this to an output file
		File outFile = new File("data/Products.json");
		PrintWriter out;
		try {
			out = new PrintWriter(outFile);
			
			out.printf("{\n\"assets\": [\n");
			
			//Use GSON to convert each of the Products in 'productsList' to json and print to the output file
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			int i = 0;
			for (Products p : productsList) {
				if (i == (productsList.size()-1)) {
					String json = gson.toJson(p);
					out.println(json);
				}
				else {
					String json = gson.toJson(p) + ",";
					out.println(json);
				}
				i++;
			}
			
			out.printf("]}");
			
			out.close();
		}
		catch (FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}
	}

	public static void main(String[] args) {
		
		ArrayList<Person> personList = Parsers.parsePersonsList();
		ArrayList<Customer> customerList = Parsers.parseCustomerList(personList);
		ArrayList<Products> productsList = Parsers.parseProductsList();

		xmlPersonsBuilder(personList);
		xmlCustomerBuilder(customerList);
		xmlProductsBuilder(productsList);
		
		jsonPersonsBuilder(personList);
		jsonCustomerBuilder(customerList);
		jsonProductsBuilder(productsList);

	}

}
