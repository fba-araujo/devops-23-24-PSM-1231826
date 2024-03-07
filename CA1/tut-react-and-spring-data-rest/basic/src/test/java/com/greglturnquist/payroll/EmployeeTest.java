package com.greglturnquist.payroll;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {
    @Test
    void createValidEmployee(){
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 0;
        String email = "frodo@lotr.com";
        assertDoesNotThrow(() -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void tryToCreateEmployeeNullFirstName() {
        String firstName = null;
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 1;
        String email = "frodo@lotr.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void tryToCreateEmployeeEmptyFirstName() {
        String firstName = "";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 1;
        String email = "frodo@lotr.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void tryToCreateEmployeeWhitespaceFirstName() {
        String firstName = "   ";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 1;
        String email = "frodo@lotr.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void tryToCreateEmployeeNullLastName() {
        String firstName = "Frodo";
        String lastName = null;
        String description = "Ring Bearer";
        int jobYears = 1;
        String email = "frodo@lotr.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void tryToCreateEmployeeEmptyLastName() {
        String firstName = "Frodo";
        String lastName = "";
        String description = "Ring Bearer";
        int jobYears = 1;
        String email = "frodo@lotr.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void tryToCreateEmployeeWhitespaceLastName() {
        String firstName = "Frodo";
        String lastName = "   ";
        String description = "Ring Bearer";
        int jobYears = 1;
        String email = "frodo@lotr.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void tryToCreateEmployeeNullDescription() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = null;
        int jobYears = 1;
        String email = "frodo@lotr.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void tryToCreateEmployeeEmptyDescription() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "";
        int jobYears = 1;
        String email = "frodo@lotr.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void tryToCreateEmployeeWhitespaceDescription() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "  ";
        int jobYears = 1;
        String email = "frodo@lotr.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void tryToCreateEmployeeNegativeJobYears() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = -1;
        String email = "frodo@lotr.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void tryToCreateEmployeeNullEmail() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 1;
        String email = null;
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void tryToCreateEmployeeEmptyEmail() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 1;
        String email = "";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void tryToCreateEmployeeWhitespaceEmail() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 1;
        String email = "    ";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }
}