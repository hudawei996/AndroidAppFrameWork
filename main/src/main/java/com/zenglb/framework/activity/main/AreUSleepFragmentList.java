package com.zenglb.framework.activity.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.zenglb.framework.R;
import com.zenglb.framework.entity.Messages;
import com.zenglb.framework.http.core.HttpCall;
import com.zenglb.framework.http.core.HttpCallBack;
import com.zenglb.framework.http.core.HttpResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * 喂,你睡着了吗（答题列表）
 *
 * @author zenglb 2016.10.24
 */
public class AreUSleepFragmentList extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private TextView mEmptyTipsTxt;
    private int page;
    private String mParam1;
    private SpringView springView;
    private RecyclerView mRecyclerView = null;
    private AreUSleepListAdapter areUSleepListAdapter;
    private List<AreuSleepBean> data = new ArrayList<>();

    public AreUSleepFragmentList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment AreUSleepFragment.
     */
    public static AreUSleepFragmentList newInstance(String param1) {
        AreUSleepFragmentList fragment = new AreUSleepFragmentList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_are_usleep, container, false);
        viewsInit(rootView);
        return rootView;
    }

    /**
     * init views,
     *
     * @param
     */
    private void viewsInit(View rootView) {
        areUSleepListAdapter = new AreUSleepListAdapter(getActivity(), data);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setAdapter(areUSleepListAdapter);

        springView = (SpringView) rootView.findViewById(R.id.springview);
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getHttpData(mParam1, page);
            }

            @Override
            public void onLoadmore() {
                getHttpData(mParam1, page);
            }
        });

        mEmptyTipsTxt = (TextView) rootView.findViewById(R.id.tips_txt);
        mEmptyTipsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                springView.callFresh();
            }
        });

        springView.setHeader(new DefaultHeader(getActivity()));
        springView.setFooter(new DefaultFooter(getActivity()));

        areUSleepListAdapter.setOnItemClickListener(new AreUSleepListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
//                intent.putExtra("id", resultBeen.get(position).getId());
//                intent.putExtra(LogIntentService.FROM_TYPE, LogConstant.ARE_U_SLEEP);
//                intent.putExtra(LogIntentService.FROM_ID, resultBeen.get(position).getId());
//                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }

    /**
     * 请求答题列表
     *
     */
    private void getHttpData(String mParam1, int page) {
        Call<HttpResponse<List<AreuSleepBean>>> getAreuSleepCall = HttpCall.getApiService().getAreuSleep(mParam1, page);
        getAreuSleepCall.enqueue(new HttpCallBack<HttpResponse<List<AreuSleepBean>>>(getActivity(),false) {
            @Override
            public void onSuccess(HttpResponse<List<AreuSleepBean>> listHttpResponse) {
                Log.e("dfd",listHttpResponse.getResult().toString());
                disposeHttpResult(listHttpResponse.getResult());
            }

            @Override
            public void onFailure(int code, String messageStr) {
                super.onFailure(code, messageStr);
            }

        });
    }



    /**
     * 处理http返回来的结果
     *
     * @return
     */
    private void disposeHttpResult(List<AreuSleepBean> areuSleepBeanLista) {
        springView.onFinishFreshAndLoad();
        if (areuSleepBeanLista != null) {
            if (page <= 1) data.clear();

            if (areuSleepBeanLista != null && areuSleepBeanLista.size() != 0) {
                data.addAll(areuSleepBeanLista);
                page++;
            } else {
//				ToastUtil.toast(getActivity(), "暂无数据，请稍后再试！", Toast.LENGTH_SHORT);
            }
            areUSleepListAdapter.notifyDataSetChanged();
        }

        if (data == null || data.size() == 0) {
            mEmptyTipsTxt.setVisibility(View.VISIBLE);
        } else {
            mEmptyTipsTxt.setVisibility(View.GONE);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                springView.callFresh();
            }
        }, 500);
    }

}
