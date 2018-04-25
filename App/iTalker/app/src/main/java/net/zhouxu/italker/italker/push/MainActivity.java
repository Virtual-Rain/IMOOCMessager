package net.zhouxu.italker.italker.push;


import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;
import net.zhouxu.italker.common.app.Acitvity;
import net.zhouxu.italker.common.widget.PortraitView;
import net.zhouxu.italker.italker.push.frags.main.ActiveFragment;
import net.zhouxu.italker.italker.push.frags.main.ContactFragment;
import net.zhouxu.italker.italker.push.frags.main.GroupFragment;
import net.zhouxu.italker.italker.push.helper.NavHelper;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends Acitvity implements BottomNavigationView.OnNavigationItemSelectedListener,
NavHelper.OnTabChangedListener<Integer>{

    @BindView(R.id.appbar)
    View mLayAppbar;
    @BindView(R.id.im_portrait)
    PortraitView mPortrait;
    @BindView(R.id.txt_title)
    TextView mTitle;
    @BindView(R.id.lay_container)
    FrameLayout mContainer;
    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;
    @BindView(R.id.btn_action)
    FloatActionButton mAction;

    private NavHelper<Integer> mNavHelper;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initWidget() {
        super.initWidget();
        //初始化工具类
        mNavHelper=new NavHelper<>(this, getSupportFragmentManager(),
                R.id.lay_container,this);
        mNavHelper.add(R.id.action_home,new NavHelper.Tab<Integer>(ActiveFragment.class,R.string.title_home))
        .add(R.id.action_group, new NavHelper.Tab<Integer>(GroupFragment.class,R.string.title_group))
        .add(R.id.action_contact,new NavHelper.Tab<Integer>(ContactFragment.class,R.string.title_contact));

        //设置底部导航的监听
        mNavigation.setOnNavigationItemSelectedListener(this);

        Glide.with(this).load(R.drawable.bg_src_morning).centerCrop()
                .into(new ViewTarget<View,GlideDrawable>(mLayAppbar) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                    }
                });
    }

    @Override
    protected void initData() {
        super.initData();
        //从底部导航中接管我们的Menu，然后手动触发第一次点击
        Menu menu=mNavigation.getMenu();
        //触发首次选中Home
        menu.performIdentifierAction(R.id.action_home,0);

    }
    @OnClick(R.id.im_search)
    void onSerarchMenuClick(){

    }
    @OnClick(R.id.btn_action)

    void onAcitonClick(){

    }

    //底部导航被点击的时候触发
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        //转接事件流到工具类
        return mNavHelper.performClickMenu(item.getItemId());
    }
    //NavHelper处理后回调的方法
    @Override
    public void onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
        //从额外字段中取出我们的Title资源Id
        mTitle.setText(newTab.extra);

        //添加动画:对浮动按钮的显示与隐藏
        float transY=0;
        float rotation=0;
        if(Objects.equals(newTab.extra,R.string.title_home)){
            transY= Ui.dipToPx(getResources(),76);
        }else{
            //transY=0显示
            if(Objects.equals(newTab.extra,R.string.title_group)){
                //群
                mAction.setImageResource(R.drawable.ic_group_add);
                rotation=-360;
            }else {
                //个人
                mAction.setImageResource(R.drawable.ic_contact_add);
                rotation=360;
            }
        }
        mAction.animate()
                .rotation(rotation) //旋转
                .translationY(transY)   //Y轴
                .setInterpolator(new AnticipateOvershootInterpolator(1))    //弹性差值器
                .setDuration(480)   //时间
                .start();
    }
}
