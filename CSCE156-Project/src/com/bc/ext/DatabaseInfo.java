package com.bc.ext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseInfo {

	/**
	 * Connection parameters that are necessary for CSE's configuration
	 */
	public static final String PARAMETERS = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

	public static final String USERNAME = "tvice";
	public static final String PASSWORD = "4M8OcroP";
	public static final String URL = "jdbc:mysql://cse.unl.edu/" + USERNAME + PARAMETERS;
	
	public static Connection connectToDatabase() {
		// A method that can be called to create a connection to the SQL database using 
		// the credentials above and returns the connection
		
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQLException: could not connect to database");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return conn;
	}
}
