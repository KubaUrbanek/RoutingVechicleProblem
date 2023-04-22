package com.urbanek.routingproblem.distances;

import java.util.StringJoiner;

public class IdGenerator {
    public static String generateDistanceId(Location first, Location second) {
        return generateDistanceId(String.valueOf(first.id()), String.valueOf(second.id()));
    }

    public static String generateDistanceId(String first, String second) {
        return new StringJoiner(" -> ")
                .add(first)
                .add(second)
                .toString();
    }
}
