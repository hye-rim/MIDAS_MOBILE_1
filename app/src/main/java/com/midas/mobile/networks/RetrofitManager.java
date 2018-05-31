package com.midas.mobile.networks;

import android.annotation.SuppressLint;
import android.content.Context;

import com.midas.mobile.R;

import java.lang.reflect.Field;
import java.net.ConnectException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hyerim on 2018. 5. 23....
 */

public class RetrofitManager {
    private static final String TAG = RetrofitManager.class.getSimpleName();

    private static RetrofitManager instance = null;

    public static final String BASE_URL = "http://13.125.46.183:3000/";

    public static final String TEST = "test";
    public static final String LOGIN = "jwt";
    public static final String JOIN = "user";
    public static final String MENU = "menu";
    public static final String USER = "user";
    public static final String USER_DETAIL = "user-detail";
    public static final String RESERVATION = "reservation";
    public static final String FCM = "fcm";
    public static final String PURCHASEHISTORYADMIN = "purchase-history-admin";


    private Retrofit mRetrofit;

    private RetrofitManager() {
    }

    public static RetrofitManager getInstance() {
        if (instance == null) {
            synchronized (RetrofitManager.class) {
                if (instance == null) {
                    instance = new RetrofitManager();
                }
            }
        }
        return instance;
    }

    public void init() {
        mRetrofit = getDefaultRetrofitSetting();
    }

    private Retrofit getDefaultRetrofitSetting() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .build();
        try {
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                @SuppressLint("TrustAllX509TrustManager")
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @SuppressLint("TrustAllX509TrustManager")
                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }}, new SecureRandom());
            HostnameVerifier hv = (hostname, session) -> true;
            String workerClassName = "okhttp3.OkHttpClient";
            try {
                Class workerClass = Class.forName(workerClassName);
                Field hostnameVerifier = workerClass.getDeclaredField("hostnameVerifier");
                hostnameVerifier.setAccessible(true);
                hostnameVerifier.set(httpClient, hv);
                Field sslSocketFactory = workerClass.getDeclaredField("sslSocketFactory");
                sslSocketFactory.setAccessible(true);
                sslSocketFactory.set(httpClient, sslContext.getSocketFactory());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public static RetrofitInterface getRetrofitMethod() {
        return getInstance().getRetrofit().create(RetrofitInterface.class);
    }

    public static String catchAllThrowable(final Context context, final Throwable throwable) {
        if (throwable instanceof NullPointerException) {
            return context.getString(R.string.error_null);
        } else if (throwable instanceof ConnectException) {
            return context.getString(R.string.error_network);
        } else {
            return throwable.getMessage();
        }
    }

}