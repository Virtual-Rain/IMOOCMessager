package net.zhouxu.italker.italker.push.frags.search;


import net.zhouxu.italker.common.app.Fragment;
import net.zhouxu.italker.common.app.PresenterFragment;
import net.zhouxu.italker.factory.model.card.GroupCard;
import net.zhouxu.italker.factory.presenter.search.SearchContract;
import net.zhouxu.italker.factory.presenter.search.SearchGroupPresenter;
import net.zhouxu.italker.italker.push.R;
import net.zhouxu.italker.italker.push.activities.SearchActivity;

import java.util.List;

/**
 * 搜索群的界面实现
 * A simple {@link Fragment} subclass.
 */
public class SearchGroupFragment extends PresenterFragment<SearchContract.Presenter>
        implements SearchActivity.SearchFragment ,SearchContract.GroupView{


    public SearchGroupFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_group;
    }

    @Override
    public void search(String content) {
//Activity-》Fragment-》Presenter-》Net
        mPresenter.search(content);
    }



    @Override
    public void onSearchDonw(List<GroupCard> groupCards) {
        //数据成功的情况下返回数据
    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        return new SearchGroupPresenter(this);
    }
}
