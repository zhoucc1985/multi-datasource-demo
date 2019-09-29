package com.example.multidatasourcedemo.component;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: AcessInterceptor
 * @Auther: zhoucc
 * @Date: 2019/6/14 09:31
 * @Description: 拦截请求,添加用户参数
 */

@Component
public class AcessInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String user = request.getParameter("user");
        if (user == null || "".equals(user)) {
            request.setAttribute("user", "zhouql");
            return true;
        }
        request.setAttribute("user", user);
        return true;
    }
}
