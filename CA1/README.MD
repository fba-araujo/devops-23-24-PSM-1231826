# Class Assigment 1

## Introduction
In this tutorial, we'll be working with an existing React.js and Spring Data REST application. Adding new features to it.

## Table of Contents
- [Part 1](#Part-1)
- [Step 1: Updating Employee Model](#step-1-updating-employee-model)
- [Step 2: Updating DatabaseLoader](#step-2-updating-databaseloader)
- [Step 3: Updating UI Components](#step-3-updating-ui-components)
- [Step 4: Attribute Validation with Unit Testing](#step-4-attribute-validation-with-unit-testing)
- [Step 5: Data Consistency](#step-5-data-consistency)


- [Part 2](#Part-2)
- [Step 6: Updating Employee Model](#step-6-updating-employee-model)
- [Step 7: Updating DatabaseLoader](#step-7-updating-databaseloader)
- [Step 8: Updating UI Components](#step-8-updating-ui-components)
- [Step 9: Attribute Validation with Unit Testing](#step-9-attribute-validation-with-unit-testing)
- [Step 10: Data Consistency](#step-10-data-consistency)
- [Step 11: Bug Fixing](#step-11-bug-fixing)

- [Project Analysis](#Project-Analysis)


## Part 1
The goal is to add a new field to the Employee model to track the years of service of each employee. Additionally, we'll implement attribute validation with unit testing to ensure data integrity and debug both server and client parts of the solution to enforce data consistency.

### Step 1: Updating Employee Model
Update the `Employee.java` class:
```java
// Added attribute 
private int jobYears;

// Updated empty constructor to public
public Employee() {
}

// Updated parameterized constructor to accept int jobYears and initialized it using this.jobYears = jobYears
public Employee(String firstName, String lastName, String description, int jobYears){
    this.firstName = firstName;
    this.lastName = lastName;
    this.description = description;
    this.jobYears = jobYears;
}

// Modified equals() method to compare jobYears field
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Employee employee = (Employee) o;
    return Objects.equals(id, employee.id) &&
            Objects.equals(firstName, employee.firstName) &&
            Objects.equals(lastName, employee.lastName) &&
            Objects.equals(description, employee.description) &&
            Objects.equals(jobYears, employee.jobYears);
}

// Updated hashCode() method to include jobYears
public int hashCode() {
    return Objects.hash(id, firstName, lastName, description, jobYears);
}

// Modified toString() method to include jobYears
public String toString() {
    return "Employee{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", description='" + description + '\'' +
            ", jobYears='" + jobYears + '\'' +
            '}';
}

// Added getter and setter for jobYears
public int getJobYears() {
    return jobYears;
}

public void setJobYears(int jobYears) {
    this.jobYears = jobYears;
}
```

### Step 2:  Updating DatabaseLoader
Next, we need to update the DatabaseLoader.java to ensure that jobYears field is included when initializing employee data.
```java 
// Modified run() method to include jobYears when initializing employees
public void run(String... strings) throws Exception { // <4>
    this.repository.save(new Employee("Frodo", "Baggins", "Ring Bearer", 1));
    this.repository.save(new Employee("Bilbo", "Baggins", "Burglar", 7));
    this.repository.save(new Employee("Gandalf", "the Grey", "Wizard", 2000));
    this.repository.save(new Employee("Samwise", "Gamgee", "Gardener", 0));
}
```

### Step 3: Updating UI Components
Update UI components to display the job years of each employee:
```javascript
// Updated EmployeeList component to display a table header for job years
class EmployeeList extends React.Component {
    render() {
        const employees = this.props.employees.map(employee =>
            <Employee key={employee._links.self.href} employee={employee}/>
        );
        return (
            <table>
                <tbody>
                    <tr>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Description</th>
                        <th>Job Years</th>
                    </tr>
                    {employees}
                </tbody>
            </table>
        )
    }
}

// Updated Employee component to display job years within the table data
class Employee extends React.Component {
    render() {
        return (
            <tr>
                <td>{this.props.employee.firstName}</td>
                <td>{this.props.employee.lastName}</td>
                <td>{this.props.employee.description}</td>
                <td>{this.props.employee.jobYears}</td>
            </tr>
        )
    }
}
```

### Step 4: Attribute Validation with Unit Testing
Update the Employee.java class:
```java
// Added checkIfEmployeeAttributes() method to validate input attributes
public boolean checkIfEmployeeAtributes(String firstName, String lastName, String description, int jobYears, String email) {
    if (firstName == null || firstName.isBlank() || lastName == null || lastName.isBlank() || description == null || description.isBlank()) {
        return false;
    }
    if (jobYears < 0) {
        return false;
    }
    return true;
}

// Modified parameterized constructor to throw InstantiationException if attributes are invalid
public Employee(String firstName, String lastName, String description, int jobYears) throws InstantiationException {
    boolean employeeAtributeCheck = checkIfEmployeeAtributes(firstName, lastName, description, jobYears);
    if (!employeeAtributeCheck) {
        throw (new InstantiationException("Employee parameters invalid! Verify!"));
    }
    this.firstName = firstName;
    this.lastName = lastName;
    this.description = description;
    this.jobYears = jobYears;
    this.email = email;
}
```
Create EmployeeTest.java

Created tests to validate employee creation with various scenarios:
```java
// Valid employee creation
@Test
void createValidEmployee() {
    String firstName = "Frodo";
    String lastName = "Baggins";
    String description = "Ring Bearer";
    int jobYears = 0;
    assertDoesNotThrow(() -> new Employee(firstName, lastName, description, jobYears));
}

// Invalid scenarios such as null or empty strings for first name, last name, and description
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

// Negative job years
@Test
void tryToCreateEmployeeNegativeJobYears() {
    String firstName = "Frodo";
    String lastName = "Baggins";
    String description = "Ring Bearer";
    int jobYears = -1;
    assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears));
}
```

### Step 5: Data Consistency
#### Debug both server and client parts of the solution:
Checked the insertion of data by Inspecting the page, there being able to check the elements
``` 
           <table>
                <tbody>
                    <tr>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Description</th>
                        <th>Job Years</th>
                    </tr>
                    <tr>
                        <td>Frodo</td>
                        <td>Baggins</td>
                        <td>Ring Bearer</td>
                        <td>1</td>
                    </tr>
```
Invalid Employee Creation: The program will fail to build if an invalid employee is added via DatabaseLoader.
Server-side Validation: Attempting to add an employee with invalid attributes using curl doesn't fail, which means a weakness in the data integrity.
- curl -X POST localhost:8080/api/employees -d "{\"firstName\": \"Peregrin\", \"lastName\": \"Took\", \"description\": \"Pipe Smoker\", \"jobYears\": \"-3\"}" -H "Content-Type:application/json"

By following these steps, we've successfully enhanced our React.js and Spring Data REST application with a new feature and ensured data consistency through attribute validation and debugging.

## Part 2
This part of the tutorial will walk you through adding a new field to feature e-mail for each employee in the company.


### Step 6: Updating Employee Model
Update the `Employee.java` class:
```java
// Added attribute 
private String email;

// Updated parameterized constructor to accept String email and initialized it using this.email = email
public Employee(String firstName, String lastName, String description, int jobYears, String email) throws InstantiationException {
    boolean employeeAtributeCheck = checkIfEmployeeAtributes(firstName, lastName, description, jobYears, email);
    if (!employeeAtributeCheck) {
        throw (new InstantiationException("Employee parameters invalid! Verify!"));
    }
    this.firstName = firstName;
    this.lastName = lastName;
    this.description = description;
    this.jobYears = jobYears;
    this.email = email;
}

// Modified equals() method to compare email field
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

// Updated hashCode() method to include email
public int hashCode() {
    return Objects.hash(id, firstName, lastName, description, jobYears, email);
}

// Modified toString() method to include email
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

// Added getter and setter for email
public String getEmail() {
    return email;
}

public void setEmail(String email) {
    this.email = email;
}
```

### Step 7:  Updating DatabaseLoader
Next, we need to update the DatabaseLoader.java to ensure that jobYears field is included when initializing employee data.
```java 
// Modified run() method to include email when initializing employees
public void run(String... strings) throws Exception { // <4>
    this.repository.save(new Employee("Frodo", "Baggins", "Ring Bearer", 1, "frodobaggins@lotr.com"));
    this.repository.save(new Employee("Bilbo", "Baggins", "Burglar", 7, "bilbobaggins@lotr.com"));
    this.repository.save(new Employee("Gandalf", "the Grey", "Wizard", 2000, "gandlafthegrey@lotr.com"));
    this.repository.save(new Employee("Samwise", "Gamgee", "Gardener", 0, "samwisegamgee@lotr.com"));
}
```

### Step 8: Updating UI Components
Update UI components to display the job years of each employee:
```javascript
// Updated EmployeeList component to display a table header for Email
class EmployeeList extends React.Component {
    render() {
        const employees = this.props.employees.map(employee =>
            <Employee key={employee._links.self.href} employee={employee}/>
        );
        return (
            <table>
                <tbody>
                    <tr>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Description</th>
                        <th>Job Years</th>
                        <th>Email</th>
                    </tr>
                    {employees}
                </tbody>
            </table>
        )
    }
}

// Updated Employee component to display email within the table data
class Employee extends React.Component {
    render() {
        return (
            <tr>
                <td>{this.props.employee.firstName}</td>
                <td>{this.props.employee.lastName}</td>
                <td>{this.props.employee.description}</td>
                <td>{this.props.employee.jobYears}</td>
                <td>{this.props.employee.email}</td>
            </tr>
        )
    }
}
```

### Step 9: Attribute Validation with Unit Testing
Update the Employee.java class:
```java
// Updated checkIfEmployeeAttributes() method to validate input attributes for email
public boolean checkIfEmployeeAtributes(String firstName, String lastName, String description, int jobYears, String email) {
    if (firstName == null || firstName.isBlank() || lastName == null || lastName.isBlank() || description == null || description.isBlank()) {
        return false;
    }
    if (email == null || email.isBlank()) {
        return false;
    }
    if (jobYears < 0) {
        return false;
    }
    return true;
}
```
Updated EmployeeTest.java
```java
//Updated existing tests with 
String email = "frodo@lotr.com";
// Invalid scenarios such as null or empty strings for email
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

```

### Step 10: Data Consistency
#### Debug both server and client parts of the solution:
Checked the insertion of data by Inspecting the page, there being able to check the elements
``` 
           <table>
                <tbody>
                    <tr>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Description</th>
                        <th>Job Years</th>
                        <th>Email</th>
                    </tr>
                    <tr>
                        <td>Frodo</td>
                        <td>Baggins</td>
                        <td>Ring Bearer</td>
                        <td>1</td>
                        <td>frodobaggins@lotr.com</td>
                    </tr>
```
### Step 11: Bug Fixing
The server should only accept Employees with a valid email (e.g., an email must have the ”@” sign). So a verification was created in order to ensure this.
Update the `Employee.java` class:
```java
// Added check for the email parameter if it contains an "@"
    public boolean checkIfEmployeeAtributes(String firstName, String lastName, String description, int jobYears, String email) {
    if (firstName == null || firstName.isBlank() || lastName == null || lastName.isBlank() || description == null || description.isBlank()) {
        return false;
    }
    if (!email.contains("@") || email == null || email.isBlank()) {
        return false;
    }
    if (jobYears < 0) {
        return false;
    }
    return true;
}
```

Update the `EmployeeTest.java` class:
```java
//Invalid scenario for email without a "@"
    @Test
    void tryToCreateEmployeeEmailNoAtSign() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 1;
        String email = "frodolotr.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }
```

## Project Analysis
### Instructions on how to run the program
Move to the basic folder 
```git bash
cd tut-react-and-spring-data-rest/basic
```
Try to execute the application from the command line
```git bash
./mvnw spring-boot:run
```
Enter the following url in your browser: 
http://localhost:8080/


### Version control system
In this project, Git was chosen as the version control system, but there are other distributed version control systems available, each with slightly different conceptual models.

Mercurial has immutable branching where each changeset belongs to a specific branch. Merging creates a new changeset combining changes from two branches.
Smaller ecosystem compared to Git but still offers tools, hosting platforms, and plugins for development workflows.
Less flexible compared to Git but suitable for projects requiring a simpler branching model and immutable history.

Subversion has a Centralized version control system (CVCS) where there's a single central repository, and developers typically interact with it to commit changes and update their working copies.
Branching and merging are less flexible compared to Git. Branches are typically created as copies of the trunk (main branch), and merging can be more cumbersome, especially for long-lived branches.
Operations like commit and update can be slower, especially with larger repositories, as they involve communication with the central server.
Doesn't allow offline work for developers requiring network access for most operations involving history and metadata, as these are stored centrally on the server.
Works well with simpler workflows due to its centralized nature, but may be less suitable for complex branching strategies.

### Command Differences
#### Here are some notable differences in commands between Git, Mercurial, and Subversion:
Checking Status:
- Git: git status
- Mercurial: hg status
- Subversion: svn status

Adding Changes to the Staging Area:
- Git: git add <files>
- Mercurial: hg add <files>
- Subversion: svn add <file>

Committing Changes:
- Git: git commit -m "Commit message"
- Mercurial: hg commit -m "Commit message"
- Subversion: svn commit -m "Commit message"

Pushing Changes to a Remote Repository:
- Git: git push
- Mercurial: hg push
- Subversion: No direct command (svn commit)

Pulling Changes from a Remote Repository:
- Git: git pull
- Mercurial: hg pull
- Subversion: svn update

Creating a Branch:
- Git: git branch <branch_name>
- Mercurial: hg branch <branch_name>
- Subversion: svn copy <source_url> <destination_url> -m "Branch message"

Switching Branches:
- Git: git checkout <branch_name>
- Mercurial: hg update <branch_name>
- Subversion: svn switch <branch_url>

Merging Branches:
- Git: git merge <branch_name>
- Mercurial: hg merge <branch_name>
- Subversion: svn merge <source_url>

Creating a Tag:
- Git: git tag <tag_name>
- Mercurial: hg tag <tag_name>
- Subversion: svn copy <source_url> <destination_url> -m "Tag message"

Viewing Differences:
- Git: git diff
- Mercurial: hg diff
- Subversion: svn diff

Git is often preferred due to its flexibility, extensive ecosystem, and support from third-party tools and services like GitHub, GitLab, and Bitbucket. However, the choice between version control systems ultimately depends on project requirements and team preferences.

### Difficulties found during the class assignment
One challenge I faced was with the usage of "git merge" from a branch that had progressed ahead of the main branch. This action inadvertently caused the separate branch to overwrite the main branch, leading to its disappearance from the network timeline.

To address this issue, I had to resort to using "git reset --hard" to revert back to the starting point of the first part of the class assignment. However, upon attempting to merge my changes again, I opted to use "git merge --no-ff" instead of the regular "git merge". Due to the history in my local repository, this resulted in two commits being duplicated.