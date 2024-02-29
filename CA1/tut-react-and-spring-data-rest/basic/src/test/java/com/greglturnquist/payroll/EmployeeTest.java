package com.greglturnquist.payroll;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {
    @Test
    void createValidEmployee() throws InstantiationException {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 0;
        Employee employee = new Employee(firstName, lastName, description, jobYears);
        assertNotNull(employee);
    }

    @Test
    void tryToCreateEmployeeNullFirstName() {
        String firstName = null;
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 1;
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears));
    }

    @Test
    void tryToCreateEmployeeEmptyFirstName() {
        String firstName = "";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 1;
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears));
    }

    @Test
    void tryToCreateEmployeeWhitespaceFirstName() {
        String firstName = "   ";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 1;
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears));
    }

    @Test
    void tryToCreateEmployeeNullLastName() {
        String firstName = "Frodo";
        String lastName = null;
        String description = "Ring Bearer";
        int jobYears = 1;
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears));
    }

    @Test
    void tryToCreateEmployeeEmptyLastName() {
        String firstName = "Frodo";
        String lastName = "";
        String description = "Ring Bearer";
        int jobYears = 1;
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears));
    }

    @Test
    void tryToCreateEmployeeWhitespaceLastName() {
        String firstName = "Frodo";
        String lastName = "   ";
        String description = "Ring Bearer";
        int jobYears = 1;
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears));
    }

    @Test
    void tryToCreateEmployeeNullDescription() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = null;
        int jobYears = 1;
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears));
    }

    @Test
    void tryToCreateEmployeeEmptyDescription() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "";
        int jobYears = 1;
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears));
    }

    @Test
    void tryToCreateEmployeeWhitespaceDescription() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "  ";
        int jobYears = 1;
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears));
    }

    @Test
    void tryToCreateEmployeeNegativeJobYears() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = -1;
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears));
    }
}