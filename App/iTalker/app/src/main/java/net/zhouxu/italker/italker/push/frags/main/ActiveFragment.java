package net.zhouxu.italker.italker.push.frags.main;


import net.zhouxu.italker.common.app.Fragment;
import net.zhouxu.italker.common.widget.GalleyView;
import net.zhouxu.italker.italker.push.R;

import butterknife.BindView;

public class ActiveFragment extends Fragment {
    @BindView(R.id.galleyView)
    GalleyView mGalley;

    public ActiveFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
       return R.layout.fragment_active;
    }

    @Override
    protected void initData() {
        super.initData();
        mGalley.setup(getLoaderManager(), new GalleyView.SelectedChangeListener() {
            @Override
            public void onSelectedCountChanged(int count) {

            }
        });
    }
}
