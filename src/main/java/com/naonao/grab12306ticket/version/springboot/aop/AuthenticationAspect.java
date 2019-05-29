package com.naonao.grab12306ticket.version.springboot.aop;

import com.naonao.grab12306ticket.version.springboot.aop.base.AbstractAop;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-29 01:46
 **/
@Aspect
@Component
public class AuthenticationAspect extends AbstractAop {



    @Pointcut(value = "@annotation(com.naonao.grab12306ticket.version.springboot.annotation.Authentication)", argNames = "value")
    public void pointCut(){

    }

    @Around(value = "@annotation(com.naonao.grab12306ticket.version.springboot.annotation.Authentication)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        Object[] objects = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        Class returnType = ((MethodSignature) signature).getReturnType();
        for (Object object: objects){
            // interceptor login request, decrypt password
            if (object instanceof HttpServletRequest){
                HttpServletRequest request = (HttpServletRequest) object;
                String username12306 = (String) request.getSession().getAttribute("username12306");
                String password12306 = (String) request.getSession().getAttribute("password12306");
                if (username12306 == null || password12306 == null){
                    return response(returnType);
                }
            }
        }
        return joinPoint.proceed();
    }



    private Object response(Class clazz)throws Exception{
        Object object = clazz.getDeclaredConstructor().newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field: fields){
            field.setAccessible(true);
            switch (field.getName()){
                case "status":
                    field.set(object, false);
                    break;
                case "httpStatus":
                    field.set(object, HTTP_SUCCESS);
                    break;
                case "message":
                    field.set(object, USER_NOT_LOGGED_IN);
                    break;
                default:
                    break;
            }
        }
        return object;
    }
}
