package net.zhouxu.italker.common.app;

import android.content.Context;

import net.zhouxu.italker.factory.presenter.BaseContract;

/**
 * Created by zx on 2018/5/3.
 */

public abstract class PresenterFragment<Presenter extends BaseContract.Presenter> extends Fragment
        implements BaseContract.View<Presenter> {

    protected Presenter mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //界面onAttach之后触发初始化
        initPresenter();
    }

    /*初始化Presenter*/
    protected abstract Presenter initPresenter();

    @Override
    public void showError(int str) {
        if(mPlaceHolderView!=null){
            mPlaceHolderView.triggerError(str);
        }else{
            //显示错误
            Application.showToast(str);
        }
    }

    @Override
    public void showLoading() {
        if(mPlaceHolderView!=null){
            mPlaceHolderView.triggerLoading();
        }
    }

    @Override
    public void setPresenter(Presenter presenter) {
        //View中赋值Presenter
        mPresenter = presenter;
    }
}
