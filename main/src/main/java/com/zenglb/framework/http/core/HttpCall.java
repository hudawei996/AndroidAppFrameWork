package com.zenglb.framework.http.core;

import android.util.Log;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Http 请求
 *
 * Created by Anylife.zlb@gmail.com on 2016/7/11.
 */
public class HttpCall {
    private static final String TAG = HttpCall.class.getSimpleName();
    private static String baseUrl = "http://test.4009515151.com/";
    private static String TOKEN;
    private static ApiService apiService;
//    private static ProgressResponseBody.ProgressListener progressListener;


    /**
     * set demo token
     */
    public static void setToken(String token) {
        TOKEN = token;
    }

    public static ApiService getApiService() {
        if (apiService == null) {
            //http 401 Not Authorised
            Authenticator mAuthenticator2 = new Authenticator() {
                @Override
                public Request authenticate(Route route, Response response) throws IOException {
                    if (responseCount(response) >= 2) {
                        // If both the original call and the call with refreshed token failed,it will probably keep failing, so don't try again.
                        return null;
                    }
                    refreshToken();
                    return response.request().newBuilder()
                            .header("Authorization", TOKEN)
                            .build();
                }
            };

            /**
             * 如果你的 token 是空的，就是还没有请求到 token，比如对于登陆请求，是没有 token 的，
             * 只有等到登陆之后才有 token，这时候就不进行附着上 token。另外，如果你的请求中已经带有验证 header 了，
             * 比如你手动设置了一个另外的 token，那么也不需要再附着这一个 token.
             */
            Interceptor mRequestInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request originalRequest = chain.request();
                    /***
                     * TOKEN == null，Login/Register noNeed Token
                     * noNeedAuth(originalRequest)    refreshToken api request is after log in before log out,but  refreshToken api no need auth
                     */
                    if (TOKEN == null || alreadyHasAuthorizationHeader(originalRequest) || noNeedAuth(originalRequest)) {
                        Response originalResponse = chain.proceed(originalRequest);
                        return originalResponse.newBuilder()
                                //get http request progress,et download app
//                                .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                                .build();
                    }

                    Request authorisedRequest = originalRequest.newBuilder()
                            .header("Authorization", TOKEN)
                            .build();

                    Response originalResponse = chain.proceed(authorisedRequest);
                    return originalResponse.newBuilder()
//                            .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                            .build();
                }
            };

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
//            loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)                 //??
                    .connectTimeout(2, TimeUnit.SECONDS)
                    .addNetworkInterceptor(mRequestInterceptor)
                    .addInterceptor(loggingInterceptor)
                    .authenticator(mAuthenticator2)
                    .build();


            Retrofit client = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())    //RXjava
                    .build();
            apiService = client.create(ApiService.class);
        }
        return apiService;
    }


    /**
     * uese refresh token to Refresh an Access Token
     */
    private static void refreshToken() {
//		LoginParams loginParams = new LoginParams();
//		loginParams.setClient_id("5e96eac06151d0ce2dd9554d7ee167ce");
//		loginParams.setClient_secret("aCE34n89Y277n3829S7PcMN8qANF8Fh");
//		loginParams.setGrant_type("refresh_token");
//		loginParams.setRefresh_token(MainActivity.refreshToken);
//		Call<HttpResponse<LoginResult>> refreshTokenCall = HttpCall.getApiService(null).refreshToken(loginParams);
//
//		try {
//			retrofit2.Response<HttpResponse<LoginResult>> response = refreshTokenCall.execute();
//			if (response.isSuccessful()) {
//				int responseCode = response.body().getCode();
//				if (responseCode == 0) {
//					HttpResponse<LoginResult> httpResponse = response.body();
//					HttpCall.setToken("Bearer " + httpResponse.getResult().getAccessToken());
//					MainActivity.refreshToken = httpResponse.getResult().getRefreshToken();
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
    }

    /**
     * If both the original call and the call with refreshed token failed,it will probably keep failing, so don't try again.
     * count times ++
     *
     * @param response
     * @return
     */
    private static int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }

    /**
     * check if already has auth header
     *
     * @param originalRequest
     */
    private static boolean alreadyHasAuthorizationHeader(Request originalRequest) {
        if (originalRequest.headers().toString().contains("Authorization")) {
            Log.w(TAG, "already add Auth header");
            return true;
        }
        return false;
    }

    /**
     * some request after login/oauth before logout
     * but they no need oauth,so do not add auth header
     *
     * @param originalRequest
     */
    private static boolean noNeedAuth(Request originalRequest) {
        if (originalRequest.headers().toString().contains("NoNeedAuthFlag")) {
            Log.d("WW", "no need auth !");
            return true;
        }
        return false;
    }


    /**
     *
     */
    public interface ApiService {
//		/**
//		 * login/oauth2
//		 */
//		@Headers("test: ")
//		@POST("api/lebang/oauth/access_token")
//		Call<HttpResponse<LoginResult>> goLogin(@Body LoginParams loginParams);


    }

}
