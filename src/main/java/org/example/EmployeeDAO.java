package org.example;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class EmployeeDAO {
    public Employee add(Employee employee) {
        Objects.requireNonNull(employee, "Employee must not be null");

        if (employee.id() != null) {
            throw new IllegalArgumentException("Employee must not contain id.");
        }

        String sql = "INSERT INTO employees (name, age, position, salary) VALUES (?, ?, ?, ?) RETURNING *";

        try (
                DatabaseConnector databaseConnector = new DatabaseConnector();
                PreparedStatement preparedStatement = databaseConnector.getConnection().prepareStatement(sql)
        ) {
            preparedStatement.setString(1, employee.name());
            preparedStatement.setInt(2, employee.age());
            preparedStatement.setString(3, employee.position());
            preparedStatement.setBigDecimal(4, employee.salary());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return this.resultSetToEmployee(resultSet);
                }

                throw new IllegalStateException("Failed to insert a new employee to DB");
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to insert a new employee to DB", ex);
        }
    }

    public Optional<Employee> update(Employee employee) {
        Objects.requireNonNull(employee, "Employee must not be null");

        Integer employeeId = employee.id();
        if (employeeId == null) {
            throw new IllegalArgumentException("Employee id must not be null");
        }

        String sql = "UPDATE employees SET name = ?, age = ?, position = ?, salary = ? WHERE id = ? RETURNING *";

        try (
                DatabaseConnector databaseConnector = new DatabaseConnector();
                PreparedStatement preparedStatement = databaseConnector.getConnection().prepareStatement(sql)
        ) {
            preparedStatement.setString(1, employee.name());
            preparedStatement.setInt(2, employee.age());
            preparedStatement.setString(3, employee.position());
            preparedStatement.setBigDecimal(4, employee.salary());
            preparedStatement.setInt(5, employeeId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(this.resultSetToEmployee(resultSet));
                }

                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to update an employee with id " + employeeId, ex);
        }
    }

    public boolean deleteById(int id) {
        String sql = "DELETE FROM employees WHERE id = ?";

        try (
                DatabaseConnector databaseConnector = new DatabaseConnector();
                PreparedStatement preparedStatement = databaseConnector.getConnection().prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, id);

            int affectedRows = preparedStatement.executeUpdate();

            return affectedRows == 1;
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to delete an employee with id " + id, ex);
        }
    }

    public Optional<Employee> getById(int id) {
        String sql = "SELECT * FROM employees WHERE id = ?";

        try (DatabaseConnector databaseConnector = new DatabaseConnector();
                PreparedStatement preparedStatement = databaseConnector.getConnection().prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(this.resultSetToEmployee(resultSet));
                }

                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get an employee with id " + id, ex);
        }
    }

    public List<Employee> getAll() {
        String sql = "SELECT * FROM employees";

        try (
                DatabaseConnector databaseConnector = new DatabaseConnector();
                Statement statement = databaseConnector.getConnection().createStatement()
        ) {
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                List<Employee> employees = new ArrayList<>();
                Employee employee;

                while (resultSet.next()) {
                    employee = this.resultSetToEmployee(resultSet);
                    employees.add(employee);
                }

                return employees;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get employees", ex);
        }
    }

    private Employee resultSetToEmployee(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        int age = resultSet.getInt("age");
        String position = resultSet.getString("position");
        BigDecimal salary = resultSet.getBigDecimal("salary");

        return new Employee(id, name, age, position, salary);
    }
}
