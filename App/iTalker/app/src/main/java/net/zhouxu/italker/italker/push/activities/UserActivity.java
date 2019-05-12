package net.zhouxu.italker.italker.push.activities;

import android.content.Intent;

import net.zhouxu.italker.common.app.Acitvity;
import net.zhouxu.italker.common.app.Fragment;
import net.zhouxu.italker.italker.push.R;
import net.zhouxu.italker.italker.push.frags.user.UpdateInfoFragment;

public class UserActivity extends Acitvity {
    private Fragment mCurFragment;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mCurFragment = new UpdateInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container, mCurFragment)
                .commit();
    }

    //图片剪切成功的回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCurFragment.onActivityResult(requestCode, resultCode, data);
    }

}
