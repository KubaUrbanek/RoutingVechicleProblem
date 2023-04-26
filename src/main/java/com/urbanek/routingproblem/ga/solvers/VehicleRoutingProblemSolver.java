package com.urbanek.routingproblem.ga.solvers;

import com.urbanek.routingproblem.employes.dtos.Employee;
import com.urbanek.routingproblem.ga.config.Configs;
import com.urbanek.routingproblem.ga.fitness.FitnessCalculator;
import com.urbanek.routingproblem.ga.operations.*;
import com.urbanek.routingproblem.ga.randomkey.LocationRandomKeySeries;
import com.urbanek.routingproblem.ga.statistics.StatisticsAggregator;
import com.urbanek.routingproblem.ga.writers.ConsoleResultPrinter;
import com.urbanek.routingproblem.geo.distances.dtos.DistanceIdentifier;
import com.urbanek.routingproblem.geo.distances.services.DistanceCalculator;
import com.urbanek.routingproblem.geo.locations.dtos.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class VehicleRoutingProblemSolver {
    private final DistanceCalculator distanceCalculator;
    private final PopulationGenerator populationGenerator;
    private final ConsoleResultPrinter resultPrinter;
    private final TournamentSelection tournamentSelection;
    private final EliteSelection eliteSelection;
    private final SinglePointCrossover singlePointCrossover;
    private final FitnessCalculator fitnessCalculator;
    private final StatisticsAggregator statisticsAggregator;
    private final BitFlipMutation bitFlipMutation;

    public void start() {
        assertInitialRules();
        Map<DistanceIdentifier, Double> distances = distanceCalculator.calculateDistances();
        List<LocationRandomKeySeries> beginningPopulation = populationGenerator.generateUsingRandomKeys(distances);
        AtomicReference<List<LocationRandomKeySeries>> currentPopulation = new AtomicReference<>(beginningPopulation);

        IntStream.rangeClosed(1, Configs.GENERATION_AMOUNT).forEach(generationNumber -> {
            statisticsAggregator.addGenerationStat(generationNumber, currentPopulation.get());
            currentPopulation.set(performGaOperations(new ArrayList<>(currentPopulation.get())).stream()
                    .map(series -> Objects.isNull(series.fitnessScore()) ? recreateSeriesWithFitness(series, distances) : series)
                    .toList());

        });

        resultPrinter.printPopulation(statisticsAggregator);
        statisticsAggregator.reset();

    }

    private List<LocationRandomKeySeries> performGaOperations(List<LocationRandomKeySeries> initialPopulation) {
        List<LocationRandomKeySeries> elitePopulation = eliteSelection.getElite(initialPopulation);
        initialPopulation.removeAll(elitePopulation);

        return Stream.concat(Stream.of(tournamentSelection.performSelection(initialPopulation))
                                .map(singlePointCrossover::performCrossover)
                                .map(bitFlipMutation::performMutation)
                                .flatMap(Collection::stream),
                        elitePopulation.stream())
                .toList();
    }

    private LocationRandomKeySeries recreateSeriesWithFitness(LocationRandomKeySeries person, Map<DistanceIdentifier, Double> distances) {
        return LocationRandomKeySeries.builder()
                .locationRandomKeys(person.locationRandomKeys())
                .fitnessScore(fitnessCalculator.calculateFitness(distances, person.locationRandomKeys()))
                .build();
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
