package net.zhouxu.web.italker.push.provider;

import com.google.common.base.Strings;
import net.zhouxu.web.italker.push.bean.api.base.ResponseModel;
import net.zhouxu.web.italker.push.bean.db.User;
import net.zhouxu.web.italker.push.factory.UserFactory;
import org.glassfish.jersey.server.ContainerRequest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;

/*过滤器,用于所有的请求的接口的过滤和拦截*/
@Provider
public class AuthRequestFilter implements ContainerRequestFilter {
    //实现接口的过滤方法
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        //检查是否是登录注册接口
        String relationPath=((ContainerRequest)requestContext).getPath(false);
        if(relationPath.startsWith("account/login")||
                relationPath.startsWith("account/register")){
            //直接走正常逻辑，不做拦截
            return;
        }
        //从请求头中找到第一个token节点
        String token=requestContext.getHeaders().getFirst("token");
        if(!Strings.isNullOrEmpty(token)){
            //通过token查询用户信息
            final User self=UserFactory.findByToken(token);
            if(self!=null){
                //给当前请求添加一个上下文，写入user
                requestContext.setSecurityContext(new SecurityContext() {
                    //主体部分
                    @Override
                    public Principal getUserPrincipal() {
                        //User 实现Principal接口
                        return self;
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        //此次可以写入用户的权限，管理员等等
                        return false;
                    }

                    @Override
                    public boolean isSecure() {
                        //默认false，HTTPS
                        return false;
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        //无需处理
                        return null;
                    }
                });
                return;
            }
        }
        //直接返回账户异常需要登录
        ResponseModel model=ResponseModel.buildAccountError();

        //拦截 停止一个请求的继续下发，调用该方法后直接返回请求，不会进入Service

        //构建一个返回
        Response response=Response.status(Response.Status.OK)
                .entity(model)
                .build();
        requestContext.abortWith(response);
    }
}
