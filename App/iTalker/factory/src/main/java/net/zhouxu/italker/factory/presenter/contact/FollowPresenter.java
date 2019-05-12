package net.zhouxu.italker.factory.presenter.contact;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.zhouxu.italker.factory.data.DataSource;
import net.zhouxu.italker.factory.data.helper.UserHelper;
import net.zhouxu.italker.factory.model.card.UserCard;
import net.zhouxu.italker.factory.presenter.BasePresenter;

/**
 * 关注的逻辑实现
 * Created by zx on 2018/5/31.
 */

public class FollowPresenter extends BasePresenter<FollowContract.View>
implements FollowContract.Presenter,DataSource.Callback<UserCard>{
    public FollowPresenter(FollowContract.View view) {
        super(view);
    }

    @Override
    public void follow(String id) {
        start();
        UserHelper.follow(id,this);
    }

    @Override
    public void onDataLoaded(final UserCard userCard) {
        //关注成功
        final FollowContract.View view=getView();
        if(view!=null){
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onFollowSucceed(userCard);
                }
            });
        }
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final FollowContract.View view=getView();
        if(view!=null){
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.showError(strRes);
                }
            });
        }
    }
}
