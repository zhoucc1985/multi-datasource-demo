package com.example.multidatasourcedemo.component;

import com.example.multidatasourcedemo.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @ClassName: WebLogAspect
 * @Auther: zhoucc
 * @Date: 2019/6/13 15:24
 * @Description: AOP切面定义
 */

@Aspect
@Component
@Slf4j
public class WebLogAspect {

    ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * 切入点
     */
    @Pointcut("execution(public * com.example.multidatasourcedemo.controller..*(..))")
    public void webLog(){}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        startTime.set(System.currentTimeMillis());
        log.info(startTime.toString() + ": 开始时间" + startTime.get());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        log.info("URL : {}", request.getRequestURL().toString());
        log.info("HTTP_METHOD : {}", request.getMethod());
        log.info("IP : {}", IpUtil.getIpAddr(request));
        log.info("CLASS_METHOD : {}", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.info("ARGS : {}", Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        log.info("RESPONSE : {}", ret);
        log.info("SPEND TIME : " + (System.currentTimeMillis() - startTime.get()));
        log.info(startTime.toString() + "结束时间时的开始时间：" + startTime.get());
    }

    @AfterThrowing(pointcut = "webLog()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        log.error("Exception : {}", e.toString());
        log.info("SPEND TIME : " + (System.currentTimeMillis() - startTime.get()));
        log.info(startTime.toString());
        log.info(startTime.toString() + "结束时间时的开始时间：" + startTime.get());
    }
}
