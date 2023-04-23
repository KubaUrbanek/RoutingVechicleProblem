package com.urbanek.routingproblem.ga.solvers;

import com.urbanek.routingproblem.employes.dtos.Employee;
import com.urbanek.routingproblem.ga.config.Configs;
import com.urbanek.routingproblem.ga.fitness.FitnessCalculator;
import com.urbanek.routingproblem.ga.operations.PopulationGenerator;
import com.urbanek.routingproblem.ga.randomkey.LocationRandomKeySeries;
import com.urbanek.routingproblem.ga.writers.ResultPrinter;
import com.urbanek.routingproblem.geo.distances.dtos.DistanceIdentifier;
import com.urbanek.routingproblem.geo.locations.dtos.Location;
import com.urbanek.routingproblem.geo.distances.services.DistanceCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VehicleRoutingProblemSolver {
    private final DistanceCalculator distanceCalculator;
    private final PopulationGenerator populationGenerator;
    private final FitnessCalculator fitnessCalculator;
    private final ResultPrinter resultPrinter;

    public void start() {
        assertInitialRules();
        Map<DistanceIdentifier, Double> distances = distanceCalculator.calculateDistances();
        List<LocationRandomKeySeries> locationRandomKeySeriesList = populationGenerator.generateUsingRandomKeys();
        Map<String, Double> seriesToFitnessMap = locationRandomKeySeriesList.stream()
                .collect(Collectors.toUnmodifiableMap(LocationRandomKeySeries::getSeriesIdentifier,
                        locationRandomKeySeries -> fitnessCalculator.calculateFitness(locationRandomKeySeries, distances)));
        locationRandomKeySeriesList.stream()
                .sorted(Comparator.comparingDouble(locationRandomKeySeries -> seriesToFitnessMap.get(locationRandomKeySeries.getSeriesIdentifier())))
                .skip(Configs.ELITE_AMOUNT);
        resultPrinter.printPopulation(locationRandomKeySeriesList, seriesToFitnessMap);

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