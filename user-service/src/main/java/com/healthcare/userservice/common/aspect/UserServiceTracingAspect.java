package com.healthcare.userservice.common.aspect;

import com.healthcare.userservice.common.logger.UserServiceLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserServiceTracingAspect {

    private final UserServiceLogger userServiceLogger;

    public UserServiceTracingAspect(UserServiceLogger userServiceLogger) {
        this.userServiceLogger = userServiceLogger;
    }

    public void log(ProceedingJoinPoint joinPoint) throws Throwable {
        userServiceLogger.trace(joinPoint.getSignature().toString());
        joinPoint.proceed();
        userServiceLogger.trace(joinPoint.getSignature().toString());
    }

}
