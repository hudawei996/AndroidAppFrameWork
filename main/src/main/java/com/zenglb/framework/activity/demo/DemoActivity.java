package com.zenglb.framework.activity.demo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zenglb.commonlib.base.BaseActivity;
import com.zenglb.framework.R;
import com.zenglb.framework.entity.Messages;
import com.zenglb.framework.http.core.HttpCall;
import com.zenglb.framework.http.core.HttpCallBack;
import com.zenglb.framework.http.core.HttpResponse;
import com.zenglb.framework.http.result.Modules;

import java.util.List;

import retrofit2.Call;

import static android.R.id.message;

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
        getMessages();
//        requestModules();
    }


    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
    }


    /**
     * Test @Query and result is json array, not json object
     * <p>
     * Call<HttpResponse< jsonArray >> ,not Call<HttpResponse< jsonObj >>
     */
    private void getMessages() {
        Call<HttpResponse<List<Messages>>> getMsgsCall = HttpCall.getApiService().getMessages(0, 20);
        getMsgsCall.enqueue(new HttpCallBack<HttpResponse<List<Messages>>>(null) {
            @Override
            public void onSuccess(HttpResponse<List<Messages>> listHttpResponse) {
                ((TextView)findViewById(R.id.message_txt)).setText(listHttpResponse.getResult().toString());
            }

            @Override
            public void onFailure(int code, String messageStr) {
                super.onFailure(code, messageStr);
                ((TextView)findViewById(R.id.message_txt)).setText(code + "@@checkMobileCall@@" + messageStr);
            }

        });
    }


    /**
     * test get http
     */
    private void requestModules() {
        Call<HttpResponse<Modules>> getModulesCall = HttpCall.getApiService().getModules();
        getModulesCall.enqueue(new HttpCallBack<HttpResponse<Modules>>(this) {
            @Override
            public void onSuccess(HttpResponse<Modules> getModulesCallResponse) {
                ((TextView)findViewById(R.id.message_txt)).setText(getModulesCallResponse.getResult().toString());
            }

            @Override
            public void onFailure(int code, String failedText) {
                super.onFailure(code, failedText);
            }
        });
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
