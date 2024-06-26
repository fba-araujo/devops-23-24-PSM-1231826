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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Initializes the database with sample employee data.
 * Implements CommandLineRunner interface to run code on application startup.
 * Loads sample employee data into the database using the EmployeeRepository.
 *
 * @author Greg Turnquist
 */
// tag::code[]
@Component // <1>
public class DatabaseLoader implements CommandLineRunner { // <2>

    private final EmployeeRepository repository;

    /**
     * Constructs a new DatabaseLoader with the specified EmployeeRepository.
     *
     * @param repository The EmployeeRepository to use for database operations.
     */
    @Autowired // <3>
    public DatabaseLoader(EmployeeRepository repository) {
        this.repository = repository;
    }

    /**
     * Runs the DatabaseLoader to initialize the database with sample employee data.
     *
     * @param strings Command-line arguments (not used).
     * @throws Exception If an error occurs while initializing the database.
     */
    @Override
    public void run(String... strings) throws Exception { // <4>
        this.repository.save(new Employee("Frodo", "Baggins", "Ring Bearer", 1, "frodobaggins@lotr.com"));
        this.repository.save(new Employee("Bilbo", "Baggins", "Burglar", 7, "bilbobaggins@lotr.com"));
        this.repository.save(new Employee("Gandalf", "the Grey", "Wizard", 2000, "gandlafthegrey@lotr.com"));
        this.repository.save(new Employee("Samwise", "Gamgee", "Gardener", 0, "samwisegamgee@lotr.com"));
    }
}
// end::code[]
