package com.zenglb.framework.fragment.mainfragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dtr.settingview.lib.SettingView;
import com.dtr.settingview.lib.entity.SettingData;
import com.dtr.settingview.lib.entity.SettingViewItemData;
import com.dtr.settingview.lib.item.BasicItemViewH;
import com.dtr.settingview.lib.item.SwitchItemView;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.zenglb.commonlib.base.BaseFragment;
import com.zenglb.framework.R;
import com.zenglb.framework.activity.preLogin.LoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 懒加载的实验Fragment，将会作为github me Profile 的Fragment 复用
 * <p>
 * <p>
 * https://github.com/SpikeKing/TestCoordinatorLayout  ：个人简介使用这种布局样式，交互样式非常的好
 *
 * @author anylife.zlb@gmail.com
 */
public class MeProfileFragment extends BaseFragment {
    private String TAG = MeProfileFragment.class.getSimpleName();
    private TextView errorTipsText;    //错误提示的textView
    private TextView logout;

    private SettingView mSettingView1 = null;
    private SettingData mItemData = null;
    private SettingViewItemData mItemViewData = null;
    private List<SettingViewItemData> mListData = new ArrayList<SettingViewItemData>();

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MeProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BlankFragment.
     */
    public static MeProfileFragment newInstance(String param1, String param2) {
        MeProfileFragment fragment = new MeProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 使用这种方式来生成的Fragment 在内存不足的时候重启后会
     * 一定会在任何情况都能恢复到离开前的页面，并且保证数据的完整性。
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, mParam1 + "   onCreate  !!!!! " + savedInstanceState);
        if (getArguments() != null) {  //在  static BlankFragment newInstance 中实例化的
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * 当视图可见的时候就会被调用，当然在onCreateView 也会调用一次，
     */
    @Override
    protected void lazyLoadData(boolean isForceLoad) {
        if (isViewsInit && visibleTime < 1) {
            Log.e(TAG, "视图已经初始化完毕了，虽然不去加载网络数据，但是可以加载一下本地持久化的缓存数据啊！");
        }

        if (!isViewsInit || visibleTime < 1) {  //假如views 没有初始化或者Fragment不可见，那就不要尝试加载数据
            return;
        } else {
            if (isForceLoad) {
                Log.e(TAG, "前面的支付页面支付9.9，那么这里显示的剩余金额必然变动了，敏感数据，要实时刷新");
            }
            if (visibleTime == 1) { //这里也不是每次可见的时候都能刷新，只有第一次可见的时候或者数据加载从来没有成功 才调用刷新
                disposeHttpResult();
                Toast.makeText(mActivity,"第一次可见",Toast.LENGTH_SHORT).show();

//                new Handler().postDelayed(new Runnable() {  //是Spring View的Bug！
//                    @Override
//                    public void run() {
//                        springView.callFresh();
//                    }
//                }, 500);

            }
        }
    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(TAG, mParam1 + "   onCreateView  " + savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_me_profile, container, false);
        initViews(rootView);
        lazyLoadData(true);
        return rootView;
    }

    /**
     * 初始化所有的视图
     *
     * @param rootView
     */
    protected void initViews(View rootView) {

        logout=(TextView) rootView.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        mSettingView1 = (SettingView) rootView.findViewById(R.id.ios_style_setting_view_01);

        mSettingView1.setOnSettingViewItemClickListener(new SettingView.onSettingViewItemClickListener() {

            @Override
            public void onItemClick(int index) {
                // TODO Auto-generated method stub
                Toast.makeText(mActivity, "第" + index + "项被点击", Toast.LENGTH_SHORT).show();
                if (index == 4) {
                    mSettingView1.modifySubTitle("中国联通", index);
                } else if (index == 2) {
                    mSettingView1.modifySubTitle("关闭", index);
                }
            }
        });

        mSettingView1.setOnSettingViewItemSwitchListener(new SettingView.onSettingViewItemSwitchListener() {

            @Override
            public void onSwitchChanged(int index, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    Toast.makeText(mActivity, "第" + index + "项打开", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mActivity, "第" + index + "项关闭", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //=======================    FBI WARMMING !=================
        super.initViews(rootView);  //一定放在最后面来调用
    }

    /**
     * 在这里实现Fragment数据的缓加载.
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e(TAG, "setUserVisibleHint called,------isVisibleToUser:" + isVisibleToUser);
    }


    private void disposeHttpResult() {
        /* ==========================SettingView1========================== */
        mSettingView1.removeAllViews();
        mListData.clear();


        mItemViewData = new SettingViewItemData();
        mItemData = new SettingData();
        mItemData.setTitle("飞行模式");
        mItemViewData.setData(mItemData);
        mItemViewData.setItemView(new SwitchItemView(mActivity));
        mListData.add(mItemViewData);



        mItemViewData = new SettingViewItemData();
        mItemData = new SettingData();
        mItemData.setTitle("飞行模式2");
        mItemViewData.setData(mItemData);
        mItemViewData.setItemView(new SwitchItemView(mActivity));
        mListData.add(mItemViewData);



        mItemViewData = new SettingViewItemData();
        mItemData = new SettingData();
        mItemData.setTitle("飞行模式3");
        mItemViewData.setData(mItemData);
        mItemViewData.setItemView(new SwitchItemView(mActivity));
        mListData.add(mItemViewData);



        mItemViewData = new SettingViewItemData();
        mItemData = new SettingData();
        mItemData.setTitle("飞行模式");
        mItemViewData.setData(mItemData);
        mItemViewData.setItemView(new SwitchItemView(mActivity));
        mListData.add(mItemViewData);

        mItemViewData = new SettingViewItemData();
        mItemData = new SettingData();
        mItemData.setTitle("运营商");
        mItemData.setSubTitle("中国移动");
//        mItemData.setDrawable(getResources().getDrawable(R.drawable.main_footer_discovery_selected));
        mItemViewData.setData(mItemData);
        mItemViewData.setItemView(new BasicItemViewH(mActivity));
        mListData.add(mItemViewData);


        mItemViewData = new SettingViewItemData();
        mItemData = new SettingData();
        mItemData.setTitle("飞行模式3");
        mItemViewData.setData(mItemData);
        mItemViewData.setItemView(new SwitchItemView(mActivity));
        mListData.add(mItemViewData);


        mSettingView1.setAdapter(mListData);

        /* ==========================SettingView1========================== */
    }

    /**
     *
     */
    public void logout(){
        Intent intent = new Intent();
        intent.setClass(mActivity, LoginActivity.class);
        mActivity.startActivity(intent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, mParam1 + "   onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, mParam1 + "   onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, mParam1 + "   onDetach");
    }

}
