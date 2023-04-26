package com.urbanek.routingproblem.utils.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Aspect
@Slf4j
@Component
public class CacheHitAspect {
    private final Map<String, Integer> hitCounter = new HashMap<>();

    @Around("@annotation(cacheHit)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint, CacheHit cacheHit) throws Throwable {
        Object result = joinPoint.proceed();
        if (Objects.nonNull(result)) {
            String cacheName = joinPoint.getSignature().getDeclaringType().getName();

            hitCounter.compute(cacheName, (k, v) -> Integer.sum((Objects.isNull(v) ? 0 : v), 1));
            log.info("Cache" + cacheName + " was hit. \n "
                    + "This cache was hit " + hitCounter.get(cacheName) + " times already");
        }
        return result;
    }
}