package com.urbanek.routingproblem.utils.writers;

import com.urbanek.routingproblem.ga.randomkey.LocationRandomKeySeries;
import com.urbanek.routingproblem.ga.statistics.GenerationStatistics;
import com.urbanek.routingproblem.ga.statistics.StatisticsAggregator;
import com.urbanek.routingproblem.geo.locations.dtos.Location;
import com.urbanek.routingproblem.geo.locations.services.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class ConsoleResultPrinter {
    private final LocationService locationService;

    public void printPopulation(StatisticsAggregator statisticsAggregator) {

        StringJoiner joiner = new StringJoiner("\n");
        statisticsAggregator.getStats()
                .forEach(stat -> {
                    joiner.add("Generation " + stat.getGenerationNumber());

                    joiner.add("Best result: ");
                    addSingleSeriesStat(joiner, stat.getBestResult());
                    joiner.add("");

                    joiner.add("Worst result: ");
                    addSingleSeriesStat(joiner, stat.getWorstResult());
                    joiner.add("");

                    joiner.add("Total fitness: " + stat.getTotalFitness());
                    joiner.add("Average fitness: " + stat.getAverageFitness());
                    joiner.add("");

                });
        joiner.add("Summary");
        GenerationStatistics seriesWithBestResult = statisticsAggregator.getSeriesWithBestResult();
        joiner.add("Best series ever ");
        joiner.add("Generation " + seriesWithBestResult.getGenerationNumber());
        addSingleSeriesStat(joiner, seriesWithBestResult.getBestResult());
        joiner.add("");
        GenerationStatistics seriesWithWorstResult = statisticsAggregator.getSeriesWithWorstResult();
        joiner.add("Worst series ever: ");
        joiner.add("Generation " + seriesWithWorstResult.getGenerationNumber());
        addSingleSeriesStat(joiner, seriesWithWorstResult.getWorstResult());

        log.info(joiner.toString());
    }

    private void addSingleSeriesStat(StringJoiner joiner, LocationRandomKeySeries series) {
        joiner.add("Fitness "  + series.fitnessScore());
        Map<String, List<Location>> orderedLocationGroupByEmployee = locationService.getOrderedLocationGroupByEmployee(series.locationRandomKeys());
        orderedLocationGroupByEmployee.forEach((key, value) ->
                joiner.add(key + " Route: " + value.stream()
                        .map(location -> String.valueOf(location.id()))
                        .collect(Collectors.joining(" -> "))));
    }
}
