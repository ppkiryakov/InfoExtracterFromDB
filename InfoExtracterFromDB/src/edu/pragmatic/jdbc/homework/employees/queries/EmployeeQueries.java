package edu.pragmatic.jdbc.homework.employees.queries;

import static edu.pragmatic.jdbc.homework.utils.SQLUtils.executeQuery;
import static edu.pragmatic.jdbc.homework.utils.SQLUtils.getAllEmployeesQuery;
import static edu.pragmatic.jdbc.homework.utils.SQLUtils.getConnection;
import static edu.pragmatic.jdbc.homework.utils.SQLUtils.getEmployeeOrderedByFirstNameQuery;
import static edu.pragmatic.jdbc.homework.utils.SQLUtils.getEmployeesAndTheirManagerQuery;
import static edu.pragmatic.jdbc.homework.utils.SQLUtils.getEmployeesNameAndDepartmentQuery;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeQueries {
	
	public static class Employee {
		private String name;
		private String lastName;
		private String department;
		private String manager;

		public Employee(String name, String lastName) {
			this(name, lastName, "", "");
		}
		
		public Employee(String name, String lastName, String department) {
			this(name, lastName, department, "");
		}
		
		public Employee(String name, String lastName, String department, String manager) {
			this.name = name;
			this.lastName = lastName;
			this.department = department;
			this.manager = manager;
		}
		
		@Override
		public String toString() {
			return String.format("%s %s %s %s", name, lastName, department, manager);
		}
		
	}
	
	private interface Builder<T> {
		T build(ResultSet info) throws SQLException;
	}
	
	private <T> List<T> buildModel(String sql, Builder<T> builder) throws SQLException{
		try (Connection connection = getConnection()){
			ResultSet info = executeQuery(connection , sql);
			List<T> employees = new ArrayList<>();
			while (info.next()){
				employees.add(builder.build(info));
			}
			return employees;
		}
	}
	
	private Builder<Employee> getSimpleEmployeInfoBuilder() {
		return new Builder<Employee>(){
			@Override
			public Employee build(ResultSet info) throws SQLException {
				String name = info.getString("first_name");
				String lastName = info.getString("last_name");
				return new Employee(name, lastName);
			}
		};
	}
	
	private Builder<Employee> getEmployeeWithDepartmentBuilder() {
		return new Builder<Employee>(){
			@Override
			public Employee build(ResultSet info) throws SQLException {
				String name = info.getString("first_name");
				String lastName = info.getString("last_name");
				String department = info.getString("department");
				return new Employee(name, lastName, department);
			}
		};
	}
	
	public List<Employee> getAllEmployees() throws SQLException{
		return buildModel(getAllEmployeesQuery(), getSimpleEmployeInfoBuilder());
	}
	
	public List<Employee> getAllEmployeesOrderedByFirstName() throws SQLException{
		return buildModel(getEmployeeOrderedByFirstNameQuery(), getSimpleEmployeInfoBuilder());
	}

	public List<Employee> getAllEmployeesAndDepartment() throws SQLException{
		return buildModel(getEmployeesNameAndDepartmentQuery(), getEmployeeWithDepartmentBuilder());
	}
	
	public List<Employee> getAllEmployeesAndTheirManager() throws SQLException{
		return buildModel(getEmployeesAndTheirManagerQuery(), new Builder<Employee>() {

			@Override
			public Employee build(ResultSet info) throws SQLException {
				String name = info.getString("first_name");
				String lastName = info.getString("last_name");
				String department = info.getString("department");
				String manager = info.getString("manager");
				return new Employee(name, lastName, department, manager);
			}
		});
	}
}
