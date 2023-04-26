package com.urbanek.routingproblem.geo.locations.services;

import com.urbanek.routingproblem.employes.services.EmployeeService;
import com.urbanek.routingproblem.ga.config.Configs;
import com.urbanek.routingproblem.ga.randomkey.LocationRandomKey;
import com.urbanek.routingproblem.geo.locations.dtos.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final EmployeeService employeeService;

    @Override
    public List<Location> getAllLocations() {
        return Stream.concat(getAllCustomerLocations().stream(), Stream.of(Configs.DEPOT))
                .toList();
    }

    @Override
    public List<Location> getAllCustomerLocations() {
        return Configs.LOCATIONS;
    }
    @Override
    public Map<String, List<Location>> getOrderedLocationGroupByEmployee(List<LocationRandomKey> locationRandomKeys) {
        List<LocationRandomKey> locationRandomKeysCopy = new ArrayList<>(locationRandomKeys);
        Map<String, List<Location>> orderedLocationIndexesGroupByEmployee = groupLocationByEmployees(locationRandomKeysCopy);
        fillWithMissingEmployees(orderedLocationIndexesGroupByEmployee);
        addDepotToAllEmployees(orderedLocationIndexesGroupByEmployee);

        return orderedLocationIndexesGroupByEmployee;
    }

    private void addDepotToAllEmployees(Map<String, List<Location>> orderedLocationIndexesGroupByEmployee) {
        orderedLocationIndexesGroupByEmployee.values()
                .forEach(locations -> locations.add(0, Configs.DEPOT));
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
                .filter(locationRandomKey -> Objects.equals(locationRandomKey.getLocationId(), locationId))
                .findFirst()
                .orElseThrow()
                .getEmployeeId();
    }

    private void fillWithMissingEmployees(Map<String, List<Location>> orderedLocationIndexesGroupByEmployee) {
        employeeService.getAllEmployees().stream()
                .filter(employee -> !orderedLocationIndexesGroupByEmployee.containsKey(employee.getId()))
                .forEach(employee -> orderedLocationIndexesGroupByEmployee.put(employee.getId(), new ArrayList<>()));
    }

    private List<Integer> getLocationIdsInOrder(List<LocationRandomKey> locationRandomKeys) {
        return locationRandomKeys.stream()
                .sorted(Comparator.comparingDouble(LocationRandomKey::getRandomKey))
                .map(LocationRandomKey::getLocationId)
                .toList();
    }

    private Location getLocationById(int id) {
        return getAllLocations()
                .stream()
                .filter(location -> Objects.equals(location.id(), id))
                .findFirst()
                .orElseThrow();
    }
}
