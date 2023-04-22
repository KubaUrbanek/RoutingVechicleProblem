package com.urbanek.routingproblem.distances;


public record Location(int id, Coordinates coords, double requiredTime
//        , TimeWindow timeWindow, double requiredTime) {
) {
    public static final Location EMPTY_LOCATION = new Location(-1, null, 0);

    public double y() {
        return coords.y();
    }

    public double x() {
        return coords.x();
    }
}
