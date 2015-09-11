package edu.pragmatic.jdbc.homework.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

public final class SQLUtils {
	
	private static final String CONFIG_FILE = "resources/config.properties";
	private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
	private static final String FILE_PATH_ROOT = "resources/sql";
	public static final String GET_ALL_EMPLOYEES = FILE_PATH_ROOT + "/GetAllEmployees.sql";
	public static final String GET_ALL_EMPLOYEES_ORDERED_BY_FIRST_NAMA = FILE_PATH_ROOT + "/GetAllEmployeesOrderedByFirstName.sql";
	public static final String GET_EMPLOYEES_NAME_AND_DEPARTMENT = FILE_PATH_ROOT + "/GetEmployeesNamesAndDepartments.sql";
	public static final String GET_EMPLOYEES_AND_THEIR_MANAGER = FILE_PATH_ROOT + "/GetEmployeesAndTheirManagers.sql";
	private static boolean showLog = false;

	public static void showLog(){
		showLog = true;
	}
	
	public static void hideLog(){
		showLog = false;
	}
	
	private SQLUtils() {}
	
	private static Properties config = new Properties();
	
	static {
		try {
			config.load(new FileReader(new File(CONFIG_FILE)));
		} catch (IOException e) {
			throw new ConfigurationException("Cannot load config file [ " + CONFIG_FILE + "]", e);
		}
		String logginEnabledRaw = config.getProperty("showLog");
		if (logginEnabledRaw != null && ! logginEnabledRaw.isEmpty()){
			if (Boolean.valueOf(logginEnabledRaw)) showLog();
		}else hideLog();
		
	}
	
	public static String getQuerySQL(String fileName){
		try (Scanner sqlFile = new Scanner(new File(fileName))){
			StringBuilder sql = new StringBuilder();
			while (sqlFile.hasNext())
				sql.append(sqlFile.nextLine());
			String actualSQL = sql.toString();
			log(actualSQL);
			return actualSQL;
		} catch (FileNotFoundException e) {
			throw new ConfigurationException("Cannot find sql file @["+ fileName +"]", e);
		}
	}
	
	private static void log(String querySql){
		if (showLog)
			System.out.printf("Executing %s @ %s \n", querySql, getDBURL());
	}
	
	public static String getAllEmployeesQuery(){
		return getQuerySQL(GET_ALL_EMPLOYEES);
	}
	
	public static String getEmployeeOrderedByFirstNameQuery(){
		return getQuerySQL(GET_ALL_EMPLOYEES_ORDERED_BY_FIRST_NAMA);
	}
	
	public static String getEmployeesNameAndDepartmentQuery(){
		return getQuerySQL(GET_EMPLOYEES_NAME_AND_DEPARTMENT);
	}
	
	public static String getEmployeesAndTheirManagerQuery(){
		return getQuerySQL(GET_EMPLOYEES_AND_THEIR_MANAGER);
	}
	
	public static String getDBUser(){
		return config.getProperty("user");
	}
	
	public static String getPassword(){
		return config.getProperty("password");
	}
	
	public static String getDBURL(){
		return config.getProperty("url");
	}
	
	public static void loadDriver(){
		try {
			Class.forName(DRIVER_CLASS);
		} catch (ClassNotFoundException e) {
			throw new ConfigurationException("Cannot load driver["+ DRIVER_CLASS +"]", e);
		}
	}
	
	public static Connection getConnection(){
		try {
			Connection con = DriverManager.getConnection(getDBURL(), getDBUser(), getPassword());
			Properties conectionConfiguration = con.getClientInfo();
			conectionConfiguration.setProperty("allowMultiQueries", String.valueOf(true));
			con.setClientInfo(conectionConfiguration);
			return con;
		} catch (SQLException e) {
			throw new ConfigurationException("Cannot establish a connection to [ " + getDBURL() + "]", e);
		}
	}
	
	public static class ConfigurationException extends RuntimeException{
		private static final long serialVersionUID = 1L;

		public ConfigurationException(String errMsg, Throwable cause) {
			super(errMsg, cause);
		}
	}
	
	public static ResultSet executeQuery(Connection con, String sql) throws SQLException{
		Statement queryStatement = con.createStatement();
		boolean hasMutlitpeResults = queryStatement.execute(sql);
		if (hasMutlitpeResults)
			return queryStatement.getResultSet();
		else return null;
	}
}
