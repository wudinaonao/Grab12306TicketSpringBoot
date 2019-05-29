package com.naonao.grab12306ticket.version.springboot.annotation;

import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-28 14:23
 **/
@Target({
        ElementType.METHOD
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface Decrypt {
    String value() default "";
}
