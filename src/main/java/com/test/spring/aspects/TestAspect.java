package com.test.spring.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TestAspect {

    @Around("execution(* com.test.spring.controllers.*.*(..))")
    public Object testMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println(joinPoint.getSignature());
        return joinPoint.proceed();
    }

}
