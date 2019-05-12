package net.zhouxu.italker.factory.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import net.zhouxu.italker.factory.Factory;
import net.zhouxu.italker.factory.model.api.account.AccountRspModel;
import net.zhouxu.italker.factory.model.db.User;
import net.zhouxu.italker.factory.model.db.User_Table;

/**
 * Created by zx on 2018/5/11.
 */

public class Account {

    private static final String KEY_PUSH_ID="KEY_PUSH_ID";
    private static final String KEY_IS_BIND="KEY_IS_BIND";
    private static final String KEY_TOKEN="KEY_TOKEN";
    private static final String KEY_USER_ID="KEY_USER_ID";
    private static final String KEY_ACCOUNT="KEY_ACCOUNT";


    //设备的推送Id
    private static String pushId ;
    //设备Id是否已经绑定到了服务器
    private static boolean isBind;
    //登录状态的Token,用来接口请求
    private static String token;
    //登录的用户ID
    private static String userId;
    //登录的账户
    private static String account;

    //存储到XML文件，持久化

    private static void save(Context context){
        //获取数据持久化的SP
        SharedPreferences sp=context.getSharedPreferences(Account.class.getName(),
                Context.MODE_PRIVATE);
        //存储数据
        sp.edit().putString(KEY_PUSH_ID,pushId)
                .putBoolean(KEY_IS_BIND,isBind)
                .putString(KEY_TOKEN,token)
                .putString(KEY_USER_ID,userId)
                .putString(KEY_ACCOUNT,account)
                .apply();
    }

    /*进行数据加载*/
    public static void load(Context context){
        //获取数据持久化的SP
        SharedPreferences sp=context.getSharedPreferences(Account.class.getName(),
                Context.MODE_PRIVATE);
        pushId=sp.getString(KEY_PUSH_ID,"");
        isBind=sp.getBoolean(KEY_IS_BIND,false);
        token=sp.getString(KEY_TOKEN,"");
        userId=sp.getString(KEY_USER_ID,"");
        account=sp.getString(KEY_ACCOUNT,"");
    }

    /*获取推送的Id*/
    public static String getPushId() {
        return pushId;
    }

    /*设置并存储设备的Id*/
    public static void setPushId(String pushId) {
        Account.pushId = pushId;
        Account.save(Factory.app());
    }

    /*返回当前账户是否登录*/
    public static boolean isLogin(){
        //用户Id和Token 不为空
       return !TextUtils.isEmpty(userId)
                &&!TextUtils.isEmpty(token);
    }
    /*是否已经完善了用户信息*/
    public static boolean isComplete(){
        //TODO
        return isLogin();
    }

    /*是否已经绑定到了服务器*/
    public static boolean isBind(){
        return isBind;
    }

    /*设置绑定状态*/
    public static void setBind(boolean isBind){
        Account.isBind=isBind;
        Account.save(Factory.app());
    }
    /*保存AccountRspModel的用户信息到持久化XML中*/
    public static void login(AccountRspModel model){
        //存储当前登录账户的token，用户Id,方便从数据库中查询我的信息
        Account.token=model.getToken();
        Account.account=model.getAccount();
        Account.userId=model.getUser().getId();
        save(Factory.app());
    }
    /*获取当前登录的信息*/
    public static User getUser(){
        return TextUtils.isEmpty(userId)?new User(): SQLite.select()
                .from(User.class)
                .where(User_Table.id.eq(userId))
                .querySingle();
    }
    /*获取token*/
    public static String getToken(){
        return token;
    }
    /*获取当前用户Id*/
    public static String getUserId(){
        return getUser().getId();
    }
}
