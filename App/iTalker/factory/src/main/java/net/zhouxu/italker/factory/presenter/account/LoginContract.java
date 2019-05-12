package net.zhouxu.italker.factory.presenter.account;

import net.zhouxu.italker.factory.presenter.BaseContract;

/**
 * Created by zx on 2018/5/3.
 */

public interface LoginContract {

    interface View extends BaseContract.View<Presenter>{
        //注册成功
        void loginSuccess();

    }
    interface Presenter extends BaseContract.Presenter{
        //发起注册
        void login(String phone,String password);

        //检查手机号是否正确
        boolean checkMobile(String phone);

    }
}
