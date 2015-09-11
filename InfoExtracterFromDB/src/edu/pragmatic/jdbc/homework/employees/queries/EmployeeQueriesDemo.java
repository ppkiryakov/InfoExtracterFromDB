package edu.pragmatic.jdbc.homework.employees.queries;

import java.sql.SQLException;
import java.util.List;

import edu.pragmatic.jdbc.homework.employees.queries.EmployeeQueries.Employee;

public class EmployeeQueriesDemo {

	
	public static void main(String[] args) throws SQLException {
		EmployeeQueries queries = new EmployeeQueries();
		printEmployees(queries.getAllEmployees());
		printEmployees(queries.getAllEmployeesOrderedByFirstName());
		printEmployees(queries.getAllEmployeesAndDepartment());
		printEmployees(queries.getAllEmployeesAndTheirManager());
	}

	private static void printEmployees(List<Employee> employees) {
		for (Object employee : employees) 
			System.out.println(employee);
	}
}
