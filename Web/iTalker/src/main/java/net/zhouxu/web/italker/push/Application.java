package net.zhouxu.web.italker.push;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import net.zhouxu.web.italker.push.provider.AuthRequestFilter;
import net.zhouxu.web.italker.push.provider.GsonProvider;
import net.zhouxu.web.italker.push.service.AccountService;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.logging.Logger;

/*@author zhouxu*/
public class Application extends ResourceConfig {
    public Application() {
        //注册逻辑处理的包名
        //packages("net.zhouxu.web.italker.push.service");
        packages(AccountService.class.getPackage().getName());

        //注册全局请求拦截
        register(AuthRequestFilter.class);

        //注册Json解析器
        //register(JacksonJsonProvider.class);
        register(GsonProvider.class);
        //注册日志打印输出
        register(Logger.class);
    }
}
