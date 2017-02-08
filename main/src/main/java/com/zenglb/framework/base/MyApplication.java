package com.zenglb.framework.base;

import android.text.TextUtils;
import android.util.Log;

import com.zenglb.commonlib.sharedpreferences.SharedPreferencesDao;
import com.zenglb.framework.database.daomaster.DaoMaster;
import com.zenglb.commonlib.base.BaseApplication;
import com.zenglb.framework.database.daomaster.DaoSession;
import com.zenglb.framework.database.update.MySQLiteOpenHelper;
import org.greenrobot.greendao.database.Database;

/**
 *
 */
public class MyApplication extends BaseApplication {
	public static final String TAG = MyApplication.class.getSimpleName();

	public static final boolean ENCRYPTED = false;
	private DaoSession daoSession;

	@Override
	public void onCreate() {
		super.onCreate();
		String processName = getProcessName();
		Log.d(TAG, processName + "Application onCreate");
		if (!TextUtils.isEmpty(processName) && processName.equals(this.getPackageName())) { //main Process
			setDaoSession(SharedPreferencesDao.getInstance().getData("Account","DefDb",String.class));
		}
	}


	@Override
	public void onTerminate() {
		super.onTerminate();
	}


	/**
	 * 设置数据库操作对象
	 * 1.在Application 中设置一个默认的上传登陆的Session,在登录成功后创建一个新的Session
	 *
	 */
	public void setDaoSession(String account){
		if (!TextUtils.isEmpty(account)) {
			String DBName = ENCRYPTED ? account + "encrypted" : account;
			MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this, DBName, null);
			Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
			daoSession = new DaoMaster(db).newSession();
		} else {
			Log.w(TAG, "Account is empty,init db failed");
		}
	}

	/**
	 *获取数据库操作对象
	 * @return
	 */
	public DaoSession getDaoSession() {
		return daoSession;
	}



}
