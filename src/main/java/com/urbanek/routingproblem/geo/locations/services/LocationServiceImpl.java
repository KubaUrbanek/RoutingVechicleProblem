package com.urbanek.routingproblem.geo.locations.services;

import com.urbanek.routingproblem.employes.services.EmployeeService;
import com.urbanek.routingproblem.ga.config.Configs;
import com.urbanek.routingproblem.ga.randomkey.LocationRandomKey;
import com.urbanek.routingproblem.geo.locations.daos.LocationDao;
import com.urbanek.routingproblem.geo.locations.dtos.Coordinates;
import com.urbanek.routingproblem.geo.locations.dtos.Location;
import com.urbanek.routingproblem.utils.aspects.ExecutionTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationDao locationDao;
    private final EmployeeService employeeService;
    private final Random rand = new Random();

    @Override
    public List<Location> getAllLocations() {
        return Stream.concat(locationDao.getAllCustomLocations().stream(), Stream.of(Configs.DEPOT))
                .toList();
    }

    @Override
    public Collection<Location> getAllCustomerLocations() {
        return locationDao.getAllCustomLocations();
    }

    @Override
    public Map<String, List<Location>> getOrderedLocationGroupByEmployee(List<LocationRandomKey> locationRandomKeys) {
        List<LocationRandomKey> locationRandomKeysCopy = new ArrayList<>(locationRandomKeys);
        Map<String, List<Location>> orderedLocationIndexesGroupByEmployee = groupLocationByEmployees(locationRandomKeysCopy);
        fillWithMissingEmployees(orderedLocationIndexesGroupByEmployee);
        addDepotToAllEmployees(orderedLocationIndexesGroupByEmployee);

        return orderedLocationIndexesGroupByEmployee;
    }

    @Override
    @ExecutionTime
    public void prepareRandomLocations(int amount) {
        IntStream.rangeClosed(1, amount)
                .forEach(id -> locationDao.add(new Location(id, new Coordinates(rand.nextDouble(100), rand.nextDouble(100)),
                        rand.nextInt(100))));
    }

    private void addDepotToAllEmployees(Map<String, List<Location>> orderedLocationIndexesGroupByEmployee) {
        orderedLocationIndexesGroupByEmployee.values()
                .forEach(locations -> locations.add(0, Configs.DEPOT));
    }

    private Map<String, List<Location>> groupLocationByEmployees(List<LocationRandomKey> locationRandomKeys) {
        List<Integer> locationIdsInOrder = getLocationIdsInOrder(locationRandomKeys);
        Map<Integer, String> locationToEmployeeIdMap = locationRandomKeys.stream()
                .collect(Collectors.toUnmodifiableMap(LocationRandomKey::getLocationId,
                        LocationRandomKey::getEmployeeId,
                        (first, second) -> first));

        return new HashMap<>(locationIdsInOrder.stream()
                .collect(Collectors.toUnmodifiableMap(locationToEmployeeIdMap::get,
                        (locationId) -> new ArrayList<>(Collections.singletonList(getLocationById(locationId))),
                        (existingList, newList) -> {
                            existingList.addAll(newList);
                            return existingList;
                        })));
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
        return locationDao.getLocationById(id);
    }
}
