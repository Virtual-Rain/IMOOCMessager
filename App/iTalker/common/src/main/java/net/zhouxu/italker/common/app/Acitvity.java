package net.zhouxu.italker.common.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import butterknife.ButterKnife;
/**
 * Created by zx on 2018/4/19.
 */

public abstract class Acitvity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindows();
        if (initArgs(getIntent().getExtras())) {
            //得到界面Id 并设置到界面中
            int layId=getContentLayoutId();
            setContentView(layId);
            initWidget();
            initData();
        } else {
            finish();
        }

    }

    //初始化窗口
    protected void initWindows() {

    }

    //初始化相关参数
    protected boolean initArgs(Bundle bundle) {
        return true;
    }

    //得到当前界面资源文件Id
    protected abstract int getContentLayoutId();

    //初始化控件
    protected void initWidget() {
        //绑定布局
        ButterKnife.bind(this);
    }

    //初始化数据
    protected void initData() {

    }

    @Override
    public boolean onSupportNavigateUp() {
        //点击界面导航返回，Finish当前界面
        finish();
        return super.onSupportNavigateUp();
    }

    //返回按键触发调用
    @Override
    public void onBackPressed() {
        //手机back键
        @SuppressLint("RestrictedApi")
        List<android.support.v4.app.Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (android.support.v4.app.Fragment fragment : fragments) {
                //判断是否为我们能够处理的Fragment类型
                if (fragment instanceof net.zhouxu.italker.common.app.Fragment) {
                    //判断是否拦截了返回按钮
                    if (((net.zhouxu.italker.common.app.Fragment) fragment).onBackPressed()) {
                        //如果有直接return
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
        finish();
    }
}
