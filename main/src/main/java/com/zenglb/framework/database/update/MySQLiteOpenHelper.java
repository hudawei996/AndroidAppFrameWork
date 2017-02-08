package com.zenglb.framework.database.update;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zenglb.framework.database.daomaster.DaoMaster;
import com.zenglb.framework.database.daomaster.MessagesDao;


/**
 * 重新的考虑升级策略
 *
 * Created by zenglb on 2017/1/3.
 */
public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db,MessagesDao.class);  //升级

        if (oldVersion == newVersion) {
            Log.d("onUpgrade", "数据库是最新版本" + oldVersion + "，不需要升级");
            return;
        }
        Log.d("onUpgrade", "数据库从版本" + oldVersion + "升级到版本" + newVersion);
        switch (oldVersion) {
            case 1:

                break;
            case 2:

                break;
            default:
                break;
        }
    }
}