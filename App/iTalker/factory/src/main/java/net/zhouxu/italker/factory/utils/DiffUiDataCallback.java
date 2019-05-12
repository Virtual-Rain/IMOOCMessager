package net.zhouxu.italker.factory.utils;

import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * Created by zx on 2018/6/1.
 */

public class DiffUiDataCallback<T extends DiffUiDataCallback.UiDataDiffer<T>> extends DiffUtil.Callback {

    private List<T> mOldList,mNewList;

    public DiffUiDataCallback(List<T> mOldList,List<T>mNewList){
        this.mOldList=mOldList;
        this.mNewList=mNewList;
    }

    @Override
    public int getOldListSize() {
        //旧的数据大小
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        //新的的数据大小
        return mNewList.size();
    }

    //两个类是否是同一个，例如Id相等的User
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        T beanOld=mOldList.get(oldItemPosition);
        T beanNew=mNewList.get(newItemPosition);
        return beanNew.isSame(beanOld);
    }

    //判断相等后，进一步判断是否有数据修改
    //例如，同一个用户的不同实例，其中的name字段不同
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        T beanOld=mOldList.get(oldItemPosition);
        T beanNew=mNewList.get(newItemPosition);
        return beanNew.isUiContentSame(beanOld);
    }
    //进行比较的数据类型
    // 范型的目的，就是你是和一个你这个类型的数据进行比较
    public interface UiDataDiffer<T>{
        //传递一个旧的数据给你，问你是否和你标示的是同一个数据
        boolean isSame(T old);

        //你和酒店数据对比，内容是否相同
        boolean isUiContentSame(T old);
    }
}
