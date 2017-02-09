package com.zenglb.framework.http.core;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.CallSuper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zenglb.commonlib.utils.TextUtils;
import com.zenglb.framework.R;
import com.zenglb.framework.activity.preLogin.LoginActivity;
import com.zenglb.framework.http.utils.HttpDialogUtils;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 一般的Success 的处理各不相同，但是fail会有很多相同的处理方式
 * 一定要处理好各种异常情况。
 *
 * Communicates responses from a server or offline requests. One and only one method will be
 * invoked in response to a given request.
 *
 * Callback methods are executed using the {@link Retrofit} callback executor. When none is
 * specified, the following defaults are used:
 * <ul>
 * <li>Android: Callbacks are executed on the application's main (UI) thread.</li>
 * <li>JVM: Callbacks are executed on the background thread which performed the request.</li>
 * </ul>
 *
 * @param <T> Successful response body type.
 */

public abstract class HttpCallBack<T extends HttpResponse> implements Callback<T> {
    private final String TAG = HttpCallBack.class.getSimpleName();
    private static Gson gson = new Gson();
    private Context mContext;
    //是否需要显示Http 请求的进度，默认的是需要，但是Service 和 预取数据不需要
    private boolean showProgress = true;

    /**
     *
     * @param mContext
     */
    public HttpCallBack(Context mContext) {
        this.mContext = mContext;
        if (showProgress) {
            //show your progress bar
            showDialog(true, "loading...");
        }
    }

    /**
     * @param mContext
     * @param showProgress 默认需要显示进程，不要的话请传 false
     */
    public HttpCallBack(Context mContext, boolean showProgress) {
//		super();
        this.showProgress = showProgress;
        this.mContext = mContext;
        if (showProgress) {
            showDialog(true, null);
            //show your progress bar
        }
    }

    /**
     * Invoked for a received HTTP response.
     * <p>
     * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
     * Call {@link Response#isSuccessful()} to determine if the response indicates success.
     */
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        dismissDialog();
        if (response.isSuccessful()) {
            int responseCode = response.body().getCode();           //responseCode是api 里面定义的,进行进一步的数据和事件分发!
            if (responseCode == 0) {
                onSuccess(response.body());
            } else {
                onFailure(responseCode, response.body().getError());
            }
        } else {
            //================ http default error 401,404=================
            int code = response.raw().code();
            String message = response.raw().message();
            //================ http default error 401,404=================

            String errorBodyStr = "";
            try {
                errorBodyStr = TextUtils.convertUnicode(response.errorBody().string());
            } catch (IOException ioe) {
                Log.e("errorBodyStr ioe:", ioe.toString());
            }

            try {
                HttpResponse errorResponse = gson.fromJson(errorBodyStr, HttpResponse.class);
                if (null != errorResponse) {
                    onFailure(errorResponse.getCode(), errorResponse.getError()); //这里的code 如果定义和public void onFailure(Call<T> call, Throwable t) { 一样，要记得分开处理
                } else {
                    onFailure(-1, "ErrorResponse is null ");  //!!!!!!
                }
            } catch (Exception jsonException) {
                onFailure(-1, "http错误请求Json 信息异常"); //
                jsonException.printStackTrace();
            }

        }//response is not Successful dispose over !

    }

    /**
     * 区别处理Htpp error 和 业务逻辑的Error code ,如果有重复，需要区别处理
     * <p>
     * Invoked when a network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response.
     */
    @Override
    public void onFailure(Call<T> call, Throwable t) {
        dismissDialog();
        String temp = t.getMessage().toString();

        String errorMessage = "获取数据失败[def-error]" + temp;
        if (t instanceof SocketTimeoutException) {
            errorMessage = "服务器响应超时";
        } else if (t instanceof ConnectException) {
            errorMessage = "网络连接异常，请检查网络";
        } else if (t instanceof RuntimeException) {
            errorMessage = "运行时错误";
        } else if (t instanceof UnknownHostException) {
            errorMessage = "无法解析主机，请检查网络连接";
        } else if (t instanceof UnknownServiceException) {
            errorMessage = "未知的服务器错误";
        }
        onFailure(-1, errorMessage);
    }


    public abstract void onSuccess(T t);

    /**
     * Default error dispose!
     * 一般的就是 AlertDialog 或 SnackBar
     *
     * @param code
     * @param message
     */
    @CallSuper  //if overwrite,you should let it run.
    public void onFailure(int code, String message) {
        if (code == -1 && mContext != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("获取数据失败");
            builder.setMessage(message);
            builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog dlg = builder.create();
            dlg.show();
            dlg.setCanceledOnTouchOutside(false);

            dlg.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
            dlg.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        } else {
            //错误的统一处理
            switch (code){
                case 101:
                case 112:
                case 123:
                    //退回到登录页面
                    Intent intent = new Intent();
                    intent.setClass(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                    break;
            }

            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 显示网络请求的对话框
     *
     * @param canceledOnTouchOutside
     * @param messageText
     */
    public void showDialog(final boolean canceledOnTouchOutside, final String messageText) {
        if (mContext == null || (Activity) mContext == null || ((Activity) mContext).isFinishing())
            return;
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                HttpDialogUtils.showDialog(mContext, canceledOnTouchOutside, messageText);
            }
        });
    }

    /**
     * 关闭网络处理对话框,
     */
    public void dismissDialog() {
        if ((Activity) mContext == null || ((Activity) mContext).isFinishing())
            return;                      //maybe not good !
        if (mContext != null) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    HttpDialogUtils.dismissDialog();
                }
            });
        }
    }

}
