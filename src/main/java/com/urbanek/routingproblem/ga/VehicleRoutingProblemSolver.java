package com.urbanek.routingproblem.ga;

import com.urbanek.routingproblem.distances.DistanceCalculator;
import com.urbanek.routingproblem.distances.Employee;
import com.urbanek.routingproblem.distances.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Component
@RequiredArgsConstructor
public class VehicleRoutingProblemSolver {
    private final DistanceCalculator distanceCalculator;
    private final PopulationGenerator populationGenerator;

    public void start() {
        assertInitialRules();
        Map<String, Double> distances = distanceCalculator.calculateDistances(Configs.LOCATIONS);
        List<List<Employee>> population = populationGenerator.generatePopulation(Configs.POPULATION_AMOUNT);

        population.forEach(employees -> {
            StringJoiner joiner = new StringJoiner("\n", "Generation \n", "\n");
            employees.forEach(employee -> joiner.add(employee.toString()));
            System.out.println(joiner);
        });

    }

    private static void assertInitialRules() {
        Double totalRequiredWork = Configs.LOCATIONS.stream()
                .map(Location::requiredTime)
                .reduce(Double::sum)
                .orElse((double) 0);
        Integer totalEmployeeCapacity = Configs.EMPLOYEES.stream()
                .map(Employee::getCapacity)
                .reduce(Integer::sum)
                .orElse(0);

        if (totalEmployeeCapacity < totalRequiredWork) {
            throw new IllegalArgumentException("Amount of work cannot be greater than total capacity of employees");
        }
    }

}
