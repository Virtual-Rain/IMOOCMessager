package net.zhouxu.italker.factory.presenter.contact;

import net.zhouxu.italker.factory.model.card.UserCard;
import net.zhouxu.italker.factory.presenter.BaseContract;

/**
 * 关注的接口定义
 * Created by zx on 2018/5/31.
 */

public interface FollowContract {
    //任务调度者
    interface Presenter extends BaseContract.Presenter{
       //关注一个人
        void follow(String id);
    }
    interface View extends BaseContract.View<Presenter>{
        //成功的情况下返回一个用户信息
        void onFollowSucceed(UserCard userCard);
    }
}
