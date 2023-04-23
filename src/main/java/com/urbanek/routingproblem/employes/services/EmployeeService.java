package com.urbanek.routingproblem.employes.services;

import com.urbanek.routingproblem.employes.dtos.Employee;

import java.util.List;

public interface EmployeeService {
    Employee getRandomEmployee();

    Employee getEmployeeById(String id);

    List<Employee> getAllEmployees();
}
