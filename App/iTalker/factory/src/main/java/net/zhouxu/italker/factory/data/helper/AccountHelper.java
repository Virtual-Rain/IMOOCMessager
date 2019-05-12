package net.zhouxu.italker.factory.data.helper;

import android.text.TextUtils;

import net.zhouxu.italker.factory.Factory;
import net.zhouxu.italker.factory.R;
import net.zhouxu.italker.factory.data.DataSource;
import net.zhouxu.italker.factory.model.api.RspModel;
import net.zhouxu.italker.factory.model.api.account.AccountRspModel;
import net.zhouxu.italker.factory.model.api.account.LoginModel;
import net.zhouxu.italker.factory.model.api.account.RegisterModel;
import net.zhouxu.italker.factory.model.db.User;
import net.zhouxu.italker.factory.net.NetWork;
import net.zhouxu.italker.factory.net.RemoteService;
import net.zhouxu.italker.factory.persistence.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zx on 2018/5/7.
 */

public class AccountHelper {


    /*注册的接口，异步的调用*/
    public static void register(RegisterModel model, final DataSource.Callback<User> callback) {
      /* //线程模拟网络请求
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                callback.onDataNotAvailable(R.string.data_rsp_error_parameters);
            }
        }.start();*/
        //调用Retrofit对我们的网络请求接口做代理
        RemoteService service= NetWork.remote();
        //得到一个call
        Call<RspModel<AccountRspModel>>call= service.accountRegister(model);
        //异步的请求
        call.enqueue(new AccountRspCallback(callback));
    }

    /*登录的调用*/
    public static void login(LoginModel model, final DataSource.Callback<User> callback) {
        //调用Retrofit对我们的网络请求接口做代理
        RemoteService service= NetWork.remote();
        //得到一个call
        Call<RspModel<AccountRspModel>>call= service.accountLogin(model);
        //异步的请求
        call.enqueue(new AccountRspCallback(callback));
    }

    /*对设备Id进行绑定的操作*/
    public static void  bindPush(final DataSource.Callback<User> callback){
      //检查是否为空
        String pushId=Account.getPushId();
      if(TextUtils.isEmpty(pushId))
          return;
      //调用Retrofit对我们的网络请求接口做代理
        RemoteService service= NetWork.remote();
        Call<RspModel<AccountRspModel>>call= service.accountBind(pushId);
        call.enqueue(new AccountRspCallback(callback));

    }
    /*请求的回调部分封装*/
    private static class AccountRspCallback implements Callback<RspModel<AccountRspModel>>{

        final DataSource.Callback<User> callback;

        AccountRspCallback(DataSource.Callback<User> callback){
            this.callback=callback;
        }

        @Override
        public void onResponse(Call<RspModel<AccountRspModel>> call, Response<RspModel<AccountRspModel>> response) {
            //网络请求成功
            //从返回中得到我们的全局Model，内部使用的GSON解析
            RspModel<AccountRspModel> rspModel=response.body();
            //判断绑定状态，是否绑定设备
            if(rspModel.success()){
                //拿到实体
                AccountRspModel accountRspModel=rspModel.getResult();
                //获取我的信息
                User user=accountRspModel.getUser();
                //第一种存储
                user.save();
                       /* //第二种存储,使用ModelAdapter(可存储多个)
                        FlowManager.getModelAdapter(User.class)
                                .save(user);

                        //第三种存储，放在事务中
                        DatabaseDefinition definition=FlowManager.getDatabase(AppDatabase.class);
                        definition.beginTransactionAsync(new ITransaction() {
                            @Override
                            public void execute(DatabaseWrapper databaseWrapper) {
                                FlowManager.getModelAdapter(User.class)
                                        .save(user);
                            }
                        }).build().execute();*/
                //同步到XML持久化当中
                Account.login(accountRspModel);
                if(accountRspModel.isBind()){
                    //设置绑定状态
                    Account.setBind(true);
                    //然后返回
                    if(callback!=null)
                    callback.onDataLoaded(user);
                }else {
                    //进行绑定唤起
                    bindPush(callback);
                }
            }else{
                //TODO 失败的提示 j解析RspModel中的Code
                Factory.decodeRspCode(rspModel,callback);
            }
        }

        @Override
        public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
            //网络请求失败
            if(callback!=null)
            callback.onDataNotAvailable(R.string.data_network_error);
        }
    }
}
