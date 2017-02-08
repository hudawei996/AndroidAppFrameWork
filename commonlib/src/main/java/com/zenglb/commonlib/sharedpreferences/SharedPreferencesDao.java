package com.zenglb.commonlib.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * 存储key-value 数据，支持加密
 * <p>
 * Created by zenglb on 2016/11/8.
 */
public class SharedPreferencesDao {
    private static final String TAG = SharedPreferencesDao.class.getSimpleName();
    private static final String SharedPreferencesName = "YourAppName-Vanke";
    private static SharedPreferencesDao sharedPreferencesDao;

    private static Context mContext;   //init form application
    private SharedPreferences sharedPreferences;

    /**
     * init Shareprefence in application,set mContext
     */
    public static void initSharePrefenceDao(Context applicationContext) {
        mContext = applicationContext;
    }

    public static SharedPreferencesDao getInstance() {
        if (sharedPreferencesDao == null) {
            sharedPreferencesDao = new SharedPreferencesDao(Context.MODE_PRIVATE);
        }
        return sharedPreferencesDao;
    }

    /**
     * MODE_MULTI_PROCESS  will not work fine
     */
    private SharedPreferencesDao(int mode) {
        if (mContext != null) {
            sharedPreferences = mContext.getSharedPreferences(SharedPreferencesName, mode);
        } else {
            Log.e(TAG, "WARMIKNG! You must initSharePrefenceDao in your Application ！");
        }
    }


    /**
     The best way of storing double values in SharedPreferences without losing precision is:
     Transform to bit representation to store it as long:
     prefsEditor.putLong("Latitude", Double.doubleToLongBits(location.getLatitude()));
     To retrieve, transfrom from bit representation to double:
     double latitude = Double.longBitsToDouble(prefs.getLong("Latitude", 0);
     */

    /**
     * if type of defvalue is not equal clazz
     *
     * @param defValue
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getData(String key, @NonNull Object defValue, @NonNull Class<T> clazz) {
        T t = null;
        if (!defValue.getClass().getSimpleName().equals(clazz.getSimpleName())) {
            // TODO: 2016/12/2
            throw new UnsupportedOperationException("defValue type does not equals whit clazz "); //转换将失败！
        }

        switch (clazz.getSimpleName()) {
            case "String":
                t = (T) sharedPreferences.getString(key, (String) defValue);
                break;
            case "Integer":
                t = (T) (Integer) sharedPreferences.getInt(key, (Integer) defValue);
                break;
            case "Float":
                t = (T) (Float) sharedPreferences.getFloat(key, (Float) defValue);
                break;
            case "Long":
                t = (T) (Long) sharedPreferences.getLong(key, (Long) defValue);
                break;
//			case "Double":
//				t = (T) (Double) sharedPreferences.get
//				break;
            case "Boolean":
                t = (T) (Boolean) sharedPreferences.getBoolean(key, (Boolean) defValue);
                break;
        }
        return t;
    }

    /**
     * 存储数据
     *
     * @param key
     * @param value
     */
    public void saveData(String key, @NonNull Object value) {
        if (value instanceof String) {
            sharedPreferences.edit().putString(key, (String) value).commit();
        } else if (value instanceof Integer) {
            sharedPreferences.edit().putInt(key, (Integer) value).commit();
        } else if (value instanceof Float) {
            sharedPreferences.edit().putFloat(key, (Float) value).commit();
        } else if (value instanceof Long) {
            sharedPreferences.edit().putLong(key, (Long) value).commit();
        } else if (value instanceof Boolean) {
            sharedPreferences.edit().putBoolean(key, (Boolean) value).commit();
        }
    }

}
