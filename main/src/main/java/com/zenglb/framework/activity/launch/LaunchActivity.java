package com.zenglb.framework.activity.launch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.zenglb.commonlib.base.BaseActivity;
import com.zenglb.commonlib.sharedpreferences.SharedPreferencesDao;
import com.zenglb.framework.R;
import com.zenglb.framework.activity.demo.DemoActivity;
import com.zenglb.framework.activity.preLogin.LoginActivity;
import com.zenglb.framework.config.SPKey;

/**
 * 启动页面的背景图放在不同的目录还会导致内存的占用大小不一样啊
 */
public class LaunchActivity extends BaseActivity {

    private Handler UiHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String accessToken = SharedPreferencesDao.getInstance().getData(SPKey.KEY_ACCESS_TOKEN, "", String.class);

                    if (TextUtils.isEmpty(accessToken)) {
                        Intent i1 = new Intent();
                        i1.setClass(LaunchActivity.this, LoginActivity.class);
                        startActivity(i1);
                        LaunchActivity.this.finish();
                    } else {
                        Intent i1 = new Intent();
                        i1.setClass(LaunchActivity.this, DemoActivity.class);
//						i1.setClass(LaunchActivity.this, MainActivityDrawer.class);
                        startActivity(i1);
                        LaunchActivity.this.finish();
                    }

                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected int setLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UiHandler.sendEmptyMessageDelayed(0, 2000); //
    }


}
