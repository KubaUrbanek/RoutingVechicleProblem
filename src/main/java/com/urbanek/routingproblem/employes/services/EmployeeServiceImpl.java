package com.urbanek.routingproblem.employes.services;

import com.urbanek.routingproblem.employes.dtos.Employee;
import com.urbanek.routingproblem.ga.config.Configs;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final Random rand = new Random();

    public Employee getRandomEmployee() {
        return new Employee(Configs.EMPLOYEES.get(rand.nextInt(Configs.EMPLOYEES.size())));
    }

    public Employee getEmployeeById(String id) {
        return new Employee(Configs.EMPLOYEES.stream()
                .filter(employee -> Objects.equals(employee.getId(), id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format("Employee with ID {0} does not exists", id))));
    }

    public List<Employee> getAllEmployees(){
        return Configs.EMPLOYEES;
    }
}
