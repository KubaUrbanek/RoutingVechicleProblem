package com.urbanek.routingproblem.geo.locations.services;

import com.urbanek.routingproblem.ga.randomkey.LocationRandomKey;
import com.urbanek.routingproblem.geo.locations.dtos.Location;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface LocationService {

    List<Location> getAllLocations();

    Collection<Location> getAllCustomerLocations();

    Map<String, List<Location>> getOrderedLocationGroupByEmployee(List<LocationRandomKey> locationRandomKeys);

    void prepareRandomLocations(int amount);
}
