package com.example.multidatasourcedemo.pojo;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @ClassName: TestRealm
 * @Auther: zhoucc
 * @Date: 2019/7/16 16:04
 * @Description: 自定义Realm类
 */
@Slf4j
public class TestRealm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("执行授权逻辑");
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("执行认证逻辑");
        String name = "fzy";
        String password = "fzy";
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
        //如果用户名不存在
        if(!token.getUsername().equals(name)) {
            return null;		//shiro底层会抛出UnknownAccountException
        }
        //2.判断密码
        return new SimpleAuthenticationInfo("", password, "");
    }
}
