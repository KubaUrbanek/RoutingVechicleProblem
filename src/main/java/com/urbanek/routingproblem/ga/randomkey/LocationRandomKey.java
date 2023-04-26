package com.urbanek.routingproblem.ga.randomkey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class LocationRandomKey {
    private int locationId;
    private String employeeId;
    @Setter
    private double randomKey;
}
