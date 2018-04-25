package net.zhouxu.italker.italker.push.helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;


/**
 * 完成对Fragment的调度与重用问题
 * 达到最优的Fragment切换
 * Created by zx on 2018/4/24.
 */

public class NavHelper<T> {
    //所有的Tab集合
    private final SparseArray<Tab<T>> tabs = new SparseArray();

    //用于初始化的必须参数
    private final Context context;
    private final FragmentManager fragmentManager;
    private final int containerId;
    private final OnTabChangedListener<T> listener;
    //当前的Tab
    private Tab<T> currentTab;

    public NavHelper(Context context, FragmentManager fragmentManager,
                     int containerId, OnTabChangedListener<T> listener) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
        this.listener = listener;
    }

    //添加Tab
    public NavHelper<T> add(int meunId, Tab<T> tab) {
        tabs.put(meunId, tab);
        return this;
    }

    //返回当前的Tab
    public Tab<T> getCurrentTab() {
        return currentTab;
    }

    //执行点击菜单操作
    public boolean performClickMenu(int menuId) {
        //集合中寻找点击的菜单对应的Tab如果有则进行处理
        Tab<T> tab = tabs.get(menuId);
        if (tab != null) {
            doSelect(tab);
            return true;
        }
        return false;
    }

    //真是的Tab选择操作
    private void doSelect(Tab<T> tab) {
        Tab<T> oldTab = null;
        if (currentTab != null) {
            oldTab = currentTab;
            if (oldTab == tab) {
                //如果当前的Tab就是点击的Tab,不做处理
                notifyTabReselect(tab);
                return;
            }
        }
        currentTab = tab;
        doTabChanged(currentTab, oldTab);
    }

    private void doTabChanged(Tab<T> newTab, Tab<T> oldTab) {
        //Fragment的事务
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (oldTab != null) {
            if (oldTab.fragment != null) {
                //从界面移除，内存当中还存在Fragment的缓存空间中
                ft.detach(oldTab.fragment);
            }
        }
        if (newTab != null) {
            if (newTab.fragment == null) {
                //新建一个Fragment
                Fragment fragment = Fragment.instantiate(context, newTab.clx.getName(), null);
                //缓存起来
                newTab.fragment = fragment;
                //提交到FragmentManger
                ft.add(containerId, fragment, newTab.clx.getName());
            } else {
                ft.attach(newTab.fragment);
            }
        }
        ft.commit();
        //通知回调
        notifyTabSelect(newTab, oldTab);
    }

    //通知界面做处理,回调监听器
    private void notifyTabSelect(Tab<T> newTab, Tab<T> oldTab) {
        if (listener != null) {
            listener.onTabChanged(newTab,oldTab);
        }
    }

    //二次点击同一个Tab 所做的操作
    private void notifyTabReselect(Tab<T> tab) {

    }

    //Tab基础属性
    public static class Tab<T> {
        public Tab(Class<?> clx,T extra){
            this.clx=clx;
            this.extra=extra;
        }
        //Fragment对应的信息
        public Class<?> clx;
        //额外的字段，用户自己设定需要使用什么
        public T extra;
        //Package权限，外部无法使用；
        Fragment fragment;
    }

    //事件机制 回调接口
    public interface OnTabChangedListener<T> {
        void onTabChanged(Tab<T> newTab, Tab<T> oldTab);
    }
}
