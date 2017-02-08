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

/**
 * 启动页面应该都一样
 *
 */
public class LaunchActivity extends BaseActivity {

    private Handler UiHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String accessToken = SharedPreferencesDao.getInstance().getData("222","",String.class);

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        UiHandler.sendEmptyMessageDelayed(0, 20000); //

    }


}
