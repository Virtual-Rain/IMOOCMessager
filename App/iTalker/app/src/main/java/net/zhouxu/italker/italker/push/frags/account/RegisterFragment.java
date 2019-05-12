package net.zhouxu.italker.italker.push.frags.account;


import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

import net.qiujuer.genius.ui.widget.Loading;
import net.zhouxu.italker.common.app.Fragment;
import net.zhouxu.italker.common.app.PresenterFragment;
import net.zhouxu.italker.factory.presenter.account.RegisterContract;
import net.zhouxu.italker.factory.presenter.account.RegisterPresenter;
import net.zhouxu.italker.italker.push.R;
import net.zhouxu.italker.italker.push.activities.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends PresenterFragment<RegisterContract.Presenter>
        implements RegisterContract.View{

    private AccountTrigger mAccountTrigger;
    @BindView(R.id.edit_phone)
    EditText mPhone;
    @BindView(R.id.edit_name)
    EditText mName;
    @BindView(R.id.edit_password)
    EditText mPassword;
    @BindView(R.id.loading)
    Loading mLoading;
    @BindView(R.id.btn_submit)
    Button mSubmit;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //得到我们的Activity的引用
        mAccountTrigger = (AccountTrigger) context;
    }

    @Override
    protected RegisterContract.Presenter initPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_register;
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick(){
        String phone=mPhone.getText().toString();
        String name=mName.getText().toString();
        String password=mPassword.getText().toString();
        //调用P层进行注册
        mPresenter.register(phone,name,password);
    }

    @OnClick(R.id.txt_go_login)
    void onShowLoginClick(){
        //让AccountActivity进行界面切换
        mAccountTrigger.triggerView();
    }

    @Override
    public void showError(int str) {
        super.showError(str);
        //当提示需要显示错误的时候触发,一定是结束了
        //停止Loading
        mLoading.stop();
        //让控件可以输入
        mPhone.setEnabled(true);
        mName.setEnabled(true);
        mPassword.setEnabled(true);
        //提交按钮可以继续点击
        mSubmit.setEnabled(true);
    }

    @Override
    public void showLoading() {
        super.showLoading();
        //正在进行，界面不可操作
        //开始Loading
        mLoading.start();
        //让控件不可以输入
        mPhone.setEnabled(false);
        mName.setEnabled(false);
        mPassword.setEnabled(false);
        //提交按钮可以继续点击
        mSubmit.setEnabled(false);
    }

    @Override
    public void registerSuccess() {
        //注册成功，即登录成功，进行跳转到MainActivity
        MainActivity.show(getContext());
        //关闭当前页面
        getActivity().finish();
    }
}
