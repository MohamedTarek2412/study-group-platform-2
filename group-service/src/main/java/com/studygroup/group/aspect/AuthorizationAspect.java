package com.studygroup.group.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class AuthorizationAspect {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationAspect.class);

    @Before("@annotation(org.springframework.security.access.prepost.PreAuthorize)")
    public void checkAuthorization(JoinPoint joinPoint) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String userId = request.getHeader("X-User-Id");
                String userRole = request.getHeader("X-User-Role");
                
                logger.debug("Authorization check - User ID: {}, Role: {}, Method: {}", 
                           userId, userRole, joinPoint.getSignature().getName());
                
                // Log the authorization attempt
                if (userId == null) {
                    logger.warn("Authorization check failed - No user ID found in headers");
                }
                
                // Additional authorization logic can be added here
                // For now, we just log the attempt
            }
        } catch (Exception e) {
            logger.error("Error during authorization check: {}", e.getMessage());
        }
    }

    @Before("execution(* com.studygroup.group.service.*.*(..))")
    public void logServiceMethodCalls(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();
        
        logger.debug("Calling {}.{} with arguments: {}", 
                   className, methodName, Arrays.toString(args));
    }
}
