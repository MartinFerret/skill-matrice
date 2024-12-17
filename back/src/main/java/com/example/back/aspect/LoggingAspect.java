package com.example.back.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Before("execution(* com.example.back.service.*.*.*(..))")
    public void beforeMethods(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        log.info("Method : {} called in class: {}", methodName, className);
    }

    @AfterThrowing(pointcut = "execution(* com.example.back.service.*.*.*(..))", throwing = "ex")
    public void afterThrowingMethods(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        log.error("Exception thrown: {} in method: {} called in class: {}", ex.getMessage(), methodName, className);
    }

    @Before("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void beforeTransactionalMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        log.info("Starting Transaction: Method {} called in class {}", methodName, className);
    }

    @AfterReturning("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void afterSuccessfulTransactionalMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        log.info("Transaction completed successfully: Method {} called in class {}", methodName, className);
    }

    @AfterThrowing(value = "@annotation(org.springframework.transaction.annotation.Transactional)", throwing = "ex")
    public void afterTransactionException(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        log.error("Exception thrown during transaction: Method {} called in class {}", methodName, className);
    }
}
