package com.studygroup.discussion.aspect;

import com.studygroup.discussion.service.GroupMemberService;
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
import java.util.UUID;

@Aspect
@Component
public class MembershipCheckAspect {

    private static final Logger logger = LoggerFactory.getLogger(MembershipCheckAspect.class);
    
    private final GroupMemberService groupMemberService;

    public MembershipCheckAspect(GroupMemberService groupMemberService) {
        this.groupMemberService = groupMemberService;
    }

    @Before("execution(* com.studygroup.discussion.controller.*.*(..))")
    public void checkMembership(JoinPoint joinPoint) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String userId = request.getHeader("X-User-Id");
                String path = request.getRequestURI();
                
                // Extract group ID from path
                String[] pathParts = path.split("/");
                if (pathParts.length >= 4 && "groups".equals(pathParts[3])) {
                    try {
                        UUID groupId = UUID.fromString(pathParts[4]);
                        if (userId != null) {
                            UUID userUuid = UUID.fromString(userId);
                            
                            // Check if user is a member (either in cache or database)
                            boolean isMember = groupMemberService.isMemberCached(groupId, userUuid);
                            
                            if (isMember) {
                                logger.debug("User {} is a member of group {}", userId, groupId);
                            } else {
                                logger.debug("User {} membership status for group {} not cached", userId, groupId);
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        logger.debug("Invalid UUID in path: {}", path);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error during membership check: {}", e.getMessage());
        }
    }

    @Before("execution(* com.studygroup.discussion.service.*.*(..))")
    public void logServiceMethodCalls(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();
        
        logger.debug("Calling {}.{} with arguments: {}", 
                   className, methodName, Arrays.toString(args));
    }
}
