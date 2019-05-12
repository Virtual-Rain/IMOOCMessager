package net.zhouxu.italker.common.app;


import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import net.zhouxu.italker.common.R;

/**
 * Created by zx on 2018/5/28.
 */

public abstract class ToolbarActivity extends Acitvity {
    protected Toolbar mToolbar;

    @Override
    protected void initWidget() {
        super.initWidget();
        initToolbar((Toolbar)findViewById(R.id.toolbar));
    }

    /*初始化toolbar*/
    public void initToolbar(Toolbar toolbar) {
        mToolbar=toolbar;
        if(toolbar!=null){
            setSupportActionBar(toolbar);
        }
        initTitleNeedBack();
    }
    protected void initTitleNeedBack(){
        //设置左上角的返回按钮为时间的返回效果
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }
}
