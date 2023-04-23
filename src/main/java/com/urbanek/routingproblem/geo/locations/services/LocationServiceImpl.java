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
public class LocationServiceImpl implements LocationService{
    private final EmployeeService employeeService;

    public List<Location> getAllLocations() {
        return Configs.LOCATIONS;
    }

    public Map<String, List<Location>> getOrderedLocationGroupByEmployee(List<LocationRandomKey> locationRandomKeys) {
        List<Integer> sortedIndexes = getLocationOrder(locationRandomKeys);

        Map<String, List<Location>> orderedLocationIndexesGroupByEmployee = groupLocationByEmployees(locationRandomKeys, sortedIndexes);
        fillWithMissingEmployees(orderedLocationIndexesGroupByEmployee);

        return orderedLocationIndexesGroupByEmployee;
    }

    private Map<String, List<Location>> groupLocationByEmployees(List<LocationRandomKey> locationRandomKeys, List<Integer> indexes) {
        return new HashMap<>(indexes.stream()
                .collect(Collectors.toUnmodifiableMap(index -> locationRandomKeys.get(index).employeeId(),
                        (index) -> new ArrayList<>(Collections.singletonList(getLocationByIndex(index))),
                        (existingList, newList) -> {
                            existingList.addAll(newList);
                            return existingList;
                        })));
    }

    private void fillWithMissingEmployees(Map<String, List<Location>> orderedLocationIndexesGroupByEmployee) {
        employeeService.getAllEmployees().stream()
                .filter(employee -> !orderedLocationIndexesGroupByEmployee.containsKey(employee.getId()))
                .forEach(employee -> orderedLocationIndexesGroupByEmployee.put(employee.getId(), Collections.emptyList()));
    }

    private List<Integer> getLocationOrder(List<LocationRandomKey> locationRandomKeys) {
        Integer[] indexes = new Integer[locationRandomKeys.size()];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = i;
        }
        Arrays.sort(indexes, Comparator.comparingDouble(index -> locationRandomKeys.get(index).randomKey()));

        return Arrays.stream(indexes)
                .toList();
    }

    private Location getLocationByIndex(int index) {
        return Configs.LOCATIONS.get(index);
    }
}
