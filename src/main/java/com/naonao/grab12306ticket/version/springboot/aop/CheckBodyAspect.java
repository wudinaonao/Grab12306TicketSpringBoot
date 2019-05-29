package com.naonao.grab12306ticket.version.springboot.aop;

import com.naonao.grab12306ticket.version.springboot.aop.base.AbstractAop;
import com.naonao.grab12306ticket.version.springboot.entity.IRequestInterface;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-29 03:10
 **/
@Aspect
@Component
public class CheckBodyAspect  extends AbstractAop {

    @Pointcut(value = "@annotation(com.naonao.grab12306ticket.version.springboot.annotation.CheckBody)", argNames = "value")
    public void pointCut(){

    }


    @Around(value = "@annotation(com.naonao.grab12306ticket.version.springboot.annotation.CheckBody)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        Object[] objects = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        Class returnType = ((MethodSignature) signature).getReturnType();
        for (Object object: objects){
            // interceptor login request, decrypt password
            if (object instanceof IRequestInterface){
                // write not body method
                System.out.println("i am ok");
            }
        }
        return joinPoint.proceed();
    }

}
