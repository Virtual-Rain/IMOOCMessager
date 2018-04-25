package net.zhouxu.italker.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.zhouxu.italker.common.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by zx on 2018/4/19.
 */

public abstract class Fragment extends android.support.v4.app.Fragment {
    protected View mRoot;
    protected Unbinder mRootUnBinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //初始化参数
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null) {
            int layId = getContentLayoutId();
            //初始化当前的根布局，但是不在创建时就添加到container里边
            View root = inflater.inflate(layId, container, false);
            initWidget(root);
            mRoot = root;
        } else {
            if (mRoot.getParent() != null) {
                //把当前Root从其父控件中移除
                ((ViewGroup) mRoot.getParent()).removeView(mRoot);
            }
        }
        return mRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //View创建完成初始化数据
        //initWidget(view);
        initData();
    }

    //初始化相关参数
    protected void initArgs(Bundle bundle) {
    }

    //得到当前界面资源文件Id
    protected abstract int getContentLayoutId();

    //初始化控件
    protected void initWidget(View root) {
        mRootUnBinder = ButterKnife.bind(this,root);
    }

    //初始化数据
    protected void initData() {

    }

    /*返回按键触发时调用
    * 返回true代表我已经处理返回逻辑，Activity不用自己finish
    * 返回false代表我不处理，Activity自己处理*/
    public boolean onBackPressed() {
        return false;
    }
}
