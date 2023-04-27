package com.urbanek.routingproblem.ga.config;

import com.urbanek.routingproblem.employes.dtos.Employee;
import com.urbanek.routingproblem.geo.locations.dtos.Coordinates;
import com.urbanek.routingproblem.geo.locations.dtos.Location;

import java.util.List;

public class Configs {
    public static final List<Employee> EMPLOYEES = List.of(new Employee("Milosz", 1800),
            new Employee("Lezanka", 20),
            new Employee("Iza", 200),
            new Employee("Markiza", 200),
            new Employee("Agata z Fiata", 200),
            new Employee("Maciek", 200),
            new Employee("Klon Milosza", 200),
            new Employee("Wylonacznik", 350));

    public static final Location DEPOT = new Location(0, new Coordinates(50, 50), 0);
    public static final int ELITE_AMOUNT = 1;
    public static final int POPULATION_AMOUNT = 2000;
    public static final int GENERATION_AMOUNT = 500;
    public static final double CROSSOVER_RATE = 0.8;
    public static final double MUTATION_RATE = 0.02;
    public static final int TOURNAMENT_SIZE = 4;
    public static final int LOCATION_AMOUNT = 50;
}
