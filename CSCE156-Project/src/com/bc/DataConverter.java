package com.bc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;

/*
 * Date: 9/18/2020
 * CSCE 156, Assignment 2
 * This java program parses contents of the Persons.dat, Customers.dat, & Products.dat files into appropriate classes and then
 * converts this data to the xml and json formats. It outputs a .xml and a .json file for each of the 3 .dat file.
 * @author Treyvor Vice, Ann Le
 */

public class DataConverter {
	
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
		
		ArrayList<Person> personList = parsePersonsList();
		ArrayList<Customer> customerList = parseCustomerList(personList);
		ArrayList<Products> productsList = parseProductsList();

		xmlPersonsBuilder(personList);
		xmlCustomerBuilder(customerList);
		xmlProductsBuilder(productsList);
		
		jsonPersonsBuilder(personList);
		jsonCustomerBuilder(customerList);
		jsonProductsBuilder(productsList);

	}

}
