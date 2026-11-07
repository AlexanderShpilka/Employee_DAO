package org.example;

import java.math.BigDecimal;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        try (DatabaseConnector databaseConnector = new DatabaseConnector()) {
            SchemaInitializer schemaInitializer = new SchemaInitializer(databaseConnector);
            schemaInitializer.createEmployeesTable();

            EmployeeDAO employeeDAO = new EmployeeDAO();

            System.out.println("Initial number of employees: " + employeeDAO.getAll().size());

            Employee addedEmployee = employeeDAO.add(new Employee(null, "Alex", 100, "Java dev", new BigDecimal("100500")));
            System.out.println(addedEmployee);
            employeeDAO.add(new Employee(null, "Max", 120, "JS dev", new BigDecimal("100500")));
            System.out.println("Number of employees: " + employeeDAO.getAll().size());

            Optional<Employee> alex = employeeDAO.getById(1);
            System.out.println(alex);

            Optional<Employee> updatedAlex = employeeDAO.update(new Employee(1, "Alex_updated", 100, "Java dev", new BigDecimal("100500")));
            System.out.println(updatedAlex);

            boolean wasDeleted = employeeDAO.deleteById(2);
            System.out.printf("Employee was deleted: %b%n", wasDeleted);

            System.out.println("Number of employees: " + employeeDAO.getAll().size());
        }
    }
}
