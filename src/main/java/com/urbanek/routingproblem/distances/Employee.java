package com.urbanek.routingproblem.distances;

import lombok.Getter;

import java.util.List;

@Getter
public class Employee {
    private final String id;
    private final int workingHour;
    private final int capacity;
    private final int currentWorkload;

    public Employee(Employee employee) {
        this.capacity = employee.getCapacity();
        this.id = employee.getId();
        this.workingHour = employee.getWorkingHour();
        this.currentWorkload = employee.getCurrentWorkload();
    }

    public Employee(String id, int capacity) {
        this.capacity = capacity;
        this.workingHour = 0;
        this.currentWorkload = 0;
        this.id = id;
    }

    public boolean isFullyLoaded(List<Location> localLocations) {
        return localLocations.stream()
                .allMatch(location -> location.requiredTime() + currentWorkload >= capacity);
    }
}
