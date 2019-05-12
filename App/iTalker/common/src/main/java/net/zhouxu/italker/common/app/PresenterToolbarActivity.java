package net.zhouxu.italker.common.app;


import net.zhouxu.italker.factory.presenter.BaseContract;

/**
 * Created by zx on 2018/5/28.
 */

public abstract class PresenterToolbarActivity<Presenter extends BaseContract.Presenter>
        extends ToolbarActivity implements BaseContract.View<Presenter> {

    protected Presenter mPresenter;

    @Override
    protected void initBefore() {
        super.initBefore();
        //初始化Presenter
        initPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //界面关闭时进行销毁的操作
        if(mPresenter!=null){
            mPresenter.destroy();
        }
    }

    /*初始化Presenter*/
    protected abstract Presenter initPresenter();

    @Override
    public void showError(int str) {
        if(mPlaceHolderView!=null){
            mPlaceHolderView.triggerError(str);
        }else{
            Application.showToast(str);
        }
    }

    @Override
    public void showLoading() {
        if(mPlaceHolderView!=null){
            mPlaceHolderView.triggerLoading();
        }
    }

   protected void hideLoading(){
        if(mPlaceHolderView!=null){
            mPlaceHolderView.triggerOk();
        }
   }

    @Override
    public void setPresenter(Presenter presenter) {
        //View中赋值Presenter
        mPresenter=presenter;
    }
}
