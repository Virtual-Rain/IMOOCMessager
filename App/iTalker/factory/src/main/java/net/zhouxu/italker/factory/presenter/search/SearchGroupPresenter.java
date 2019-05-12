package net.zhouxu.italker.factory.presenter.search;

import net.zhouxu.italker.factory.presenter.BasePresenter;

/**
 * 搜索群的逻辑实现
 * Created by zx on 2018/5/30.
 */

public class SearchGroupPresenter extends BasePresenter<SearchContract.GroupView>
implements SearchContract.Presenter{
    public SearchGroupPresenter(SearchContract.GroupView view) {
        super(view);
    }

    @Override
    public void search(String content) {

    }
}
