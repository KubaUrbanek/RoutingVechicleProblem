package com.urbanek.routingproblem.ga;

import com.urbanek.routingproblem.distances.LocationRandomKey;
import com.urbanek.routingproblem.distances.LocationRandomKeySeries;
import com.urbanek.routingproblem.employes.EmployeeService;
import com.urbanek.routingproblem.locations.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Slf4j
@RequiredArgsConstructor
public class PopulationGenerator {
    private final EmployeeService employeeService;
    private final LocationService locationService;

    public List<LocationRandomKeySeries> generateUsingRandomKeys() {
        return IntStream.rangeClosed(1, Configs.POPULATION_AMOUNT)
                .mapToObj(i -> {
                    List<LocationRandomKey> locationRandomKeys = IntStream.rangeClosed(1, locationService.getAllLocations().size())
                            .mapToObj(index -> new LocationRandomKey(employeeService.getRandomEmployee().getId(), Math.random()))
                            .collect(Collectors.toList());
                    return new LocationRandomKeySeries(locationRandomKeys, locationService.getOrderedLocationGroupByEmployee(locationRandomKeys));
                })
                .collect(Collectors.toList());
    }


}
