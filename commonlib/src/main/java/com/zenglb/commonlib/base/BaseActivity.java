package com.zenglb.commonlib.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.zenglb.commonlib.R;

/**
 * 基类就只做基类的事情,不要把业务层面的代码写到这里来
 * 1.toolbar 的封装使用
 * 2.页面之间的跳转
 * 3.规范代码需要吗？？
 * 4.
 *
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = BaseActivity.class.getSimpleName();
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutId());

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        initViews();
    }


    protected abstract int setLayoutId();
    protected abstract void initViews();
    public void onClick(View view){}  //不是必须的


    /*
	 * Activity的跳转
	 */
    public void setIntentClass(Class<?> cla) {
        Intent intent = new Intent();
        intent.setClass(this, cla);
        startActivity(intent);
//        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

//    /*
//     * Activity的跳转-带参数
//     */
//    public void setIntentClass(Class<?> cla, Object obj) {
//        Intent intent = new Intent();
//        intent.setClass(this, cla);
//        intent.putExtra(INTENTTAG, (Serializable) obj);
//        startActivity(intent);
//        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//    }



    /**
     * Activity -> webview Activity的跳转-带参数
     *
     * @param
     * @param title
     * @param link
     */
    public void setIntentWebView(String title, String link) {
//        Intent intent = new Intent();
//        intent.setClass(this, WebviewActivity.class);
//        intent.putExtra(INTENTTAG, title);
//        intent.putExtra(INTENTTAG2, link);
//        startActivity(intent);
//        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }


    /**
     * Get toolbar
     *
     * @return support.v7.widget.Toolbar.
     */
    public Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }


    /**
     * 设置头部标题
     *
     * @param title
     */
    public void setToolBarTitle(CharSequence title) {
        getToolbar().setTitle(title);
        setSupportActionBar(getToolbar());
    }

    /**
     * 版本号小于21的后退按钮图片
     */
    private void showBack() {
        //setNavigationIcon必须在setSupportActionBar(toolbar);方法后面加入
        getToolbar().setNavigationIcon(R.drawable.ic_back);
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); //返回事件
            }
        });
    }

    /**
     * 是否显示后退按钮,默认显示,可在子类重写该方法.
     *
     * @return
     */
    protected boolean isShowBacking() {
        return true;
    }




    @Override
    protected void onStart() {
        super.onStart();
        if (null != getToolbar() && isShowBacking()) {
            showBack();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
