package com.example.multidatasourcedemo.Component;

import java.lang.annotation.*;

/**
 * @ClassName: IP
 * @Auther: zhoucc
 * @Date: 2019/6/13 17:06
 * @Description:
 */

@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface CurrentUser {

    String user() default "user";

    boolean required() default true;

    String defaultValue() default "";
}
