package com.kkgrocery.user;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;

import okhttp3.Cache;
import okhttp3.CookieJar;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;

public class ServiceGenerator {
    private OkHttpClient.Builder httpClient;
    private Retrofit.Builder builder;
    private Retrofit retrofit;

    public ServiceGenerator(Context context, String urlApi, Gson gson) {
        this.httpClient = new OkHttpClient.Builder().cache(new Cache(context.getCacheDir(), 5 * 1048576)); // from Mb to Byte (1024 * 1024)

        this.builder = new Retrofit.Builder()
                .baseUrl(urlApi);

        if (gson != null) this.builder.addConverterFactory(GsonConverterFactory.create(gson));
        else builder.addConverterFactory(GsonConverterFactory.create());

        this.retrofit = builder.build();
    }

    public ServiceGenerator(Context context, String urlApi, Gson gson, CookieJar cookieJar) {
        this.httpClient = new OkHttpClient.Builder()
                .cookieJar(cookieJar).cache(new Cache(context.getCacheDir(), 5 * 1048576)); // from Mb to Byte (1024 * 1024)

        this.builder = new Retrofit.Builder()
                .baseUrl(urlApi);

        if (gson != null) this.builder.addConverterFactory(GsonConverterFactory.create(gson));
        else builder.addConverterFactory(GsonConverterFactory.create());

    }

    public ServiceGenerator(Context context, String urlApi, Gson gson, String consumerKey, String consumerSecret) {
        OkHttpOAuthConsumer consumer = new OkHttpOAuthConsumer(consumerKey, consumerSecret);

        this.httpClient = new OkHttpClient.Builder()
                .addInterceptor(new SigningInterceptor(consumer)).cache(new Cache(context.getCacheDir(), 5 * 1048576)); // from Mb to Byte (1024 * 1024)


        this.builder = new Retrofit.Builder()
                .baseUrl(urlApi);

        if (gson != null) this.builder.addConverterFactory(GsonConverterFactory.create(gson));
        else builder.addConverterFactory(GsonConverterFactory.create());

    }

    public <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null);
    }

    public <S> S createService(Class<S> serviceClass, String userName, String password) {
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
            String authToken = Credentials.basic(userName, password);
            return createService(serviceClass, authToken);
        }

        return createService(serviceClass, null, null);
    }

    public <S> S createService(Class<S> serviceClass, final String authToken) {
        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor = new AuthenticationInterceptor(authToken);

            if (!httpClient.interceptors().contains(interceptor))
                httpClient.addInterceptor(interceptor);
        }

        builder.client(httpClient.build());
        retrofit = builder.build();

        return retrofit.create(serviceClass);
    }

}
