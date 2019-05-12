package net.zhouxu.italker.italker.push.frags.media;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import net.zhouxu.italker.common.tools.UiTool;
import net.zhouxu.italker.common.widget.GalleryView;
import net.zhouxu.italker.italker.push.R;

/**
 * 图片选择Fragment
 */
public class GalleryFragment extends BottomSheetDialogFragment
        implements GalleryView.SelectedChangeListener {

    private GalleryView mGallery;
    private OnSelectedListener mListener;

    public GalleryFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TransStatusBottomSheetDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //获取我们的GalleryView
        View root = inflater.inflate(R.layout.fragment_gallery, container);
        mGallery = (GalleryView) root.findViewById(R.id.galleryView);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGallery.setup(getLoaderManager(), this);
    }

    @Override
    public void onSelectedCountChanged(int count) {
        //如果选中了一张图片
        if (count > 0) {
            //隐藏自己
            dismiss();
            if (mListener != null) {
                //得到所有选中图片的路径
                String[] paths = mGallery.getSelectedPath();
                //返回第一张
                mListener.onSelectedImage(paths[0]);
                //取消和唤起者之间的引用，加快内存回收
                mListener = null;
            }
        }
    }

    public GalleryFragment setListener(OnSelectedListener listener) {
        mListener = listener;
        return this;
    }

    //选中图片的监听器
    public interface OnSelectedListener {
        void onSelectedImage(String path);
    }

    /*为了解决顶部状态栏颜色问题：状态栏透明*/
    public static class TransStatusBottomSheetDialog extends BottomSheetDialog {

        public TransStatusBottomSheetDialog(@NonNull Context context) {
            super(context);
        }

        public TransStatusBottomSheetDialog(@NonNull Context context, int theme) {
            super(context, theme);
        }

        protected TransStatusBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            final Window window=getWindow();
            if(window==null)
                return;
            //屏幕的高度
            int screenHeight= UiTool.getScreenHeight(getOwnerActivity());
            //得到状态栏高度
            int statusHeight=UiTool.getStatusBarHeight(getOwnerActivity());
            //计算dialog的高度
            int dialogHeight=screenHeight-statusHeight;
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    dialogHeight<=0?ViewGroup.LayoutParams.MATCH_PARENT:dialogHeight);
        }
    }
}