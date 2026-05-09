package com.studygroup.user.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

@Aspect
@Component
public class ValidationAspect {

    private static final Logger logger = LoggerFactory.getLogger(ValidationAspect.class);
    
    private final Validator validator;

    public ValidationAspect() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Before("execution(* com.studygroup.user.controller.*.*(..))")
    public void validateControllerInputs(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        
        for (Object arg : args) {
            if (arg != null) {
                Set<ConstraintViolation<Object>> violations = validator.validate(arg);
                
                if (!violations.isEmpty()) {
                    StringBuilder errorMessage = new StringBuilder("Validation failed for ");
                    errorMessage.append(arg.getClass().getSimpleName()).append(": ");
                    
                    for (ConstraintViolation<Object> violation : violations) {
                        errorMessage.append(violation.getPropertyPath())
                                   .append(" ")
                                   .append(violation.getMessage())
                                   .append("; ");
                    }
                    
                    logger.warn(errorMessage.toString());
                    
                    // For now, just log. In a real application, you might throw an exception
                    // throw new IllegalArgumentException(errorMessage.toString());
                }
            }
        }
    }
}
