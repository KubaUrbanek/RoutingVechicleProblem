package com.urbanek.routingproblem.ga.operations;

import com.urbanek.routingproblem.ga.config.Configs;
import com.urbanek.routingproblem.ga.randomkey.LocationRandomKey;
import com.urbanek.routingproblem.ga.randomkey.LocationRandomKeySeries;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@Component
public class BitFlipMutation {
    private final Random rand = new Random();

    public List<LocationRandomKeySeries> performMutation(List<LocationRandomKeySeries> locationRandomKeySeries) {
        return locationRandomKeySeries.stream()
                .map(this::mutateSeries)
                .toList();
    }

    private LocationRandomKeySeries mutateSeries(LocationRandomKeySeries series) {
        List<LocationRandomKey> randomKeys = series.locationRandomKeys();
        return mutateRoute(randomKeys) ? LocationRandomKeySeries.builder()
                .locationRandomKeys(randomKeys)
                .fitnessScore(null)
                .build() : series;
    }

    private boolean mutateRoute(List<LocationRandomKey> randomKeys) {
        boolean hasMutated = false;
        for (int index = 0; index < randomKeys.size(); index++) {
            if (shouldMutate()) {
                hasMutated = true;
                mutate(randomKeys, index);
            }
        }
        return hasMutated;
    }

    private void mutate(List<LocationRandomKey> series, int index) {
        LocationRandomKey locationRandomKey = series.get(index);
        locationRandomKey.setRandomKey(rand.nextDouble());
    }

    private boolean shouldMutate() {
        return Configs.MUTATION_RATE > rand.nextDouble();
    }
}
