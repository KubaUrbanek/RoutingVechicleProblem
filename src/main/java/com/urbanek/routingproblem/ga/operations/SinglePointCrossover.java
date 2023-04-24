package com.urbanek.routingproblem.ga.operations;

import com.urbanek.routingproblem.ga.config.Configs;
import com.urbanek.routingproblem.ga.randomkey.LocationRandomKey;
import com.urbanek.routingproblem.ga.randomkey.LocationRandomKeySeries;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SinglePointCrossover {
    public static final int PARENTS_AMOUNT = 2;
    private final Random rand = new Random();

    public List<LocationRandomKeySeries> performCrossover(List<LocationRandomKeySeries> locationRandomKeySeries) {
        AtomicInteger seriesIndex = new AtomicInteger(-1);

        return locationRandomKeySeries.stream()
                .filter(series -> locationRandomKeySeries.size() > seriesIndex.incrementAndGet())
                .flatMap(series -> shouldPerformCrossover(seriesIndex.intValue(), locationRandomKeySeries) ?
                        doCrossover(seriesIndex, locationRandomKeySeries).stream() :
                        Stream.of(locationRandomKeySeries.get(seriesIndex.intValue())))
                .collect(Collectors.toList());
    }

    private List<LocationRandomKeySeries> doCrossover(AtomicInteger seriesIndex, List<LocationRandomKeySeries> locationRandomKeySeries) {
        PeoplePair parents = getParents(seriesIndex.intValue(), locationRandomKeySeries);
        PeoplePair children = parents.reproduce();
        seriesIndex.incrementAndGet();
        return List.of(children.firstPerson, children.secondPerson);
    }

    private PeoplePair getParents(int seriesIndex, List<LocationRandomKeySeries> locationRandomKeySeries) {
        List<LocationRandomKeySeries> parents = locationRandomKeySeries.subList(seriesIndex, seriesIndex + PARENTS_AMOUNT);
        return new PeoplePair(parents.get(0), parents.get(1));
    }

    private boolean shouldPerformCrossover(int seriesIndex, List<LocationRandomKeySeries> locationRandomKeySeries) {
        return Configs.CROSSOVER_RATE > rand.nextDouble() && !isLastElement(locationRandomKeySeries, seriesIndex);
    }

    private static boolean isLastElement(List<LocationRandomKeySeries> locationRandomKeySeries, int i) {
        return i + 1 == locationRandomKeySeries.size();
    }

    @Value
    @AllArgsConstructor
    private class PeoplePair {
        LocationRandomKeySeries firstPerson;
        LocationRandomKeySeries secondPerson;

        private PeoplePair reproduce() {
            List<LocationRandomKey> firstParentGenes = firstPerson.locationRandomKeys();
            List<LocationRandomKey> secondParentGenes = secondPerson.locationRandomKeys();

            assert firstParentGenes.size() == secondParentGenes.size() : "Number of genes in people need to be the same in order to reproduce";

            int crossoverIndex = rand.nextInt(firstParentGenes.size());
            LocationRandomKeySeries firstChild = getChild(firstParentGenes, secondParentGenes, crossoverIndex);
            LocationRandomKeySeries secondChild = getChild(secondParentGenes, firstParentGenes, crossoverIndex);

            return new PeoplePair(firstChild, secondChild);
        }

        private LocationRandomKeySeries getChild(List<LocationRandomKey> firstGenes, List<LocationRandomKey> secondGenes, int crossoverIndex) {
            return LocationRandomKeySeries.builder()
                    .locationRandomKeys(getNewGenes(firstGenes, secondGenes, crossoverIndex))
                    .fitnessScore(null)
                    .build();
        }

        private List<LocationRandomKey> getNewGenes(List<LocationRandomKey> firstGenes, List<LocationRandomKey> secondGenes, int crossoverIndex) {
            return Stream.concat(firstGenes.subList(0, crossoverIndex).stream(),
                    secondGenes.subList(crossoverIndex, secondGenes.size()).stream()).collect(Collectors.toList());
        }
    }
}
