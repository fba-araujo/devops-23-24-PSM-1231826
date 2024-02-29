# Class Assigment 1
## Tutorial React.js and Spring Data REST Application
### Add a new field to record the years of the employee in the company
#### Updated Employee.java 
- Added attribute private int jobYears
- Changed the empty Employee constructor to public
- Changed the Employee constructor with parameters to receive int jobYears and added the line this.jobYears = jobYears;
- Changed method public boolean equals(Object o) adding the line Objects.equals(jobYears, employee.jobYears);
- Changed method public int hashCode() adding jobYears to return Objects.hash()
- Changed toString() adding the line ", jobYears='" + jobYears + '\'' +
- Added public int getJobYears()
- Added public void setJobYears(int jobYears)

#### Updated DatabaseLoader.java
- Changed public void run(String... strings) adding jobYears to existing Employee

#### Updated app.js
- Changed method EmployeeList extends React.Component adding the line <th>Job Years</th>
- Changed method class Employee extends React.Component adding the line td>{this.props.employee.jobYears}</td>

### Validation of the attributes of the creation of Employees, with unit testing
#### Updated Employee.java
- Added method public boolean checkIfEmployeeAtributes(String firstName, String lastName, String description, int jobYears)
- Changed the Employee constructor with parameters to throw InstantiationException if the checkIfEmployeeAtribute returns false

#### Created EmployeeTest.java
- Created test with a valid Employee created
- Created tests to check if the InstantiationException is thrown when trying to create a Employee with:
  - firstName null, empty and with whitespaces
  - lastName null, empty and with whitespaces
  - description null, empty and with whitespaces
  - jobYears negative years

#### Updated DatabaseLoader.java
- Added various Employees

### Debug the server and client parts of the solution
- If a invalid Employee is created in the DatabaseLoader the program is unable to be built
- With the server running if a curl is used an Employee with invalid attributes can be added to the table:
  - curl -X POST localhost:8080/api/employees -d "{\"firstName\": \"Peregrin\", \"lastName\": \"Took\", \"description\": \"Pipe Smoker\", \"jobYears\": \"-3\"}" -H "Content-Type:application/json"