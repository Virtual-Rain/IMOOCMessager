package net.zhouxu.italker.factory.model.api.account;

import net.zhouxu.italker.factory.model.db.User;

/**
 * Created by zx on 2018/5/10.
 */

public class AccountRspModel {
    //用户基本信息
    private User user;
    //当前登录账号
    private String account;
    //当前登录成功后的token，通过token可以获取用户的所有信息
    private String token;
    //标识是否已经绑定了设备Id
    private boolean isBind;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }

    @Override
    public String toString() {
        return "AccountRspModel{" +
                "user=" + user +
                ", account='" + account + '\'' +
                ", token='" + token + '\'' +
                ", isBind=" + isBind +
                '}';
    }
}
