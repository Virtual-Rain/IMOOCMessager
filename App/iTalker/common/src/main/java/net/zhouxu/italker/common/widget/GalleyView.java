package net.zhouxu.italker.common.widget;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.zhouxu.italker.common.R;
import net.zhouxu.italker.common.widget.recycler.RecyclerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class GalleyView extends RecyclerView {

    private static final int LOADER_ID=0x0100;
    private static final int MAX_IMAGE_COUNT=3; //最大的选中图片数量
    private static final int MIM_IMAGE_FILE_SIZE=10*1024; //最小的图片大小
    private LoaderCallback mLoaderCallback=new LoaderCallback();
    private Adapter mAdapter = new Adapter();
    private List<Image> mSelectedImages=new LinkedList<>();
    private SelectedChangeListener mListener;

    public GalleyView(Context context) {
        super(context);
        init();
    }

    public GalleyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GalleyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayoutManager(new GridLayoutManager(getContext(), 4));
        setAdapter(mAdapter);
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Image>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Image image) {
                //Cell点击操作，如果我们的点击是允许的，那么更新对应的Cell的状态，刷新界面
                //如果不允许点击（已达到最大的选中数量），不刷新界面
                if(onItemSelectClick(image)) {
                    holder.updateData(image);
                }
            }
        });
    }
    /*初始化方法 返回一个LOADER_ID，可用于销毁Loader*/
    public int setup(LoaderManager loaderManager,SelectedChangeListener listener){
        mListener=listener;
        loaderManager.initLoader(LOADER_ID,null,mLoaderCallback);
        return LOADER_ID;
    }

    /*Cell点击的具体逻辑：true 数据更改需要刷新；false 不需要刷新*/
    private boolean onItemSelectClick(Image image){
        //是否需要进行刷新
        boolean notifyRefreash;
        if(mSelectedImages.contains(image)){
            mSelectedImages.remove(image);
            image.isSelect=false;
            notifyRefreash=true;
        }else{
            if(mSelectedImages.size()>=MAX_IMAGE_COUNT){
                notifyRefreash=false;
            }else {
                mSelectedImages.add(image);
                image.isSelect=true;
                notifyRefreash=true;
            }
        }
        //如果数据有更改，我们需要通知外面的监听者
        if(notifyRefreash)
            notifySelectChanged();
        return true;
    }
    /*得到选中的图片的全部地址*/
    public String[]getSelectedPath(){
        String[]paths=new String[mSelectedImages.size()];
        int index=0;
        for(Image image:mSelectedImages){
            paths[index++]=image.path;
        }
        return paths;
    }
    /*清空图片*/
    private void clear(){
        for(Image image:mSelectedImages){
            //先重置状态
            image.isSelect=false;
        }
        mSelectedImages.clear();
        //通知更新
        mAdapter.notifyDataSetChanged();
    }
    /*通知选中图片状态改变*/
    private void notifySelectChanged(){
        //得到监听者，并判断是否有数量变化，回调
        SelectedChangeListener listener=mListener;
        if(listener!=null){
            listener.onSelectedCountChanged(mSelectedImages.size());
        }
    }
    /*通知Adapter数据更改的方法*/
    private void updateSource(List<Image>images){
        mAdapter.replace(images);
    }
    /*实际加载LoaderCallback数据*/
    private class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor>{
        private final  String[]IMAGE_PROJECTION=new String[]{
                MediaStore.Images.Media._ID,    //id
                MediaStore.Images.Media.DATA,   //图片路径
                MediaStore.Images.Media.DATE_ADDED//图片创建时间
        };
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            //创建一个Loader
            if(id==LOADER_ID){
                return new CursorLoader(getContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION,
                        null,
                        null,
                        IMAGE_PROJECTION[2]+"DESC");
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            //当Loader加载完成时
            List<Image>images=new ArrayList<>();
            if(data!=null){
                int count=data.getCount();
                if(count>0){
                    //移动游标到顶部
                    data.moveToFirst();
                    int indexId=data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]);
                    int indexPath=data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]);
                    int indexDate=data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]);
                    do{
                        //循环读取，直到没有下一条数据
                        int id=data.getInt(indexId);
                        String path=data.getString(indexPath);
                        long dateTime=data.getLong(indexDate);

                        File file=new File(path);
                        if(!file.exists()||file.length()<MIM_IMAGE_FILE_SIZE){
                            continue;
                        }
                        Image image=new Image();
                        image.id=id;
                        image.path=path;
                        image.date=dateTime;
                        images.add(image);
                    }while (data.moveToNext());
                }
            }
            updateSource(images);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            //当销毁或者重置了 界面清空
            updateSource(null);
        }
    }
    /*内部的数据结构*/
    private static class Image {
        int id;
        String path;
        long date;
        boolean isSelect;

        @Override
        public int hashCode() {
            return path != null ? path.hashCode() : 0;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Image image = (Image) obj;
            return path != null ? path.equals(image.path) : image.path == null;
        }
    }
    /*适配器*/
    private class Adapter extends RecyclerAdapter<Image> {

        @Override
        protected int getItemViewType(int position, Image image) {
            return R.layout.cell_galley;
        }

        @Override
        protected ViewHolder<Image> onCreateViewHolder(View root, int viewType) {
            return new GalleyView.ViewHolder(root);
        }
    }
    /*Cell对应的*/
    private class ViewHolder extends RecyclerAdapter.ViewHolder<Image> {
        private ImageView mPic;
        private View mShade;
        private CheckBox mSelected;

        public ViewHolder(View itemView) {
            super(itemView);

            mPic=(ImageView)itemView.findViewById(R.id.im_image);
            mShade=itemView.findViewById(R.id.view_shade);
            mSelected=(CheckBox)itemView.findViewById(R.id.cb_select);
        }

        @Override
        protected void onBind(Image image) {
            Glide.with(getContext())
                    .load(image.path)
                    .diskCacheStrategy(DiskCacheStrategy.NONE) //不使用缓存，直接从原图加载
                    .centerCrop()
                    .placeholder(R.color.grey_200)
                    .into(mPic);
            mShade.setVisibility(image.isSelect?VISIBLE:INVISIBLE);
            mSelected.setChecked(image.isSelect);

        }
    }
    /*对外的监听器*/
    public interface SelectedChangeListener{
        void onSelectedCountChanged(int count);
    }
}
