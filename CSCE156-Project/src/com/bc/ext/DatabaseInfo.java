package com.bc.ext;

public class DatabaseInfo {

	/**
	 * Connection parameters that are necessary for CSE's configuration
	 */
	public static final String PARAMETERS = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

	public static final String USERNAME = "tvice";
	public static final String PASSWORD = "4M8OcroP";
	public static final String URL = "jdbc:mysql://cse.unl.edu/" + USERNAME + PARAMETERS;
	
}
