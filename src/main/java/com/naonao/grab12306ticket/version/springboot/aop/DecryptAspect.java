package com.naonao.grab12306ticket.version.springboot.aop;

import com.naonao.grab12306ticket.version.springboot.aop.base.AbstractAop;
import com.naonao.grab12306ticket.version.springboot.entity.request.ChangePasswordRequest;
import com.naonao.grab12306ticket.version.springboot.entity.request.LoginRequest;
import com.naonao.grab12306ticket.version.springboot.util.RSAUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-29 00:37
 **/
@Aspect
@Component
public class DecryptAspect  extends AbstractAop {

    @Autowired
    private RSAUtil rsaUtil;

    @Pointcut(value = "@annotation(com.naonao.grab12306ticket.version.springboot.annotation.Decrypt)", argNames = "value")
    public void pointCut(String value){

    }

    @Before(value = "@annotation(com.naonao.grab12306ticket.version.springboot.annotation.Decrypt)")
    public void before(JoinPoint joinPoint){
        Object[] objects = joinPoint.getArgs();
        for (Object object: objects){
            // interceptor login request, decrypt password
            if (object instanceof LoginRequest){
                LoginRequest loginRequest = (LoginRequest) object;
                loginRequest.setPassword12306(rsaUtil.privateDecrypt(loginRequest.getPassword12306()));
            }
            // interceptor change password request, decrypt old and new password
            if (object instanceof ChangePasswordRequest){
                ChangePasswordRequest changePasswordRequest = (ChangePasswordRequest) object;
                changePasswordRequest.setOldPassword12306(rsaUtil.privateDecrypt(changePasswordRequest.getOldPassword12306()));
                changePasswordRequest.setNewPassword12306(rsaUtil.privateDecrypt(changePasswordRequest.getNewPassword12306()));
            }
        }
    }

}
