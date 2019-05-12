package net.zhouxu.italker.factory;

import android.support.annotation.StringRes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import net.zhouxu.italker.common.app.Application;
import net.zhouxu.italker.factory.data.DataSource;
import net.zhouxu.italker.factory.model.api.RspModel;
import net.zhouxu.italker.factory.persistence.Account;
import net.zhouxu.italker.factory.utils.DBFlowExclusionStrategy;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by zx on 2018/4/28.
 */

public class Factory {
    //单例模式
    private static final Factory instance;
    //全局的线程池
    private final Executor executor;
    //全局的Gson
    private final Gson gson;

    static {
        instance = new Factory();
    }

    private Factory() {
        //新建一个4个线程的线程池
        executor = Executors.newFixedThreadPool(4);
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                //设置一个过滤器,数据库级别的Model不进行json转换
                .setExclusionStrategies(new DBFlowExclusionStrategy())
                .create();
    }

    /*Factory 中的初始化*/
    public static void setUp(){
        //初始化数据库
        FlowManager.init(new FlowConfig.Builder(app())
        .openDatabasesOnInit(true)//数据库初始化的时候打开数据库
        .build());

        //持久化的数据进行初始化
        Account.load(app());
    }

    /*返回全局的Application*/
    public static Application app() {
        return Application.getInstance();
    }

    /*异步运行的方法*/
    public static void runOnAsync(Runnable runnable) {
        //拿到单例的线程池，然后异步执行
        instance.executor.execute(runnable);
    }

    /*返回全局的gson 可进行初始化*/
    public static Gson getGson() {
        return instance.gson;
    }

    /*进行错误数据的解析，网络返回的code值进行统一的规划，并返回位一个String 资源*/
    public static void decodeRspCode(RspModel model, DataSource.FailedCallback callback) {
        if (model == null)
            return;
        switch (model.getCode()) {
            case RspModel.SUCCEED:
                return;
            case RspModel.ERROR_SERVICE:

                decodeRspCode(R.string.data_rsp_error_service, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_USER:
                decodeRspCode(R.string.data_rsp_error_not_found_user, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP:
                decodeRspCode(R.string.data_rsp_error_not_found_group, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP_MEMBER:
                decodeRspCode(R.string.data_rsp_error_not_found_group_member, callback);
                break;
            case RspModel.ERROR_CREATE_USER:
                decodeRspCode(R.string.data_rsp_error_create_user, callback);
                break;
            case RspModel.ERROR_CREATE_GROUP:
                decodeRspCode(R.string.data_rsp_error_create_group, callback);
                break;
            case RspModel.ERROR_CREATE_MESSAGE:
                decodeRspCode(R.string.data_rsp_error_create_message, callback);
                break;
            case RspModel.ERROR_PARAMETERS:
                decodeRspCode(R.string.data_rsp_error_parameters, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_ACCOUNT:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_account, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_NAME:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_name, callback);
                break;
            case RspModel.ERROR_ACCOUNT_TOKEN:
                Application.showToast(R.string.data_rsp_error_account_token);
                instance.logout();
                break;
            case RspModel.ERROR_ACCOUNT_LOGIN:
                decodeRspCode(R.string.data_rsp_error_account_login, callback);
                break;
            case RspModel.ERROR_ACCOUNT_REGISTER:
                decodeRspCode(R.string.data_rsp_error_account_register, callback);
                break;
            case RspModel.ERROR_ACCOUNT_NO_PERMISSION:
                decodeRspCode(R.string.data_rsp_error_account_no_permission, callback);
                break;
            case RspModel.ERROR_UNKNOWN:
            default:
                decodeRspCode(R.string.data_rsp_error_unknown, callback);
                break;
        }
    }

    private static void decodeRspCode(@StringRes final int resId,
                                      final DataSource.FailedCallback callback) {
        if (callback != null)
            callback.onDataNotAvailable(resId);
    }
    /*账户退出的消息，进行账户推出，重新登录*/
    private  void logout(){

    }

    /*处理推送来的消息*/
    public static void dispatchPush(String message){
        //TODO
    }
}
