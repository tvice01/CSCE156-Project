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
		/* TODO*/
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
		/* TODO*/
	}

	/**
	 * 3. Adds an email record corresponding person record corresponding to the
	 * provided <code>personCode</code>
	 * 
	 * @param personCode
	 * @param email
	 */
	public static void addEmail(String personCode, String email) {
		/* TODO*/
	}

	/**
	 * 4. Method that removes every customer record from the database
	 */
	public static void removeAllCusomters() {
		/* TODO*/
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
		/* TODO*/
	}
	
	/**
	 * 6. Removes all product records from the database
	 */
	public static void removeAllProducts() {
		/* TODO*/
		
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

		// Connect to database using connectToDatabase method in DatabaseInfo.java
		Connection conn = DatabaseInfo.connectToDatabase();
		
		// Check if a product with this productCode already exists (using the getProductID method)
		int productID = getProductID(conn, productCode);
		if (productID != 0) {
			// If the record is already being used, exit the function and do not insert it again
			return;
		}
		
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
			ps.executeUpdate();
			
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

		// Connect to database using connectToDatabase method in DatabaseInfo.java
		Connection conn = DatabaseInfo.connectToDatabase();
		
		// Check if a product with this productCode already exists (using the getProductID method)
		int productID = getProductID(conn, productCode);
		if (productID != 0) {
			// If the record is already being used, exit the function and do not insert it again
			return;
		}
		
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
			ps.executeUpdate();
			
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

		// Connect to database using connectToDatabase method in DatabaseInfo.java
		Connection conn = DatabaseInfo.connectToDatabase();
		
		// Check if a product with this productCode already exists (using the getProductID method)
		int productID = getProductID(conn, productCode);
		if (productID != 0) {
			// If the record is already being used, exit the function and do not insert it again
			return;
		}
		
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
			ps.executeUpdate();
			
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

		// Connect to database using connectToDatabase method in DatabaseInfo.java
		Connection conn = DatabaseInfo.connectToDatabase();
		
		// Check if a product with this productCode already exists (using the getProductID method)
		int productID = getProductID(conn, productCode);
		if (productID != 0) {
			// If the record is already being used, exit the function and do not insert it again
			return;
		}
		
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
			ps.executeUpdate();
			
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
        /* TODO*/
	}

	/**
	 * 12. Adds an invoice record to the database with the given data.
	 * 
	 * @param invoiceCode
	 * @param ownerCode
	 * @param customertCode
	 */
	public static void addInvoice(String invoiceCode, String ownerCode, String customerCode) {
		/* TODO*/
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
		/* TODO*/
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
		/* TODO*/
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
    	/* TODO*/
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
    	/* TODO*/
    }

    public static int getProductID(Connection conn, String productCode) {
    	// A method to return a product_id for a specific productCode if it exists in the connected database
    	// and return 0 if it does not yet exist
    	
    	int productID = 0;
    	
    	String query = "SELECT product_id FROM Product WHERE productCode = ?";
		
    	PreparedStatement ps = null;
		ResultSet rs = null;
    	
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, productCode);
			rs = ps.executeQuery();
			if(rs.next()) {
				productID = rs.getInt("product_id");
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: could not search for productCode");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return productID;
	}
}
