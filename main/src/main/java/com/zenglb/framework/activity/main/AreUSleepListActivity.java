package com.zenglb.framework.activity.main;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.transition.Explode;

import com.zenglb.commonlib.base.BaseActivity;
import com.zenglb.framework.R;
import com.zenglb.framework.activity.preLogin.LoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 喂,你睡着了吗（答题列表）
 *
 * @author zenglb 2016.10.24
 */
public class AreUSleepListActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {
    private TabLayout layoutTab;
    private ViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> listTitle = new ArrayList<>();
    private AreUSleepPageAdapter areUSleepPageAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(" 演示程序DEMO");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Android 5.0 以下不能使用啊
            Explode explode = new Explode();
            explode.setDuration(300);
            getWindow().setExitTransition(explode);
            getWindow().setEnterTransition(explode);
        }
    }


    @Override
    protected int setLayoutId() {
        return R.layout.act_are_u_sleep_list;
    }

    /**
     * @param tab
     */
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                viewPager.setCurrentItem(0);
                break;
            case 1:
                viewPager.setCurrentItem(1);
                break;
            case 2:
                viewPager.setCurrentItem(2);
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    /**
     * init views
     *
     * @param
     */
    public void initViews() {
        layoutTab = (TabLayout) findViewById(R.id.layout_tab);
        layoutTab.setTabMode(TabLayout.MODE_FIXED);

        listTitle.add("经典");
        listTitle.add("最新");
        listTitle.add("收藏");

        layoutTab.addTab(layoutTab.newTab().setText(listTitle.get(0)));
        layoutTab.addTab(layoutTab.newTab().setText(listTitle.get(1)));
        layoutTab.addTab(layoutTab.newTab().setText(listTitle.get(2)));

        layoutTab.setOnTabSelectedListener(this);
        layoutTab.setSelectedTabIndicatorColor(Color.WHITE);
        layoutTab.setSelectedTabIndicatorHeight(6);

        fragmentList.add(AreUSleepFragmentList.newInstance("expired"));
        fragmentList.add(AreUSleepFragmentList.newInstance("doing"));
        fragmentList.add(AreUSleepFragmentList.newInstance("done"));

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(fragmentList.size());

        areUSleepPageAdapter = new AreUSleepPageAdapter(this.getSupportFragmentManager());
        viewPager.setAdapter(areUSleepPageAdapter);
        layoutTab.setupWithViewPager(viewPager);
    }


    /**
     * 里面的每个tab ,用fragment 来分开！
     */
    private class AreUSleepPageAdapter extends FragmentPagerAdapter {
        public AreUSleepPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return listTitle.get(position % listTitle.size());
        }
    }

}