package net.zhouxu.italker.factory.net;

import android.text.format.DateFormat;
import android.util.Log;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import net.zhouxu.italker.factory.Factory;
import net.zhouxu.italker.utils.HashUtil;

import java.io.File;
import java.util.Date;


/**
 * 上传工具类，上传任意文件到阿里OSS存储
 */

public class UploadHelper {

    public static final String TAG=UploadHelper.class.getSimpleName();
    // /终结点与OSS存储区域有关
    private static final String ENDPOINT = "http://oss-cn-beijing.aliyuncs.com";
    private static final String BUCKET_NAME = "italk-zx";

    private static OSS getClient() {
        // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的`访问控制`章节
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(
                "LTAI2ClGgHWQeeQ9", "Q8loDNW1YNtUSd7xK22VZdCPMWR6g0");
        return new OSSClient(Factory.app(), ENDPOINT, credentialProvider);
    }

    /*上传最终方法，成功返回一个路径
    * @param objKey 上传到服务器后独立的Key md5加密
    * @parm path 文件路径*/
    private static String upload(String objKey, String path) {
        // 构造上传请求
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME,
                objKey, path);
        try{
            //初始化上传的Client
            OSS client=getClient();
            //开始同步上传
            PutObjectResult result = client.putObject(request);
            //得到一个外网可访问的地址
            String url=client.presignPublicObjectURL(BUCKET_NAME,objKey);
            //格式打印输出
            Log.d(TAG,String.format("PublicObjectURL:%s",url));
            return url;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }



    /*上传普通图片*/
    public static String uploadImage(String path){
        String key=getImageObjectkey(path);
        return upload(key,path);
    }
    /*上传头像*/
    public static String uploadPortrait(String path){
        String key=getPortraitObjectkey(path);
        return upload(key,path);
    }
    /*上传普通图片*/
    public static String uploadAudio(String path){
        String key=getAudioObjectkey(path);
        return upload(key,path);
    }

    /*分月存储*/
    public static String getDateString(){
        return DateFormat.format("yyyyMM", new Date()).toString();
    }

    /*文件路径：image/08/04/sdliaodjflakdeioj.jpg*/
    private static String getImageObjectkey(String path){
        String fileMd5= HashUtil.getMD5String(new File(path));
        String dateString=getDateString();
        return String.format("image/%s/%s.jpg",dateString,fileMd5);
    }
    /*文件路径：image/08/04/sdliaodjflakdeioj.jpg*/
    private static String getPortraitObjectkey(String path){
        String fileMd5= HashUtil.getMD5String(new File(path));
        String dateString=getDateString();
        return String.format("portrait/%s/%s.jpg",dateString,fileMd5);
    }
    /*文件路径：image/08/04/sdliaodjflakdeioj.jpg*/
    private static String getAudioObjectkey(String path){
        String fileMd5= HashUtil.getMD5String(new File(path));
        String dateString=getDateString();
        return String.format("audio/%s/%s.jpg",dateString,fileMd5);
    }
}
