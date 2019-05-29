package com.naonao.grab12306ticket.version.springboot.annotation;


import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

@Target({
        ElementType.METHOD
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface Authentication {

}
