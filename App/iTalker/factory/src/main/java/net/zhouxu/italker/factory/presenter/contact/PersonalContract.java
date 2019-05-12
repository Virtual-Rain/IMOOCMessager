package net.zhouxu.italker.factory.presenter.contact;

import net.zhouxu.italker.factory.model.db.User;
import net.zhouxu.italker.factory.presenter.BaseContract;

/**
 * Created by zx on 2018/6/4.
 */

public interface PersonalContract {
    interface Presenter extends BaseContract.Presenter{
        //获取用户信息
        User getUserPersonal();
    }
    interface View extends BaseContract.View<Presenter>{
        String getUserId();

        //加载数据完成
        void onLoadDone(User user);
        //是否发起聊天
        void allowSayHello(boolean isAllow);

        //设置关注状态
        void setFolllowStatus(boolean isFollow);
    }
}
