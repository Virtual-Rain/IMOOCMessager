package net.zhouxu.italker.italker.push;

import com.igexin.sdk.PushManager;

import net.zhouxu.italker.common.app.Application;
import net.zhouxu.italker.factory.Factory;

/**
 * Created by zx on 2018/4/27.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //调用Factory进行初始
        Factory.setUp();
        //推送进行初始化
        PushManager.getInstance().initialize(this);
    }
}
