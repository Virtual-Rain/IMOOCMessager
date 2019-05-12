package net.zhouxu.web.italker.push.service;

import net.zhouxu.web.italker.push.bean.db.User;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

public class BaseService {
    //添加一个上下文注解，该注解会给securityContext赋值
    //具体的值为我们拦截器中返回的SecurityContext
    @Context
    protected SecurityContext securityContext;

    /*从上线文中直接获取个人信息*/
    protected User getSelf() {
        return (User) securityContext.getUserPrincipal();
    }
}
