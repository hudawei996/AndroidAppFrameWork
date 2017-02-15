package com.zenglb.framework.config;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import java.io.File;

/**
 * Created by zenglb on 2017/2/15.
 */
public class FileCacheConfig {
    public static final String CACHE_HOME = Environment.getExternalStorageDirectory() + "/lebang/";
    public static final String CACHE_IMAGE = CACHE_HOME + "images/";
    public static final String CACHE_DATA = CACHE_HOME + "data/";
    public static final String CACHE_AUDIO = CACHE_HOME + "audio/";
    public static final String CACHE_APK = CACHE_HOME + "apk/";

}