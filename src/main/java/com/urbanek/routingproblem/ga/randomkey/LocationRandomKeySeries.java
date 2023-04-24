package com.urbanek.routingproblem.ga.randomkey;

import lombok.Builder;

import java.util.List;

@Builder
public record LocationRandomKeySeries(List<LocationRandomKey> locationRandomKeys, Double fitnessScore) {
}
