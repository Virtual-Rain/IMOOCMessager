package net.zhouxu.italker.factory.presenter.account;

import android.text.TextUtils;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.zhouxu.italker.factory.R;
import net.zhouxu.italker.factory.data.DataSource;
import net.zhouxu.italker.factory.data.helper.AccountHelper;
import net.zhouxu.italker.factory.model.api.account.LoginModel;
import net.zhouxu.italker.factory.model.db.User;
import net.zhouxu.italker.factory.presenter.BasePresenter;

/**
 * Created by zx on 2018/5/11.
 * 登录的逻辑实现
 */

public class LoginPresenter extends BasePresenter<LoginContract.View>
        implements LoginContract.Presenter, DataSource.Callback<User> {
    public LoginPresenter(LoginContract.View view) {
        super(view);
    }


    @Override
    public void login(String phone, String password) {
        start();

        final LoginContract.View view = getView();

        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            view.showError(R.string.data_account_login_invalid_parameter);
        } else {
            //尝试传递PushId
            LoginModel model = new LoginModel(phone, password);
            AccountHelper.login(model, this);
        }
    }

    @Override
    public boolean checkMobile(String phone) {
        return false;
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        //当网络请求告知登录失败
        final LoginContract.View view=getView();
        if(view==null)
            return;
        //此时是从网络会送回来的，并不保证处于主线程状态
        //强制执行到主线程中
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                //显示错误
                view.showError(strRes);
            }
        });
    }

    @Override
    public void onDataLoaded(User data) {
        //当网络请求成功，登录
        final LoginContract.View view = getView();
        if (view == null)
            return;
        //强制执行到主线程中
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                //调用登录界面成功
                view.loginSuccess();
            }
        });
    }
}
