package net.zhouxu.italker.italker.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.igexin.sdk.PushConsts;

import net.zhouxu.italker.factory.Factory;
import net.zhouxu.italker.factory.data.helper.AccountHelper;
import net.zhouxu.italker.factory.persistence.Account;

/**
 * 个推的消息接收器
 * Created by zx on 2018/5/11.
 */

public class MessageReceiver extends BroadcastReceiver {

    public static final String TAG=MessageReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent==null)
            return;

        Bundle bundle=intent.getExtras();

        //判断当前消息的意图
        switch (bundle.getInt(PushConsts.CMD_ACTION)){
            case PushConsts.GET_CLIENTID:{
                Log.i(TAG,"GET_CLIENTID:"+bundle.toString());
                //当Id初始化的时候
                //获取设备Id
                onClientInit(bundle.getString("clientid"));
                break;
            }
            case PushConsts.GET_MSG_DATA:{
                //常规的消息送达
                byte[]payload=bundle.getByteArray("payload");
                if(payload!=null){
                    String message =new String(payload);
                    Log.i(TAG,"GET_MSG_DATA:"+message);
                    onMessageArrived(message);
                }
                break;
            }
            default:
                Log.i(TAG,"OTHER:"+bundle.toString());
                break;
        }

    }
    /*当Id初始化的时候*/
    private void onClientInit(String cid){
        //设置设备Id
        Account.setPushId(cid);
        if(Account.isLogin()){
            //账户登录状态，进行一次PushId绑定
            //没有登录不能绑定PushId
            AccountHelper.bindPush(null);
        }
    }
    /*消息送达*/
    private void onMessageArrived(String message){
        //交给Factory处理
        Factory.dispatchPush(message);
    }
}
