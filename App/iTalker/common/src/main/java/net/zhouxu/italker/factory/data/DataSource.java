package net.zhouxu.italker.factory.data;

import android.support.annotation.StringRes;

/**
 * Created by zx on 2018/5/7.
 * 数据源接口定义
 */

public interface DataSource{

    /*同时包括成功与失败的回调*/
    interface Callback<T>extends SuccessCallback<T>,FailedCallback<T>{}

    /*只关注成功的接口*/
    interface SuccessCallback<T>{
        //数据加载成功
        void onDataLoaded(T data);
    }
    /*只关注失败的接口*/
    interface FailedCallback<T>{
        //数据加载失败。
        void onDataNotAvailable(@StringRes int strRes);
    }
}
