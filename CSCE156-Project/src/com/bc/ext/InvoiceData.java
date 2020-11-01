package com.bc.ext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

 /* DO NOT change or remove the import statements beneath this.
 * They are required for the webgrader to run this phase of the project.
 * These lines may be giving you an error; this is fine. 
 * These are webgrader code imports, you do not need to have these packages.
 */
import com.bc.model.Concession;
import com.bc.model.Invoice;
import com.bc.model.Customer;
import com.bc.model.Towing;

import unl.cse.albums.DatabaseInfo;

import com.bc.model.Person;
import com.bc.model.Product;
import com.bc.model.Rental;
import com.bc.model.Repair;

/**
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 * 16 methods in total, add more if required.
 * Do not change any method signatures or the package name.
 * 
 * Adapted from Dr. Hasan's original version of this file
 * @author Chloe
 *
 */
public class InvoiceData {

	/**
	 * 1. Method that removes every person record from the database
	 */
	public static void removeAllPersons() {
		// A method that removes all members of the Person table (NOTE: does not drop the table, only empties it)
		
		// First, remove all records in Email table (b/c they reference Persons as foreign keys)
		emptyTable("Email");
		
		// Next, remove all records from PersonCustomer table (b/c they reference Persons as foreign keys)
		emptyTable("PersonCustomer");
		
		// Next, remove all records in Purchase table (b/c they reference Invoices which reference Persons as foreign keys)
		emptyTable("Purchase");
		
		// Now, remove all records in Invoice table (b/c they reference Persons as foreign keys)
		emptyTable("Invoice");
		
		// Finally, remove all records from Person table
		emptyTable("Person");
	}

