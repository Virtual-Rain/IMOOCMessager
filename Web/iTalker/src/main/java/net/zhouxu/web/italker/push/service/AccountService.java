package net.zhouxu.web.italker.push.service;

import net.zhouxu.web.italker.push.bean.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


/*@author zhouxu*/
// 127.0.0.1/api/account/...
@Path("/account")
public class AccountService {
    // 127.0.0.1/api/account/login
    @GET
    @Path("/login")
    public String get(){
        return "You get the login";
    }
    //POST 127.0.0.1/api/account/login
    @POST
    //请求与返回格式的响应体味JSON
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User post(){
        User user=new User();
        user.setName("帅哥");
        user.setSex(2);
        return user;
    }
}
