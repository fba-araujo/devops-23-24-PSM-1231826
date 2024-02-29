# Class Assigment 1
## Part 1
This tutorial walks through enhancing an existing React.js and Spring Data REST application by adding a new field to record the years of service for each employee in the company.

## Table of Contents
- [Introduction](#introduction)
- [Step 1: Updating Employee Model](#step-1-updating-employee-model)
- [Step 2: Updating DatabaseLoader](#step-2-updating-databaseloader)
- [Step 3: Updating UI Components](#step-3-updating-ui-components)
- [Step 4: Attribute Validation with Unit Testing](#step-4-attribute-validation-with-unit-testing)
- [Step 5: Data Consistency](#step-5-data-consistency)

## Introduction

In this tutorial, we'll be working with an existing React.js and Spring Data REST application. The goal is to add a new field to the Employee model to track the years of service of each employee. Additionally, we'll implement attribute validation with unit testing to ensure data integrity and debug both server and client parts of the solution to enforce data consistency.

## Step 1: Updating Employee Model
Update the `Employee.java` class:
```java
Added private int jobYears
Updated empty constructor to public
Updated parameterized constructor to accept int jobYears and initialized it using this.jobYears = jobYears;
Modified equals() method to compare jobYears field
Updated hashCode() method to include jobYears
Modified toString() method to include jobYears
Added getter and setter for jobYears
```

## Step 2:  Updating DatabaseLoader
Next, we need to update the DatabaseLoader.java to ensure that jobYears field is included when initializing employee data.
```java 
Modified run() method to include jobYears when initializing employees
```

## Step 3: Updating UI Components
Update UI components to display the job years of each employee:
```javascript
Updated EmployeeList component to display a table header for job years
Updated Employee component to display job years within the table data
```

## Step 3: Attribute Validation with Unit Testing
Update the Employee.java class:
```java
Added checkIfEmployeeAttributes() method to validate input attributes
Modified parameterized constructor to throw InstantiationException if attributes are invalid
```
Create EmployeeTest.java

Created tests to validate employee creation with various scenarios:
```java
Valid employee creation
Invalid scenarios such as null or empty strings for first name, last name, and description
Negative job years
```

## Step 5: Data Consistency
Debug both server and client parts of the solution:
Invalid Employee Creation: The program will fail to build if an invalid employee is added via DatabaseLoader.
Server-side Validation: Attempting to add an employee with invalid attributes using curl doesn't fail, which means a weakness in the data integrity.
- curl -X POST localhost:8080/api/employees -d "{\"firstName\": \"Peregrin\", \"lastName\": \"Took\", \"description\": \"Pipe Smoker\", \"jobYears\": \"-3\"}" -H "Content-Type:application/json"

By following these steps, we've successfully enhanced our React.js and Spring Data REST application with a new feature and ensured data consistency through attribute validation and debugging.
