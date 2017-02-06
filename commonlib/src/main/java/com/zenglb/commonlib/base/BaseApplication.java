package com.zenglb.commonlib.base;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.zenglb.commonlib.sharedpreferences.SharedPreferencesDao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * BaseApplication，初始化必然初始化的一些配置
 */
public class BaseApplication extends Application {
	public static final String TAG = BaseApplication.class.getSimpleName();
//	public String processName = getProcessName();



	@Override
	public void onCreate() {
		super.onCreate();
		String processName = getProcessName();
		Log.d(TAG, processName + "Application onCreate");
		if (!TextUtils.isEmpty(processName) && processName.equals(this.getPackageName())) { //main Process
			SharedPreferencesDao.initSharePrefenceDao(this);
		} else {

		}
	}




	/**
	 * 获取进程名字
	 *
	 * @return
	 */
	public String getProcessName() {
		try {
			File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
			BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
			String processName = mBufferedReader.readLine().trim();
			mBufferedReader.close();
			return processName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}




}
