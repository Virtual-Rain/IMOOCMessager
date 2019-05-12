package net.zhouxu.italker.factory.presenter.search;

import net.zhouxu.italker.factory.model.card.GroupCard;
import net.zhouxu.italker.factory.model.card.UserCard;
import net.zhouxu.italker.factory.presenter.BaseContract;

import java.util.List;

/**
 * Created by zx on 2018/5/30.
 */

public interface SearchContract {
    interface Presenter extends BaseContract.Presenter {
        //搜索内容
        void search(String content);
    }

    //搜索人的界面
    interface UserView extends BaseContract.View<Presenter> {
        void onSearchDonw(List<UserCard> userCards);
    }

    //搜索群的界面
    interface GroupView extends BaseContract.View<Presenter> {
        void onSearchDonw(List<GroupCard> groupCards);
    }
}
