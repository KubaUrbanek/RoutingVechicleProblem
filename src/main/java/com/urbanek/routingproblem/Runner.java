package com.urbanek.routingproblem;

import com.urbanek.routingproblem.ga.VehicleRoutingProblemSolver;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Runner implements CommandLineRunner {
    private final VehicleRoutingProblemSolver solver;
    @Override
    public void run(String... args) throws Exception {
        solver.start();
    }
}
