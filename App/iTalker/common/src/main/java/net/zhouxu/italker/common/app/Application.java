package net.zhouxu.italker.common.app;

import android.os.SystemClock;
import android.support.annotation.StringRes;
import android.widget.Toast;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.io.File;

/**
 * Created by zx on 2018/4/27.
 */

public class Application extends android.app.Application {
    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    //外部获取单例子
    public static Application getInstance(){return instance;}

    /*获取缓存头像临时存储文件夹地址
    * @return 当前App的缓存文件夹地址*/
    public static File getCacheDirFile() {
        return instance.getCacheDir();
    }

    //头像的临时存储文件地址
    public static File getPortraitTmpFile() {
        //得到头像目录的缓存地址
        File dir = new File(getCacheDirFile(), "portrait");
        //创建所有对应的文件夹
        dir.mkdirs();
        //删除一些旧的缓存文件
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                file.delete();
            }
        }
        //返回一个当前时间戳的目录文件夹地址
        File path = new File(dir, SystemClock.uptimeMillis() + ".jpg");
        return path.getAbsoluteFile();
    }

   /* 得到声音文件的本地地址
   * @param isTmp 是否是缓存文件，True，每次返回的文件地址一样*/
   public static File getAudioTmpFile(boolean isTmp){
       File dir=new File(getCacheDirFile(),"audio");
       dir.mkdirs();
       File[]files=dir.listFiles();
       if(files!=null&&files.length>0){
           for(File file :files){
               file.delete();
           }
       }
       File path=new File(getCacheDirFile(),isTmp?"tmp.mp3":SystemClock.uptimeMillis()+".mp3");
       return path.getAbsoluteFile();
   }

   /*显示一个Toast
   * @param msg 字符串*/
   public static void showToast(final String msg){
       //Toast只能在主线程中显示，所以需要进行线程转换，保证在主线程进行show操作
       //Toast.makeText(instance,msg,Toast.LENGTH_SHORT).show();
       Run.onUiAsync(new Action(){
           @Override
           public void call(){
               //回调进入主线程+
               Toast.makeText(instance,msg,Toast.LENGTH_SHORT).show();
           }
       });
   }
    /*显示一个Toast
       * @param msgId 字符串的资源*/
   public static void showToast(@StringRes int msgId){showToast(instance.getString(msgId));}
}
