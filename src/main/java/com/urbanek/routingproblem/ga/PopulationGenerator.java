package com.urbanek.routingproblem.ga;

import com.urbanek.routingproblem.distances.Employee;
import com.urbanek.routingproblem.distances.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Slf4j
public class PopulationGenerator {
    private final Random rand = new Random();

    public List<List<Employee>> generatePopulation(int populationAmount) {
        List<Location> locations = List.copyOf(Configs.LOCATIONS);

        return IntStream.rangeClosed(1, populationAmount)
                .mapToObj(i -> {
                    List<Location> localLocations = new ArrayList<>(List.copyOf(locations));
                    List<Employee> result = createEmployeePopulation(localLocations);
                    while (!localLocations.isEmpty()) {
                        log.warn("Population needs to be recreated. Not all jobs were assigned");
                        localLocations = new ArrayList<>(List.copyOf(locations));
                        createEmployeePopulation(localLocations);
                    }
                    return result;
                })
                .collect(Collectors.toList());

    }

    private List<Employee> createEmployeePopulation(List<Location> localLocations) {
        List<Employee> employees = new ArrayList<>(List.copyOf(Configs.EMPLOYEES));
        List<Employee> result = new ArrayList<>();
        while (!employees.isEmpty()) {
            Employee employee = getRandomEmployee(employees);
            assignLocationsToEmployee(localLocations, employee);
            employees.remove(employee);
            result.add(employee);
        }
        return result;
    }

    private Employee getRandomEmployee(List<Employee> employees) {
        return new Employee(employees.get(rand.nextInt(employees.size())));
    }

    private void assignLocationsToEmployee(List<Location> localLocations, Employee employee) {
        while (!employee.isFullyLoaded(localLocations)) {
            Location randomLocation = getRandomLocation(localLocations);
            while (!employee.assignLocation(randomLocation)) {
                randomLocation = getRandomLocation(localLocations);
            }
            localLocations.remove(randomLocation);
        }
    }

    private Location getRandomLocation(List<Location> localLocations) {
        return !localLocations.isEmpty() ? localLocations.get(rand.nextInt(localLocations.size())) : Location.EMPTY_LOCATION;
    }
}
