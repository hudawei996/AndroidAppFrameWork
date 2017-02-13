package com.zenglb.framework.activity.wechat;

import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;

/**
 * 页面的滚动监听器
 * 
 * @author Administrator
 */
public class MainPageChangeListener implements OnPageChangeListener {
	private String TAG="MainBodyPageChangeListener";
	private int currentCheckedIndex=0;                
	private TabButtonGroup customRadioGroup;
	public MainPageChangeListener(TabButtonGroup c) {
		this.customRadioGroup=c;
	}
	
	public void onPageScrollStateChanged(int arg0) {
		Log.i(TAG,"onPageScrollStateChanged    "+arg0);
	}
	
	/**
	 * 页面滑动的监听
	 * 
	 */
	public void onPageScrolled(int arg0, float arg1, int arg2){
		if (arg1!=0.0f) {
			int right,left;
			if (arg0==currentCheckedIndex) {
				//方向向右
				left=currentCheckedIndex;
				right=currentCheckedIndex+1;

			}else{
				//方向向左
				left=currentCheckedIndex-1;
				right=currentCheckedIndex;
			}
			
			customRadioGroup.itemChangeChecked(left, right, arg1);
//			Log.w(TAG,"onPageScrolled    "+left+"   "+right+"   "+arg1);

		}else{
			customRadioGroup.setCheckedIndex(arg0);
		}
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		currentCheckedIndex=arg0;
		Log.e(TAG,"onPageSelected    "+arg0);
	}

}
