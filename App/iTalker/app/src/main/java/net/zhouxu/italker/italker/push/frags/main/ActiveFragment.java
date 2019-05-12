package net.zhouxu.italker.italker.push.frags.main;


import net.zhouxu.italker.common.app.Fragment;
import net.zhouxu.italker.italker.push.R;

public class ActiveFragment extends Fragment {
//    @BindView(R.id.galleryView)
//    GalleryView mGallery;

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
//        mGallery.setup(getLoaderManager(), new GalleryView.SelectedChangeListener() {
//            @Override
//            public void onSelectedCountChanged(int count) {
//
//            }
//        });
    }
}
