package net.zhouxu.italker.italker.push;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;

import net.qiujuer.genius.res.Resource;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.zhouxu.italker.common.app.Acitvity;
import net.zhouxu.italker.factory.persistence.Account;
import net.zhouxu.italker.italker.push.activities.AccountActivity;
import net.zhouxu.italker.italker.push.activities.MainActivity;
import net.zhouxu.italker.italker.push.frags.assist.PermissionsFragment;

public class LaunchActivity extends Acitvity {

    //Drawable
    private ColorDrawable mBgDrawable;


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        //拿到根布局
        View root=findViewById(R.id.activity_launch);
        //获取颜色
        int color= UiCompat.getColor(getResources(),R.color.colorPrimary);
        //传教一个Drawable
        ColorDrawable drawable =new ColorDrawable(color);
        //设置给背景
        root.setBackground(drawable);
        mBgDrawable=drawable;
    }

    @Override
    protected void initData() {
        super.initData();

        //动物进入到50%等等PushId获取到
        startAnim(0.5f, new Runnable() {
            @Override
            public void run() {
                waitPushReceiverId();
            }
        });
    }



    /*等待个推框架对我们的PushId设置好值*/
    private void waitPushReceiverId(){

        if(Account.isLogin()){
            //已登录,判断是否已经绑定
            //如果没有进行绑定则等待广播接收器绑定
            if(Account.isBind()){
                skip();
                return;
            }
        }else {
            //没有登录
            //如果拿到了PushId,没有登录不能绑定PushId
            if(!TextUtils.isEmpty(Account.getPushId())){
                //跳转
                skip();
                return;
            }
        }

        //循环等待
        getWindow().getDecorView()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        waitPushReceiverId();
                    }
                },500);
    }
    /*在跳转之前需要把剩下的50%进行完成*/
    private void skip(){
        startAnim(1f, new Runnable() {
            @Override
            public void run() {
                reallySkip();
            }
        });


    }

    /*真实的跳转*/
    private void reallySkip(){
        if(PermissionsFragment.haveAll(this,getSupportFragmentManager())){
            //检查跳转到主界面还是登录
            if(Account.isLogin()){
                //进入MainActivity
                MainActivity.show(this);
            }else {
                AccountActivity.show(this);
            }
            //结束当前活动
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //权限判断
//        if(PermissionsFragment.haveAll(this,getSupportFragmentManager())){
//            MainActivity.show(this);
//            finish();
//        }
    }
    /*给背景设置一个动画
    * 动画结束时触发 endCallback*/
    private void startAnim(float endProgress,final Runnable endCallback){
        //获取一个最终的颜色
        int finalColor= Resource.Color.WHITE;//UiCompat.getColor(getResources(),R.color.white)
        //运算当前进度的颜色
        ArgbEvaluator evaluator=new ArgbEvaluator();
        int endColor=(int)evaluator.evaluate(endProgress,mBgDrawable.getColor(),finalColor);
        //构建一个属性动画
        ValueAnimator valueAnimator= ObjectAnimator.ofObject(this,property,evaluator,endColor );
        valueAnimator.setDuration(1500);//时间
        valueAnimator.setIntValues(mBgDrawable.getColor(),endColor);//开始&结束值
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //结束时触发
                endCallback.run();
            }
        });
        valueAnimator.start();
    }

    private final android.util.Property<LaunchActivity,Object>property=new android.util.Property<LaunchActivity, Object>(Object.class,"color"){

        @Override
        public void set(LaunchActivity object, Object value) {
            object.mBgDrawable.setColor((Integer) value);
        }

        @Override
        public Object get(LaunchActivity object) {
            return mBgDrawable.getColor();
        }
    };
}

