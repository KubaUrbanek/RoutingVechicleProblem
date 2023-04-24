package com.urbanek.routingproblem.ga.operations;

import com.urbanek.routingproblem.ga.config.Configs;
import com.urbanek.routingproblem.ga.randomkey.LocationRandomKeySeries;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EliteSelection {
    public List<LocationRandomKeySeries> getElite(List<LocationRandomKeySeries> locationRandomKeySeriesList) {
        return locationRandomKeySeriesList.stream()
                .sorted(Comparator.comparingDouble(LocationRandomKeySeries::fitnessScore))
                .limit(Configs.ELITE_AMOUNT)
                .collect(Collectors.toList());
    }
}
