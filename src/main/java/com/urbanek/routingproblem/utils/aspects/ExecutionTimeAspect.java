package com.urbanek.routingproblem.utils.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Slf4j
@Component
public class ExecutionTimeAspect {

    @Around("@annotation(executionTime)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint, ExecutionTime executionTime) throws Throwable {
        log.info("Method " + joinPoint.getSignature() + " started execution");
        long startTime = System.nanoTime();
        Object result = joinPoint.proceed();
        long endTime = System.nanoTime();
        long durationInNanos = endTime - startTime;
        double durationInSeconds = TimeUnit.SECONDS.convert(durationInNanos, TimeUnit.NANOSECONDS);

        log.info(joinPoint.getSignature() + " executed in " + durationInSeconds + " seconds");
        return result;
    }
}