package com.studygroup.auth.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.studygroup.auth.controller.*.*(..))")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        logger.info("Entering {}.{}", className, methodName);
        
        try {
            Object result = joinPoint.proceed();
            logger.info("Successfully executed {}.{}", className, methodName);
            return result;
        } catch (Exception e) {
            logger.error("Error in {}.{}: {}", className, methodName, e.getMessage());
            throw e;
        }
    }

    @Around("execution(* com.studygroup.auth.service.*.*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        logger.debug("Executing service method {}.{}", className, methodName);
        
        try {
            Object result = joinPoint.proceed();
            logger.debug("Completed service method {}.{}", className, methodName);
            return result;
        } catch (Exception e) {
            logger.error("Service method error {}.{}: {}", className, methodName, e.getMessage());
            throw e;
        }
    }
}
