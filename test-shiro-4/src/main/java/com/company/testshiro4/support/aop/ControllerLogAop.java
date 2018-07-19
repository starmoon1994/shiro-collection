package com.company.testshiro4.support.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 对controller的请求参数进行记录
 */
@Component
@Aspect
@Order(10) //构建执行顺序
public class ControllerLogAop {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(ControllerLogAop.class);

    // 用@Pointcut来注解一个切入方法
    @Pointcut("execution(* com.company.testshiro4.controller.*.*(..))")
    public void myPointcutController() {
    }

    @Before("myPointcutController()")
    public void deBefore(JoinPoint joinPoint) throws Throwable {
//        System.out.println("@Before：目标方法为：" + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
//        System.out.println("@Before：参数为：" + Arrays.toString(joinPoint.getArgs()));
        logger.info("@Before：目标方法为：{}", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("@Before：参数为：{}", Arrays.toString(joinPoint.getArgs()));

    }

}
