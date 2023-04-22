package com.urbanek.routingproblem.distances;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Employee {
    private final String id;
    private final int workingHour;
    private final int capacity;
    private int currentWorkload;
    private final List<Location> locations;

    public Employee(Employee employee) {
        this.capacity = employee.getCapacity();
        this.id = employee.getId();
        this.workingHour = employee.getWorkingHour();
        this.locations = new ArrayList<>(employee.getLocations());
        this.currentWorkload = employee.getCurrentWorkload();
    }

    public Employee(String id, int capacity) {
        this.capacity = capacity;
        this.workingHour = 0;
        this.currentWorkload = 0;
        this.id = id;
        locations = new ArrayList<>();
    }

    public boolean assignLocation(Location location) {
        if (capacity >= currentWorkload + location.requiredTime()) {
            currentWorkload += location.requiredTime();
            return locations.add(location);
        } else {
            return false;
        }
    }

    public boolean isFullyLoaded(List<Location> localLocations) {
        return localLocations.stream()
                .allMatch(location -> location.requiredTime() + currentWorkload >= capacity);
    }

    public String getRoute() {
        return locations.stream()
                .filter(location -> location != Location.EMPTY_LOCATION)
                .map(location -> String.valueOf(location.id()))
                .reduce(IdGenerator::generateDistanceId)
                .orElse("");
    }

    @Override
    public String toString() {
        return "Employee " + id + " current workload " + currentWorkload + " capacity " + capacity + "\n" +
                getRoute();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Employee employee = (Employee) o;

        return new org.apache.commons.lang3.builder.EqualsBuilder().append(id, employee.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37).append(id).toHashCode();
    }
}
