package ua.laboratory.lab_spring_task.util.aspect_logger;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private static final String REQUEST_ID = "requestId";

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerMethods() {}

    @Before("controllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        String requestId = UUID.randomUUID().toString();
        MDC.put(REQUEST_ID, requestId);

        logger.info("Method call: {} with arguments: {}",
                joinPoint.getSignature(), Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("Method: {} finished successfully. Result: {}",
                joinPoint.getSignature(), result);
        MDC.clear();
    }

    @AfterThrowing(pointcut = "controllerMethods()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        logger.error("Method: {} threw exception: {}",
                joinPoint.getSignature(), exception.getMessage(), exception);
        MDC.clear();
    }
}
