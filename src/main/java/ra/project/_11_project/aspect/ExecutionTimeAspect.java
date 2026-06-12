package ra.project._11_project.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ExecutionTimeAspect {

    @Around(
            "execution(* ra.project._11_project.service.impl.*.*(..))"
    )
    public Object logExecutionTime(
            ProceedingJoinPoint joinPoint
    ) throws Throwable {

        long startTime =
                System.currentTimeMillis();

        Object result =
                joinPoint.proceed();

        long endTime =
                System.currentTimeMillis();

        long executionTime =
                endTime - startTime;

        log.info(
                "[EXECUTION TIME] {}.{}() : {} ms",
                joinPoint.getSignature()
                        .getDeclaringType()
                        .getSimpleName(),
                joinPoint.getSignature()
                        .getName(),
                executionTime
        );

        return result;
    }
}