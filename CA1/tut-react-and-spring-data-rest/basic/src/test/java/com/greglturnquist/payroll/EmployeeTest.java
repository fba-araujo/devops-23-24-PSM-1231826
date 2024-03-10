package com.greglturnquist.payroll;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Employee class to ensure proper instantiation and validation.
 * Each test method validates a specific scenario of creating an employee instance.
 * Tests cover various cases including valid input, null values, empty strings, whitespace, and negative job years.
 * Additionally, tests check for valid email addresses.
 * These tests help ensure the correctness of the Employee class's behavior and validation logic.
 *
 * @author Filipa Araújo
 */
class EmployeeTest {

    /**
     * Test to create a valid employee with all attributes provided.
     * No exception should be thrown.
     */
    @Test
    void createValidEmployee() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 0;
        String email = "frodo@lotr.com";
        assertDoesNotThrow(() -> new Employee(firstName, lastName, description, jobYears, email));
    }

    /**
     * Test to attempt creating an employee with a null first name.
     * An InstantiationException should be thrown.
     */
    @Test
    void tryToCreateEmployeeNullFirstName() {
        String firstName = null;
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 1;
        String email = "frodo@lotr.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    /**
     * Test to attempt creating an employee with an empty first name.
     * An InstantiationException should be thrown.
     */
    @Test
    void tryToCreateEmployeeEmptyFirstName() {
        String firstName = "";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 1;
        String email = "frodo@lotr.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    /**
     * Test to attempt creating an employee with a whitespace first name.
     * An InstantiationException should be thrown.
     */
    @Test
    void tryToCreateEmployeeWhitespaceFirstName() {
        String firstName = "   ";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 1;
        String email = "frodo@lotr.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    /**
     * Test to attempt creating an employee with a null last name.
     * An InstantiationException should be thrown.
     */
    @Test
    void tryToCreateEmployeeNullLastName() {
        String firstName = "Frodo";
        String lastName = null;
        String description = "Ring Bearer";
        int jobYears = 1;
        String email = "frodo@lotr.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    /**
     * Test to attempt creating an employee with an empty last name.
     * An InstantiationException should be thrown.
     */
    @Test
    void tryToCreateEmployeeEmptyLastName() {
        String firstName = "Frodo";
        String lastName = "";
        String description = "Ring Bearer";
        int jobYears = 1;
        String email = "frodo@lotr.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    /**
     * Test to attempt creating an employee with a whitespace last name.
     * An InstantiationException should be thrown.
     */
    @Test
    void tryToCreateEmployeeWhitespaceLastName() {
        String firstName = "Frodo";
        String lastName = "   ";
        String description = "Ring Bearer";
        int jobYears = 1;
        String email = "frodo@lotr.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    /**
     * Test to attempt creating an employee with a null description.
     * An InstantiationException should be thrown.
     */
    @Test
    void tryToCreateEmployeeNullDescription() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = null;
        int jobYears = 1;
        String email = "frodo@lotr.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    /**
     * Test to attempt creating an employee with an empty description.
     * An InstantiationException should be thrown.
     */
    @Test
    void tryToCreateEmployeeEmptyDescription() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "";
        int jobYears = 1;
        String email = "frodo@lotr.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    /**
     * Test to attempt creating an employee with a whitespace description.
     * An InstantiationException should be thrown.
     */
    @Test
    void tryToCreateEmployeeWhitespaceDescription() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "  ";
        int jobYears = 1;
        String email = "frodo@lotr.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    /**
     * Test to attempt creating an employee with a negative job years value.
     * An InstantiationException should be thrown.
     */
    @Test
    void tryToCreateEmployeeNegativeJobYears() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = -1;
        String email = "frodo@lotr.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    /**
     * Test to attempt creating an employee with a null email.
     * An InstantiationException should be thrown.
     */
    @Test
    void tryToCreateEmployeeNullEmail() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 1;
        String email = null;
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    /**
     * Test to attempt creating an employee with an empty email.
     * An InstantiationException should be thrown.
     */
    @Test
    void tryToCreateEmployeeEmptyEmail() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 1;
        String email = "";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    /**
     * Test to attempt creating an employee with a whitespace email.
     * An InstantiationException should be thrown.
     */
    @Test
    void tryToCreateEmployeeWhitespaceEmail() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 1;
        String email = "    ";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    /**
     * Test to attempt creating an employee with an invalid email format (no '@' symbol).
     * An InstantiationException should be thrown.
     */
    @Test
    void tryToCreateEmployeeEmailNoAtSign() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 1;
        String email = "frodolotr.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }
}