package com.example.multidatasourcedemo.component;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;
import org.springframework.stereotype.Component;

/**
 * @ClassName: MySessionListener2
 * @Auther: zhoucc
 * @Date: 2019/7/19 10:05
 * @Description:
 */

@Slf4j
@Component
public class MySessionListener2 extends SessionListenerAdapter {

    @Override
    public void onStart(Session session) {
        //会话创建时触发
        log.info("会话创建: + session.getId()");
    }
}
