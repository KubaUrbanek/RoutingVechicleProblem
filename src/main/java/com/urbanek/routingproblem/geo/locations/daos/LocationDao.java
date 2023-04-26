package com.urbanek.routingproblem.geo.locations.daos;

import com.urbanek.routingproblem.geo.locations.dtos.Location;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LocationDao {
    private final Map<Integer, Location> inMemoryLocationDb = new HashMap<>();

    public void add(Location location) {
        inMemoryLocationDb.put(location.id(), location);
    }

    public Location getLocationById(int id) {
        return inMemoryLocationDb.get(id);
    }

    public Collection<Location> getAllCustomLocations() {
        return inMemoryLocationDb.values();
    }
}
