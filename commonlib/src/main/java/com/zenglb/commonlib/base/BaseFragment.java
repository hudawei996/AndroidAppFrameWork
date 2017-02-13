package com.zenglb.commonlib.base;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Base Fragment,实现懒加载，一般的主页面的要求实现懒加载
 * <p>
 * Created by zenglb on 2017/1/5.
 */
public abstract class BaseFragment extends Fragment {
    private String TAG = BaseFragment.class.getSimpleName();
    //保证Fragment即使在onDetach后，仍持有Activity的引用（有引起内存泄露的风险，但是相比空指针闪退，这种做法“安全”些）
    protected Activity mActivity;          //防止getActivity()== null
    protected int visibleTime = 0;         //Fragment 可见的次数，只需要区分 0,1，>1  次就行。
    protected boolean isViewsInit = false; //标示View 是否初始化完毕了

    /**
     * 会先于onCreate 和onCreateView 执行
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) { //如果是可见
            visibleTime++;
            onVisible();
        } else {
            onInvisible();
        }
    }

    /**
     * 一定要super，放在最后面的一行代码来Super!
     */
    @CallSuper
    protected void initViews(View rootView) {
        isViewsInit = true;
    }

    /**
     * 选择性的实现懒加载方案，不是所有的Fragment 都需要懒加载的
     */
    protected abstract void lazyLoadData(boolean isForceLoad);

    /**
     * Fragment 可见的时候调用尝试调用加载数据，
     */
    protected void onVisible() {
        lazyLoadData(false);
    }

    /**
     * Fragment 不可见的时候调用，选择性的使用，可以基本不用
     */
    protected void onInvisible() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

}
