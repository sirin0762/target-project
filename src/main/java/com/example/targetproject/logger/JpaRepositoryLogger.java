package com.example.targetproject.logger;

import com.example.targetproject.logger.dto.LocalQueryCounter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;

@Aspect
@Component
public class JpaRepositoryLogger {

    private static final Logger log = LoggerFactory.getLogger(JpaRepositoryLogger.class);

    private static final long SLOW_QUERY_THRESHOLD = 100;

    @Around("execution(* org.springframework.data.repository.CrudRepository+.*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = extractFullMethodName(joinPoint);
        long startTime = System.currentTimeMillis();

        ThreadLocal<LocalQueryCounter> threadLocal = new ThreadLocal<>();
        threadLocal.set(new LocalQueryCounter(methodName, startTime));

        Object result = joinPoint.proceed();

        LocalQueryCounter queryCount = threadLocal.get();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - queryCount.getStartTime();

        // 느린 메서드 감지
        if (executionTime > SLOW_QUERY_THRESHOLD) {
            log.info("⚠️ Slow jpa method detected: {} took {} ms", queryCount.getMethodName(), executionTime);
        }

        for(Map.Entry<String, Integer> queryResult: queryCount.getQueryCounter().entrySet()) {
            log.info("Invoked query information in method - invoked count: {}, invoked query string: {}", queryResult.getValue(), queryResult.getKey());
        }

        return result;
    }

    private String extractFullMethodName(ProceedingJoinPoint joinPoint) {
        String repositoryInterface = Arrays.stream(joinPoint.getTarget().getClass().getInterfaces())
            .map(Class::getSimpleName)
            .filter(name -> !name.startsWith("JpaRepository"))
            .findFirst()
            .orElse("UnknownRepository");
        String methodName = joinPoint.getSignature().getName();

        return repositoryInterface + "." + methodName;
    }

}
