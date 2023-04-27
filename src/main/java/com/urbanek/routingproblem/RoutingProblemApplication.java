package com.urbanek.routingproblem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class RoutingProblemApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoutingProblemApplication.class, args);
    }
}
