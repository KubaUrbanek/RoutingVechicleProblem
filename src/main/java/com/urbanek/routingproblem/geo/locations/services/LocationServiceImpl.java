package com.urbanek.routingproblem.geo.locations.services;

import com.urbanek.routingproblem.employes.services.EmployeeService;
import com.urbanek.routingproblem.ga.randomkey.LocationRandomKey;
import com.urbanek.routingproblem.ga.config.Configs;
import com.urbanek.routingproblem.geo.locations.dtos.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final EmployeeService employeeService;

    public List<Location> getAllLocations() {
        return Configs.LOCATIONS;
    }

    public Map<String, List<Location>> getOrderedLocationGroupByEmployee(List<LocationRandomKey> locationRandomKeys) {

        Map<String, List<Location>> orderedLocationIndexesGroupByEmployee = groupLocationByEmployees(locationRandomKeys);
        fillWithMissingEmployees(orderedLocationIndexesGroupByEmployee);

        return orderedLocationIndexesGroupByEmployee;
    }

    private Map<String, List<Location>> groupLocationByEmployees(List<LocationRandomKey> locationRandomKeys) {
        List<Integer> locationIdsInOrder = getLocationIdsInOrder(locationRandomKeys);

        return new HashMap<>(locationIdsInOrder.stream()
                .collect(Collectors.toUnmodifiableMap(locationId -> getEmployeeIdByLocation(locationRandomKeys, locationId),
                        (locationId) -> new ArrayList<>(Collections.singletonList(getLocationById(locationId))),
                        (existingList, newList) -> {
                            existingList.addAll(newList);
                            return existingList;
                        })));
    }

    private static String getEmployeeIdByLocation(List<LocationRandomKey> locationRandomKeys, Integer locationId) {
        return locationRandomKeys.stream()
                .filter(locationRandomKey -> Objects.equals(locationRandomKey.locationId(), locationId))
                .findFirst()
                .orElseThrow()
                .employeeId();
    }

    private void fillWithMissingEmployees(Map<String, List<Location>> orderedLocationIndexesGroupByEmployee) {
        employeeService.getAllEmployees().stream()
                .filter(employee -> !orderedLocationIndexesGroupByEmployee.containsKey(employee.getId()))
                .forEach(employee -> orderedLocationIndexesGroupByEmployee.put(employee.getId(), Collections.emptyList()));
    }

    private List<Integer> getLocationIdsInOrder(List<LocationRandomKey> locationRandomKeys) {
        return locationRandomKeys.stream()
                .sorted(Comparator.comparingDouble(LocationRandomKey::randomKey))
                .map(LocationRandomKey::locationId)
                .toList();
    }

    private Location getLocationById(int id) {
        return Configs.LOCATIONS.stream()
                .filter(location -> Objects.equals(location.id(), id))
                .findFirst()
                .orElseThrow();
    }
}
