package net.zhouxu.italker.factory.model.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * 数据库的基本信息
 * Created by zx on 2018/5/11.
 */
@Database(name = AppDatabase.NAME,version = AppDatabase.VISION)
public class AppDatabase {

    public  static  final String NAME="AppDatabase";
    public  static  final int VISION=1;
}
