package com.zenglb.framework.activity.wechat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.Window;

import com.zenglb.commonlib.base.BaseActivity;
import com.zenglb.framework.R;
import com.zenglb.framework.activity.main.AreUSleepFragmentList;
import com.zenglb.framework.fragment.mainfragment.MeProfileFragment;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * 主控模块，底部带Tab的主控Activity
 * 这里的Fragment 全部实现懒加载，其他的地方不需要，但是要继承同一个base,
 *
 * @author Administrator
 */
public class MainActivityTab extends BaseActivity {
    private TabButtonGroup footer;                       //下边的功能按钮
    private ViewPager body;
    private int[] itemImage = {R.drawable.main_footer_me, R.drawable.main_footer_contanct, R.drawable.main_footer_discovery, R.drawable.main_footer_me};
    private int[] itemCheckedImage = {R.drawable.main_footer_me_selected, R.drawable.main_footer_contanct_selected, R.drawable.main_footer_discovery_selected, R.drawable.main_footer_me_selected};
    private String[] itemText;
    private ArrayList<Fragment> fragmentList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main_tab;
    }

    @Override
    protected boolean isShowBacking() {
        return false;
    }


    public void initViews() {
        getToolbar().setNavigationIcon(null);

        itemText = MainActivityTab.this.getResources().getStringArray(R.array.main_function_name_array);
        // 底部的tab_button
        footer = (TabButtonGroup) findViewById(R.id.main_footer);
        for (int i = 0; i < itemImage.length; i++) {
            footer.addItem(itemImage[i], itemCheckedImage[i], itemText[i]);
        }

        // 主体
        body = (ViewPager) findViewById(R.id.main_body);
//		body.setAdapter(new BodyPageAdapter());

        final MainPageChangeListener bodyChangeListener = new MainPageChangeListener(footer);
        body.addOnPageChangeListener(bodyChangeListener);

        fragmentList = new ArrayList();

        //fragmentList 除了默认显示的第一个Fragment，其他的全部都要实现懒加载

        fragmentList.add(AreUSleepFragmentList.newInstance("expired"));
        fragmentList.add(AreUSleepFragmentList.newInstance("doing"));
        fragmentList.add(AreUSleepFragmentList.newInstance("done"));
        fragmentList.add(MeProfileFragment.newInstance("MeProfile","333")); //!!!!!

        //设置ViewPager的设配器
        body.setAdapter(new MyAdapter(getSupportFragmentManager(), fragmentList));

        footer.setCheckedIndex(0);
        footer.setOnItemChangedListener(new TabButtonGroup.OnItemChangedListener() {
            public void onItemChanged() {
                body.setCurrentItem(footer.getCheckedIndex(), false);
            }
        });
        // BUG :显示不出数字。数字尺寸太大,
        footer.setItemNewsCount(1, 5);//设置消息数量

        body.setOffscreenPageLimit(3); //123456789--97534567
    }

    public class MyAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> list;
        public MyAdapter(FragmentManager fm, ArrayList<Fragment> list) {
            super(fm);
            this.list = list;
        }
        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
        }
        @Override
        public int getCount() {
            return list.size();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

}
