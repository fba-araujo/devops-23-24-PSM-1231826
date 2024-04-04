/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.greglturnquist.payroll;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * Represents an employee in the system.
 * An employee has a first name, last name, description, job years, and email.
 * Each employee is uniquely identified by an ID.
 * Provides methods for validation, comparison, and string representation.
 *
 * @author Greg Turnquist
 */
// tag::code[]
@Entity // <1>
public class Employee {

    private @Id
    @GeneratedValue Long id; // <2>
    private String firstName;
    private String lastName;
    private String description;
    private int jobYears;
    private String email;

    /**
     * Constructs a new employee with default values.
     */
    public Employee() {
    }

    /**
     * Constructs a new employee with the provided details.
     * Throws InstantiationException if any attribute is invalid.
     *
     * @param firstName   The first name of the employee.
     * @param lastName    The last name of the employee.
     * @param description The description of the employee.
     * @param jobYears    The number of years the employee has worked.
     * @param email       The email address of the employee.
     * @throws InstantiationException If any attribute is invalid.
     */
    public Employee(String firstName, String lastName, String description, int jobYears, String email) throws InstantiationException {
        boolean employeeAttributeCheck = checkIfEmployeeAtributes(firstName, lastName, description, jobYears, email);
        if (!employeeAttributeCheck) {
            throw (new InstantiationException("Employee parameters invalid! Verify!"));
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
        this.jobYears = jobYears;
        this.email = email;
    }

    /**
     * Checks if the provided employee attributes are valid.
     *
     * @param firstName   The first name of the employee.
     * @param lastName    The last name of the employee.
     * @param description The description of the employee.
     * @param jobYears    The number of years the employee has worked.
     * @param email       The email address of the employee.
     * @return true if all attributes are valid, false otherwise.
     */
    public boolean checkIfEmployeeAtributes(String firstName, String lastName, String description, int jobYears, String email) {
        if (firstName == null || firstName.isBlank() || lastName == null || lastName.isBlank() || description == null || description.isBlank()) {
            return false;
        }
        if (email == null || !email.contains("@") || email.isBlank()) {
            return false;
        }
        if (jobYears < 0) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) &&
                Objects.equals(firstName, employee.firstName) &&
                Objects.equals(lastName, employee.lastName) &&
                Objects.equals(description, employee.description) &&
                Objects.equals(jobYears, employee.jobYears) &&
                Objects.equals(email, employee.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, description, jobYears, email);
    }

    /**
     * Returns the ID of the employee.
     *
     * @return The ID of the employee.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the employee.
     *
     * @param id The ID of the employee.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the first name of the employee.
     *
     * @return The first name of the employee.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the employee.
     *
     * @param firstName The first name of the employee.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the employee.
     *
     * @return The last name of the employee.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the employee.
     *
     * @param lastName The last name of the employee.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the description of the employee.
     *
     * @return The description of the employee.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the employee.
     *
     * @param description The description of the employee.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the number of years the employee has worked.
     *
     * @return The number of years the employee has worked.
     */
    public int getJobYears() {
        return jobYears;
    }

    /**
     * Sets the number of years the employee has worked.
     *
     * @param jobYears The number of years the employee has worked.
     */
    public void setJobYears(int jobYears) {
        this.jobYears = jobYears;
    }

    /**
     * Returns the email address of the employee.
     *
     * @return The email address of the employee.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the employee.
     *
     * @param email The email address of the employee.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", description='" + description + '\'' +
                ", jobYears='" + jobYears + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
// end::code[]
