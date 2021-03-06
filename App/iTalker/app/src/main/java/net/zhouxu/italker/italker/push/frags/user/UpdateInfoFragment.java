package net.zhouxu.italker.italker.push.frags.user;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;

import net.zhouxu.italker.common.app.Application;
import net.zhouxu.italker.common.app.Fragment;
import net.zhouxu.italker.common.widget.PortraitView;
import net.zhouxu.italker.factory.Factory;
import net.zhouxu.italker.factory.net.UploadHelper;
import net.zhouxu.italker.italker.push.R;
import net.zhouxu.italker.italker.push.frags.media.GalleryFragment;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 更新用户信息的界面
 */
public class UpdateInfoFragment extends Fragment {

    @BindView(R.id.im_portrait)
    PortraitView mPortrait;

    public UpdateInfoFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_info;
    }

    @OnClick(R.id.im_portrait)
    void onPortraitClick() {
        new GalleryFragment()
                .setListener(new GalleryFragment.OnSelectedListener() {
                    @Override
                    public void onSelectedImage(String path) {
                        UCrop.Options options = new UCrop.Options();
                        //设置图片处理的格式JPEG
                        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                        //设置压缩后的图片精度
                        options.setCompressionQuality(96);
                        //头像的缓存地址
                        File dPath = Application.getPortraitTmpFile();
                        //发起剪切
                        UCrop.of(Uri.fromFile(new File(path)), Uri.fromFile(dPath))
                                .withAspectRatio(1, 1)   //比例1：1
                                .withMaxResultSize(520, 520) //返回最大的尺寸
                                .withOptions(options) //相关参数
                                .start(getActivity());
                    }
                })
                //show的时候建议使用getChildFragmentManager
                .show(getChildFragmentManager(), GalleryFragment.class.getName());
    }

    //图片剪切成功的回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //收到从Activity传递过来的回调，然后取出其中的值进行图片加载
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            //通过UCrop得到对应的Uri
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                loadPortrait(resultUri);
            } else if (requestCode == UCrop.RESULT_ERROR) {
                final Throwable cropError = UCrop.getError(data);
            }
        }
    }

    //加载图片到mPortrait
    private void loadPortrait(Uri uri) {
        Glide.with(this)
                .load(uri)
                .asBitmap()
                .centerCrop()
                .into(mPortrait);

        //拿到本地文件的地址
        final String localPath = uri.getPath();
        Log.e("TAG", "localpath:" + localPath);

        //异步调用上传图片
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                String url= UploadHelper.uploadPortrait(localPath);
                Log.e("TAG","url"+url);
            }
        });
    }
}
