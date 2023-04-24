package com.urbanek.routingproblem.ga.config;

import com.urbanek.routingproblem.geo.locations.dtos.Coordinates;
import com.urbanek.routingproblem.geo.locations.dtos.Location;
import com.urbanek.routingproblem.employes.dtos.Employee;

import java.util.List;

public class Configs {
    public static final List<Location> LOCATIONS = List.of(new Location(1, new Coordinates(456, 320), 10),
            new Location(2, new Coordinates(228, 0), 20),
            new Location(3, new Coordinates(912, 0), 1),
            new Location(4, new Coordinates(0, 80), 57),
            new Location(5, new Coordinates(114, 80), 350),
            new Location(6, new Coordinates(570, 160), 15),
            new Location(7, new Coordinates(798, 160), 17),
            new Location(8, new Coordinates(342, 240), 150),
            new Location(9, new Coordinates(684, 240), 99),
            new Location(10, new Coordinates(570, 400), 1),
            new Location(11, new Coordinates(912, 400), 200),
            new Location(12, new Coordinates(114, 480), 525),
            new Location(13, new Coordinates(228, 480), 88),
            new Location(14, new Coordinates(342, 560), 249),
            new Location(15, new Coordinates(684, 560), 455),
            new Location(16, new Coordinates(0, 640), 9),
            new Location(17, new Coordinates(798, 640), 100));
    public static final List<Employee> EMPLOYEES = List.of(new Employee("Milosz", 1800),
            new Employee("Lezanka", 20),
            new Employee("Random", 200),
            new Employee("Wylonacznik", 350));

    public static final int DEPOT = 0;
    public static final int ELITE_AMOUNT = 1;
    public static final int POPULATION_AMOUNT = 100;

    public static final int GENERATION_AMOUNT = 100;
    public static final double CROSSOVER_RATE = 0.7;
    public static final double MUTATION_RATE = 0.01;
    public static final int TOURNAMENT_SIZE = 4;

}
