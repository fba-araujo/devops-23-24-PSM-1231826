'use strict';

// tag::vars[]
// Import necessary modules
const React = require('react'); // <1>
const ReactDOM = require('react-dom'); // <2>
const client = require('./client'); // <3>
// end::vars[]

// tag::app[]
// Define the main App component
class App extends React.Component { // <1>

    // Constructor to initialize state
    constructor(props) {
        super(props);
        this.state = {employees: []};
    }

    // Lifecycle method to fetch data when component mounts
    componentDidMount() { // <2>
        client({method: 'GET', path: '/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/api/employees'}).done(response => {
            this.setState({employees: response.entity._embedded.employees});
        });
    }

    // Render method to display the EmployeeList component
    render() { // <3>
        return (
            <EmployeeList employees={this.state.employees}/>
        )
    }
}
// end::app[]

// tag::employee-list[]
// Define the EmployeeList component
class EmployeeList extends React.Component {
    // Render method to display a table of employees
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
// end::employee-list[]

// tag::employee[]
// Define the Employee component
class Employee extends React.Component {
    render() {
        // Render method to display details of a single employee
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
// end::employee[]

// tag::render[]
// Render the main App component to the DOM
ReactDOM.render(
    <App />,
    document.getElementById('react')
)
// end::render[]