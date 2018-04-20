package net.zhouxu.italker.italker.push;


import android.os.Bundle;
import android.widget.TextView;

import net.zhouxu.italker.common.app.Acitvity;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends Acitvity {
    @BindView(R.id.txt_test)
    TextView mTestText;


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget(){
        super.initWidget();
        mTestText.setText("Hello Wrold");
    }
}
