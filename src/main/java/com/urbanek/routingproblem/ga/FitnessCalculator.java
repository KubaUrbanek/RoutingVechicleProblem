package com.urbanek.routingproblem.ga;

import java.text.MessageFormat;
import java.util.List;

public class FitnessCalculator {
    public Double calculateFitness(List<Double> distances) {
        return distances.stream()
                .reduce(Double::sum)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format("Unknown distances passed to fitness calculator {0}", distances)));
    }
}
