package net.zhouxu.italker.factory.presenter;

/**
 * Created by zx on 2018/5/3.
 */

public class BasePresenter<T extends BaseContract.View>
        implements BaseContract.Presenter {

    private T mView;

    public BasePresenter(T view) {
        setView(view);
    }

    /*子类可复写，设置View*/
    @SuppressWarnings("unchecked")
    protected void setView(T view) {
        this.mView = view;
        this.mView.setPresenter(this);
    }

    /*子类得到View*/
    protected final T getView() {
        return mView;
    }

    @Override
    public void start() {
        //开始的时候进行Loading调用
        T view = mView;
        if (view != null) {
            view.showLoading();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void destroy() {
        T view = mView;
        mView=null;
        if (view != null) {
            view.setPresenter(null);
        }
    }
}