	/**
	 * 2. Method to add a person record to the database with the provided data.
	 * 
	 * @param personCode
	 * @param firstName
	 * @param lastName
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 */
	public static void addPerson(String personCode, String firstName, String lastName, String street, String city, String state, String zip, String country) {
		// A method to add a new person record to the database
		
		// Check if necessary String arguments have values (personCode, firstName, lastName, street, and country) and throw exception if any are empty
		try {
			if (personCode==null || personCode.trim().isEmpty() || firstName==null || firstName.trim().isEmpty()
					|| lastName==null || lastName.trim().isEmpty() || street==null || street.trim().isEmpty()
					|| country==null || country.trim().isEmpty()) {
				throw new Exception();
			}
		} catch (Exception e) {
			System.out.println("RuntimeException: Missing values in one or more needed fields");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		// Check if a Person with this personCode already exists (using the getID function)
		int personID = getID("Person", personCode);
		if (personID != 0) {
			// If the person already exists in the table, exit the function
			return;
		}
		
		// Connect to database using connectToDatabase method in DatabaseInfo.java
		Connection conn = DatabaseInfo.connectToDatabase();
		
		// Get the address_id for the address if it exists, or create a new address and return the generated address_id if it doesn't
		// (using getAddressID and addAdress functions)
		int addressID = getAddressID(street, city, zip, state, country);
		if (addressID == 0) {
			addressID = addAddress(street, city, zip, state, country);
		}
		
		// Define query for creating the new person
		String query = "INSERT INTO Person (personCode, firstName, lastName, address_id) "
				+ "VALUES (?, ?, ?, ?)";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		// Execute query for given arguments to create new person record
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			ps.setString(2, firstName);
			ps.setString(3, lastName);
			ps.setInt(4, addressID);
			rs = ps.executeQuery();
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: Couldn't create new person record");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 3. Adds an email record corresponding person record corresponding to the
	 * provided <code>personCode</code>
	 * 
	 * @param personCode
	 * @param email
	 */
	public static void addEmail(String personCode, String email) {
		// A method to add associate an email with a given person record
		
		// Check if String arguments have values and throw exception if any are empty
		try {
			if (personCode==null || personCode.trim().isEmpty() || email==null || email.trim().isEmpty()) {
				throw new Exception();
			}
		} catch (Exception e) {
			System.out.println("RuntimeException: Missing values in one or more needed fields");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		// Connect to database using connectToDatabase method in DatabaseInfo.java
		Connection conn = DatabaseInfo.connectToDatabase();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		// Check if a person exists in the Person table with the given personCode. If it doesn't, exit the function
		int personID = getID("Person", personCode);
		if (personID == 0) {
			// Exit the function if the person doesn't exist in the database
			return;
		}
		
		// Define query to add this email record to Email table
		String query = "INSERT INTO Email (emailName, person_id) values (?, ?)";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, email);
			ps.setInt(2, personID);
			rs = ps.executeQuery();
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: Couldn't add new email record");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 4. Method that removes every customer record from the database
	 */
	public static void removeAllCusomters() {
		// A method that removes all members of the Person table (NOTE: does not drop the table, only empties it)
		
		// First, remove all records from PersonCustomer table (b/c they reference Customers as foreign keys)
		emptyTable("PersonCustomer");
		
		// Next, remove all records in Purchase table (b/c they reference Invoices which reference Customers as foreign keys)
		emptyTable("Purchase");
		
		// Now, remove all records in Invoice table (b/c they reference Customers as foreign keys)
		emptyTable("Invoice");
		
		// Finally, remove all records from Customer table
		emptyTable("Customer");
	}
	
	/**
	 * 5. Method to add a customer record to the database with the provided data
	 * 
	 * @param customerCode
	 * @param customerType
	 * @param primaryContactPersonCode
	 * @param name
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 */
	public static void addCustomer(String customerCode, String customerType, String primaryContactPersonCode, String name, String street, String city, String state, String zip, String country) {
		// A method to add a new customer record to the database
		
		// Check if necessary String arguments have values (personCode, firstName, lastName, street, and country) and throw exception if any are empty
		try {
			if (customerCode==null || customerCode.trim().isEmpty() || customerType==null || customerType.trim().isEmpty()
					|| primaryContactPersonCode==null || primaryContactPersonCode.trim().isEmpty() || name==null || name.trim().isEmpty() 
					|| street==null || street.trim().isEmpty() || country==null || country.trim().isEmpty()) {
				throw new Exception();
			}
		} catch (Exception e) {
			System.out.println("RuntimeException: Missing values in one or more needed fields");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		// Check if a Customer with this customerCode already exists. If so, exit the function
		int customerID = getID("Customer", customerCode);
		if (customerID == 0) {
			// If the customer already exists, exit the function
			return;
		}
		
		// Check if a Person with this personCode exists. Exit the function if it doesn't
		int personID = getID("Person", primaryContactPersonCode);
		if (personID == 0) {
			// If the person doesn't exist, exit the function
			return;
		}
		
		// Get the address_id for the address if it exists, or create a new address and return the generated address_id if it doesn't
		// (using getAddressID and addAdress functions)
		int addressID = getAddressID(street, city, zip, state, country);
		if (addressID == 0) {
			addressID = addAddress(street, city, zip, state, country);
		}
		
		// Connect to database using connectToDatabase method in DatabaseInfo.java
		Connection conn = DatabaseInfo.connectToDatabase();
		
		// Define query for creating the new customer
		String query = "INSERT INTO Customer (customerCode, customerName, customerType, address_id) "
				+ "VALUES (?, ?, ?, ?)";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		// Execute query for given arguments to create new customer record. Retrieve the generated customer_id
		try {
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, customerCode);
			ps.setString(2, name);
			ps.setString(3, customerType);
			ps.setInt(4, addressID);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			rs.next();
			customerID = rs.getInt(1);
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: Couldn't create new customer record");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		// Associate the primary contact with the new customer by adding new entry to PersonCustomer table
		query = "INSERT INTO PersonCustomer (person_id, customer_id) VALUES (?, ?)";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, personID);
			ps.setInt(2, customerID);
			rs = ps.executeQuery();
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: Couldn't associate person and customer");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 6. Removes all product records from the database
	 */
	public static void removeAllProducts() {
		// A method that removes all members of the Product table (NOTE: does not drop the table, only empties it)
				
		// First, remove all records in Purchase table (b/c they reference Product records as foreign keys)
		emptyTable("Purchase");
		
		// Now, remove all records from Product table
		emptyTable("Product");
	}

	/**
	 * 7. Adds a concession record to the database with the provided data.
	 * 
	 * @param productCode
	 * @param productLabel
	 * @param unitCost
	 */
	public static void addConcession(String productCode, String productLabel, double unitCost) {
		//A method that adds a concession product to the database
		
		//Check whether String arguments are empty, throw exception if so
		try {
			if (productCode==null || productCode.trim().isEmpty() || productLabel==null || productLabel.trim().isEmpty()) {
				throw new Exception();
			}
		} catch (Exception e) {
			System.out.println("RuntimeException: Missing values in one or more field(s)");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		// Check if a product with this productCode already exists (using the getID function)
		int productID = getID("Invoice", productCode);
		if (productID != 0) {
			// If the record is already being used, exit the function and do not insert it again
			return;
		}
		
		// Connect to database using connectToDatabase method in DatabaseInfo.java
		Connection conn = DatabaseInfo.connectToDatabase();
		
		// Create a concession product using the given arguments as appropriate fields
		String query = "INSERT INTO Product (productCode, productType, label, unitCost) " +
					   "VALUES (?, \"C\", ?, ?)";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, productCode);
			ps.setString(2, productLabel);
			ps.setFloat(3, (float)unitCost);
			rs = ps.executeQuery();
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: Couldn't create new concession");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 8. Adds a repair record to the database with the provided data.
	 * 
	 * @param productCode
	 * @param proudctLabel
	 * @param partsCost
	 * @param laborRate
	 */
	public static void addRepair(String productCode, String productLabel, double partsCost, double laborRate) {
		//A method that adds a repair product to the database
		
		//Check whether String arguments are empty, throw exception if so
		try {
			if (productCode==null || productCode.trim().isEmpty() || productLabel==null || productLabel.trim().isEmpty()) {
				throw new Exception();
			}
		} catch (Exception e) {
			System.out.println("RuntimeException: Missing values in one or more field(s)");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		// Check if a product with this productCode already exists (using the getID function)
		int productID = getID("Invoice", productCode);
		if (productID != 0) {
			// If the record is already being used, exit the function and do not insert it again
			return;
		}

		// Connect to database using connectToDatabase method in DatabaseInfo.java
		Connection conn = DatabaseInfo.connectToDatabase();
		
		// Create a repair product using the given arguments as appropriate fields
		String query = "INSERT INTO Product (productCode, productType, label, partsCost, hourlyLaborCost) " +
					   "VALUES (?, \"F\", ?, ?, ?)";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, productCode);
			ps.setString(2, productLabel);
			ps.setFloat(3, (float)partsCost);
			ps.setFloat(4, (float)laborRate);
			rs = ps.executeQuery();
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: Couldn't create new repair");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 9. Adds a towing record to the database with the provided data.
	 * 
	 * @param productCode
	 * @param productLabel
	 * @param costPerMile
	 */
	public static void addTowing(String productCode, String productLabel, double costPerMile) {
		//A method that adds a towing product to the database
		
		//Check whether String arguments are empty, throw exception if so
		try {
			if (productCode==null || productCode.trim().isEmpty() || productLabel==null || productLabel.trim().isEmpty()) {
				throw new Exception();
			}
		} catch (Exception e) {
			System.out.println("RuntimeException: Missing values in one or more field(s)");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		// Check if a product with this productCode already exists (using the getID function)
		int productID = getID("Invoice", productCode);
		if (productID != 0) {
			// If the record is already being used, exit the function and do not insert it again
			return;
		}
		
		// Connect to database using connectToDatabase method in DatabaseInfo.java
		Connection conn = DatabaseInfo.connectToDatabase();
		
		// Create a towing product using the given arguments as appropriate fields
		String query = "INSERT INTO Product (productCode, productType, label, costPerMile) " +
					   "VALUES (?, \"T\", ?, ?)";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, productCode);
			ps.setString(2, productLabel);
			ps.setFloat(3, (float)costPerMile);
			rs = ps.executeQuery();
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: Couldn't create new repair");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 10. Adds a rental record to the database with the provided data.
	 * 
	 * @param productCode
	 * @param productLabel
	 * @param dailyCost
	 * @param deposit
	 * @param cleaningFee
	 */
	public static void addRental(String productCode, String productLabel, double dailyCost, double deposit, double cleaningFee) {
        //A method that adds a rental product to the database
		
		//Check whether String arguments are empty, throw exception if so
		try {
			if (productCode==null || productCode.trim().isEmpty() || productLabel==null || productLabel.trim().isEmpty()) {
				throw new Exception();
			}
		} catch (Exception e) {
			System.out.println("RuntimeException: Missing values in one or more field(s)");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		// Check if a product with this productCode already exists (using the getID function)
		int productID = getID("Invoice", productCode);
		if (productID != 0) {
			// If the record is already being used, exit the function and do not insert it again
			return;
		}
		
		// Connect to database using connectToDatabase method in DatabaseInfo.java
		Connection conn = DatabaseInfo.connectToDatabase();
		
		// Create a rental product using the given arguments as appropriate fields
		String query = "INSERT INTO Product (productCode, productType, label, dailyCost, deposit, cleaningFee) " +
					   "VALUES (?, \"R\", ?, ?, ?, ?)";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, productCode);
			ps.setString(2, productLabel);
			ps.setFloat(3, (float)dailyCost);
			ps.setFloat(4, (float)deposit);
			ps.setFloat(5, (float)cleaningFee);
			rs = ps.executeQuery();
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: Couldn't create new repair");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 11. Removes all invoice records from the database
	 */
	public static void removeAllInvoices() {
		// A method that removes all members of the Invoice table (NOTE: does not drop the table, only empties it)
		
		// First, remove all records in Purchase table (b/c they reference Invoice records as foreign keys)
		emptyTable("Purchase");
		
		// Now, remove all records from Invoice table
		emptyTable("Invoice");
	}

	/**
	 * 12. Adds an invoice record to the database with the given data.
	 * 
	 * @param invoiceCode
	 * @param ownerCode
	 * @param customertCode
	 */
	public static void addInvoice(String invoiceCode, String ownerCode, String customerCode) {
		// A method that adds an invoice to the database
		
		//Check whether String arguments are empty, throw exception if so
		try {
			if (invoiceCode==null || invoiceCode.trim().isEmpty() || ownerCode==null || ownerCode.trim().isEmpty()
					|| customerCode==null || customerCode.trim().isEmpty()) {
				throw new Exception();
			}
		} catch (Exception e) {
			System.out.println("RuntimeException: Missing values in one or more field(s)");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		// Connect to database using connectToDatabase method in DatabaseInfo.java
		Connection conn = DatabaseInfo.connectToDatabase();
		
		// Check if an invoice with this invoiceCode already exists (using the getID function)
		int invoiceID = getID("Invoice", invoiceCode);
		if (invoiceID != 0) {
			// If the invoiceCode is already being used, exit the function and do not insert it again
			return;
		}
		
		// Create an invoice using the given arguments as appropriate fields
		String query = "INSERT INTO Invoice (invoiceCode, person_id, customer_id) VALUES " +
					   "(?, (select person_id from Person where personCode = ?), (select customer_id from Customer where customerCode = ?))";
		
    	PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, invoiceCode);
			ps.setString(2, ownerCode);
			ps.setString(3, customerCode);
			rs = ps.executeQuery();
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: Couldn't create new invoice");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 13. Adds a particular Towing (corresponding to <code>productCode</code>
	 * to an invoice corresponding to the provided <code>invoiceCode</code> with
	 * the given number of miles towed
	 * 
	 * @param invoiceCode
	 * @param productCode
	 * @param milesTowed
	 */
	public static void addTowingToInvoice(String invoiceCode, String productCode, double milesTowed) {
		// A method that adds a towing purchase to an invoice
		
		//Check whether String arguments are empty, throw exception if so
		try {
			if (invoiceCode==null || invoiceCode.trim().isEmpty() || productCode==null || productCode.trim().isEmpty()) {
				throw new Exception();
			}
		} catch (Exception e) {
			System.out.println("RuntimeException: Missing values in one or more field(s)");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		// Connect to database using connectToDatabase method in DatabaseInfo.java
		Connection conn = DatabaseInfo.connectToDatabase();
		
		// Make sure that both an invoice with this invoiceCode and a product with this productCode exist
		int invoiceID = getID("Invoice", invoiceCode);
		int productID = getID("Product", productCode);
		if (invoiceID == 0 || productID == 0) {
			// If the invoice or the product do not exist, exit the function
			return;
		}
		
		// Create a towing object in the Purchase table and link it to the provided invoiceCode
		String query = "INSERT INTO Purchase (invoice_id, product_id, milesTowed) VALUES " +
					   "((select invoice_id from Invoice where invoiceCode = ?), (select product_id from Product where productCode = ?), ?)";
		
    	PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, invoiceCode);
			ps.setString(2, productCode);
			ps.setFloat(3, (float)milesTowed);
			rs = ps.executeQuery();
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: Couldn't add towing purchase to invoice " + invoiceCode);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 14. Adds a particular Repair (corresponding to <code>productCode</code>
	 * to an invoice corresponding to the provided <code>invoiceCode</code> with
	 * the given number of hours worked
	 * 
	 * @param invoiceCode
	 * @param productCode
	 * @param hoursWorked
	 */
	public static void addRepairToInvoice(String invoiceCode, String productCode, double hoursWorked) {
		// A method that adds a repair purchase to an invoice
		
		//Check whether String arguments are empty, throw exception if so
		try {
			if (invoiceCode==null || invoiceCode.trim().isEmpty() || productCode==null || productCode.trim().isEmpty()) {
				throw new Exception();
			}
		} catch (Exception e) {
			System.out.println("RuntimeException: Missing values in one or more field(s)");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		// Connect to database using connectToDatabase method in DatabaseInfo.java
		Connection conn = DatabaseInfo.connectToDatabase();
		
		// Make sure that both an invoice with this invoiceCode and a product with this productCode exist
		int invoiceID = getID("Invoice", invoiceCode);
		int productID = getID("Product", productCode);
		if (invoiceID == 0 || productID == 0) {
			// If the invoice or the product do not exist, exit the function
			return;
		}
		
		// Create a repair object in the Purchase table and link it to the provided invoiceCode
		String query = "INSERT INTO Purchase (invoice_id, product_id, hoursWorked) VALUES " +
					   "((select invoice_id from Invoice where invoiceCode = ?), (select product_id from Product where productCode = ?), ?)";
		
    	PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, invoiceCode);
			ps.setString(2, productCode);
			ps.setFloat(3, (float)hoursWorked);
			rs = ps.executeQuery();
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: Couldn't add repair purchase to invoice " + invoiceCode);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

     /**
      * 15. Adds a particular Concession (corresponding to <code>productCode</code> to an 
      * invoice corresponding to the provided <code>invoiceCode</code> with the given
      * number of quantity.
      * NOTE: repairCode may be null
      * 
      * @param invoiceCode
      * @param productCode
      * @param quantity
      * @param repairCode
      */
    public static void addConcessionToInvoice(String invoiceCode, String productCode, int quantity, String repairCode) {
    	// A method that adds a concession purchase to an invoice
    	
    	//Check whether String arguments are empty (except for repairCode), throw exception if so
		try {
			if (invoiceCode==null || invoiceCode.trim().isEmpty() || productCode==null || productCode.trim().isEmpty()) {
				throw new Exception();
			}
		} catch (Exception e) {
			System.out.println("RuntimeException: Missing values in one or more field(s)");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		// Connect to database using connectToDatabase method in DatabaseInfo.java
		Connection conn = DatabaseInfo.connectToDatabase();
		
		// Make sure that both an invoice with this invoiceCode and a product with this productCode exist
		int invoiceID = getID("Invoice", invoiceCode);
		int productID = getID("Product", productCode);
		if (invoiceID == 0 || productID == 0) {
			// If either the invoice or the product do not exist, exit the function
			return;
		}
		
		// If repairCode is not null, check that a product with this code exists in the Product table
		if (repairCode != null) {
			productID = getID("Product", repairCode);
			if (productID == 0) {
				// If the repairCode is not a valid productCode, exit the function
				return;
			}
		}
		
		// Create a concession object in the Purchase table and link it to the provided invoiceCode
		String query = "INSERT INTO Purchase (invoice_id, product_id, hoursWorked) VALUES "
					   + "((select invoice_id from Invoice where invoiceCode = ?), (select product_id from Product where productCode = ?), "
					   + "?, (select product_id from Product where productCode = ?))";
		
    	PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, invoiceCode);
			ps.setString(2, productCode);
			ps.setInt(3, quantity);
			ps.setString(4, repairCode);
			rs = ps.executeQuery();
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: Couldn't add concession purchase to invoice " + invoiceCode);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
    }
	
    /**
     * 16. Adds a particular Rental (corresponding to <code>productCode</code> to an 
     * invoice corresponding to the provided <code>invoiceCode</code> with the given
     * number of days rented. 
     * 
     * @param invoiceCode
     * @param productCode
     * @param daysRented
     */
    public static void addRentalToInvoice(String invoiceCode, String productCode, double daysRented) {
    	// A method that adds a rental purchase to an invoice
    	
    	//Check whether String arguments are empty, throw exception if so
		try {
			if (invoiceCode==null || invoiceCode.trim().isEmpty() || productCode==null || productCode.trim().isEmpty()) {
				throw new Exception();
			}
		} catch (Exception e) {
			System.out.println("RuntimeException: Missing values in one or more field(s)");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		// Connect to database using connectToDatabase method in DatabaseInfo.java
		Connection conn = DatabaseInfo.connectToDatabase();
		
		// Make sure that both an invoice with this invoiceCode and a product with this productCode exist
		int invoiceID = getID("Invoice", invoiceCode);
		int productID = getID("Product", productCode);
		if (invoiceID == 0 || productID == 0) {
			// If the invoice or the product do not exist, exit the function
			return;
		}
		
		// Create a rental object in the Purchase table and link it to the provided invoiceCode
		String query = "INSERT INTO Purchase (invoice_id, product_id, hoursWorked) VALUES " +
					   "((select invoice_id from Invoice where invoiceCode = ?), (select product_id from Product where productCode = ?), ?)";
		
    	PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, invoiceCode);
			ps.setString(2, productCode);
			ps.setFloat(3, (float)daysRented);
			rs = ps.executeQuery();
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: Couldn't add rental purchase to invoice " + invoiceCode);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
    }
    
    public static void emptyTable(String table) {
		// A generic method that removes all records from the specified table 
    	// NOTE: does not DROP the table, only empties it of all records
		
		// Connect to database using connectToDatabase method in DatabaseInfo.java
		Connection conn = DatabaseInfo.connectToDatabase();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		// Build query
		String query = "DELETE FROM ?";
		
		// Execute query for specified table
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, table);
			rs = ps.executeQuery();
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: Couldn't remove table " + table);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
    
    public static int getID(String table, String code) {
    	// A generic method to return an id for a specific code if it exists in the specified table
    	// in the connected database. Returns 0 if the code does not yet exist in the table
    	
    	// Connect to database using connectToDatabase method in DatabaseInfo.java
    	Connection conn = DatabaseInfo.connectToDatabase();
    	
    	String idToSearchFor = null;
    	String codeToSearchFor = null;
    	
    	// Set idToSearchFor and codeToSearchFor according to which table is being queried
    	if (table.equals("Person")) {
    		idToSearchFor = "person_id";
    		codeToSearchFor = "personCode";
    	}
    	else if (table.equals("Customer")) {
    		idToSearchFor = "customer_id";
    		codeToSearchFor = "customerCode";
    	}
    	else if (table.equals("Invoice")) {
    		idToSearchFor = "invoice_id";
    		codeToSearchFor = "invoiceCode";
    	}
    	else if (table.equals("Product")) {
    		idToSearchFor = "product_id";
    		codeToSearchFor = "productCode";
    	}
    	else if (table.equals("State")) {
    		idToSearchFor = "state_id";
    		codeToSearchFor = "name";
    	}
    	else if (table.equals("Country")) {
    		idToSearchFor = "country_id";
    		codeToSearchFor = "name";
    	}
    	
    	int returnedID = 0;
    	String query = "SELECT ? FROM ? WHERE ? = ?";
		
    	PreparedStatement ps = null;
		ResultSet rs = null;
    	
		// Execute query with appropriate arguments. Update returnedID if a matching entry in the database is found
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, idToSearchFor);
			ps.setString(2, table);
			ps.setString(3, codeToSearchFor);
			ps.setString(4, code);
			rs = ps.executeQuery();
			if(rs.next()) {
				returnedID = rs.getInt(idToSearchFor);
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: could not search for code " + code + "in table " + table);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return returnedID;
    }
    
    public static int getAddressID (String street, String city, String zip, String state, String country) {
    	// A method to check if the given address exists in the Address table. Return the address_id if it exists, or return 0 if not.
    	
    	// Connect to database using connectToDatabase method in DatabaseInfo.java
    	Connection conn = DatabaseInfo.connectToDatabase();
    	
    	PreparedStatement ps = null;
		ResultSet rs = null;
		
		// Initialize addressID to 0 and define query
		int addressID = 0;
		String query = "SELECT address_id FROM Address WHERE street = ? AND city = ? AND zip = ? "
						+ "AND state_id = (SELECT state_id FROM State WHERE name = ?) "
						+ "AND country_id = (SELECT country_id FROM Country WHERE name = ?)";
    	
		// Execute query with given arguments. Set addressID to equal any returned address_id
    	try {
			ps = conn.prepareStatement(query);
			ps.setString(1, street);
			ps.setString(2, city);
			ps.setString(3, zip);
			ps.setString(4, state);
			ps.setString(5, country);
			rs = ps.executeQuery();
			if(rs.next()) {
				addressID = rs.getInt("address_id");
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: could not search for address");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
    	
    	return addressID;
    }
    
    public static int addAddress (String street, String city, String zip, String state, String country) {
    	// A method to create a new entry in the Address table. Returns the generated address_id
    	
    	int addressID = 0;
    	
    	// Connect to database using connectToDatabase method in DatabaseInfo.java
    	Connection conn = DatabaseInfo.connectToDatabase();
    	
    	PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		
		// Check if state is null. If not, check if the state exists in the State table and, if not, insert it
		if (state != null) {
	    	if (getID("State", state) == 0) {
	    		// Create a new state entry
	    		query = "INSERT INTO State (name) VALUES (?)";
	    		try {
	    			ps = conn.prepareStatement(query);
	    			ps.setString(1, state);
	    			rs = ps.executeQuery();
	    			rs.close();
	    		} catch (SQLException e) {
	    			System.out.println("SQLException: could not create new state " + state);
	    			e.printStackTrace();
	    			throw new RuntimeException(e);
	    		}
	    	}
		}
    	
    	// Check if the country exists in the Country table and, if not, insert it
    	if (getID("Country", country) == 0) {
    		// Create a new country entry
    		query = "INSERT INTO Country (name) VALUES (?)";
    		try {
    			ps = conn.prepareStatement(query);
    			ps.setString(1, country);
    			rs = ps.executeQuery();
    			rs.close();
    		} catch (SQLException e) {
    			System.out.println("SQLException: could not create new country " + country);
    			e.printStackTrace();
    			throw new RuntimeException(e);
    		}
    	}
    	
    	// Define the query depending on whether or not state is null
    	if (state!=null) {
    		query = "INSERT INTO Address (street, city, zip, country_id) VALUES "
    				+ "(?, ?, ?, (select country_id from Country where name = ?)";
    	}
    	else {
    		query = "INSERT INTO Address (street, city, zip, country_id, state_id) VALUES "
    				+ "(?, ?, ?,  (select country_id from Country where name = ?), (select state_id from State where name = ?)";
    	}
    	
    	// Execute query
    	try {
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, street);
			ps.setString(2, city);
			ps.setString(3, zip);
			ps.setString(4, country);
			if (state!=null) {
				ps.setString(5, state);
			}
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			rs.next();
			addressID = rs.getInt(1);
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: Couldn't create new address");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
    	
    	return addressID;
    }
    
}
