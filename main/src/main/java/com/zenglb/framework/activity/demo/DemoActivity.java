package com.zenglb.framework.activity.demo;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.zenglb.commonlib.base.BaseActivity;
import com.zenglb.framework.R;

/**
 * BaseActivity is bigger
 *
 */
public class DemoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolBarTitle("MainActivity"); //也可以直接在manifest 中设置好
        getToolbar().setOnMenuItemClickListener(onMenuItemClick);
    }

    /**
     * 设置布局
     * @return
     */
    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }


    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.action_share:
                    msg += "Click share";
                    break;
                case R.id.action_settings:
                    msg += "Click setting";
                    break;
            }

            if(!TextUtils.isEmpty(msg)) {
                Toast.makeText(DemoActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_demo, menu);
        return true;
    }


}
