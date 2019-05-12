package net.zhouxu.italker.italker.push.activities;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import net.zhouxu.italker.common.app.Fragment;
import net.zhouxu.italker.common.app.ToolbarActivity;
import net.zhouxu.italker.italker.push.R;
import net.zhouxu.italker.italker.push.frags.search.SearchGroupFragment;
import net.zhouxu.italker.italker.push.frags.search.SearchUserFragment;

public class SearchActivity extends ToolbarActivity {

    public static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final int TYPE_USER = 1;//搜索人
    public static final int TYPE_GROUP = 2;//搜索群

    //搜索的类型
    private int type;
    private SearchFragment mSearchFragment;

    /*搜索界面，显示的类型用户或者群*/
    public static void show(Context context, int type) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        type = bundle.getInt(EXTRA_TYPE);
        return type == TYPE_USER || type == TYPE_GROUP;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        //显示对应的Fragment
        Fragment fragment;
        if(type==TYPE_USER){
            SearchUserFragment userFragment=new SearchUserFragment();
            fragment=userFragment;
            mSearchFragment=userFragment;
        }else{
            SearchGroupFragment groupFragment=new SearchGroupFragment();
            fragment=groupFragment;
            mSearchFragment=groupFragment;
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.lay_container,fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //初始化菜单
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        //找到搜索菜单
        MenuItem searchItme = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItme.getActionView();
        if (searchView != null) {
            //拿到一个搜索管理器
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            //添加搜索监听
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    //点击提交按钮
                    search(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    //文字改变的时候，不会及时搜索，只在为null的情况下进行搜索
                    if (TextUtils.isEmpty(newText)) {
                        search("");
                        return true;
                    }
                    return false;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    /*搜索的发起点*/
    private void search(String query) {
        if (mSearchFragment == null)
            return;
        mSearchFragment.search(query);

    }

    /*搜索的Fragment必须继承的接口*/
    public interface SearchFragment {
        void search(String content);
    }
}
