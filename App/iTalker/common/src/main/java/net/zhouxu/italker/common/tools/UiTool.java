package net.zhouxu.italker.common.tools;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Window;

/**
 * Created by zx on 2018/4/27.
 */

public class UiTool {
    private static int STATUS_BAR_HEIGHT = -1;

    //得到状态栏的高度
    public static int getStatusBarHeight(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && STATUS_BAR_HEIGHT == -1) {
            try {
                final Resources res = activity.getResources();
                //尝试获取status_bar_height这个属性的Id对应的资源int值
                int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId <= 0) {
                    Class<?> clazz = Class.forName("com.android.internal.R$dimen");
                    Object object = clazz.newInstance();
                    resourceId = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
                    if (resourceId > 0)
                        STATUS_BAR_HEIGHT = res.getDimensionPixelSize(resourceId);
                    if (STATUS_BAR_HEIGHT <= 0) {
                       //通过window 拿取
                        Rect rectangle = new Rect();
                        Window window = activity.getWindow();
                        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
                        STATUS_BAR_HEIGHT = rectangle.top;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return STATUS_BAR_HEIGHT;
    }
    //得到屏幕度
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Activity activity){
        DisplayMetrics displayMetrics=activity.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }
}
