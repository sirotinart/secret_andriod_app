package com.nullsoft.art.kuponchikru;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by art on 25.04.16.
 */
public class RetrofitManager
{
    public static Retrofit getRetrofit()
    {
        OkHttpClient okHttpClient;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new AddCookiesInterceptor());
        builder.addInterceptor(new ReceivedCookiesInterceptor());
        okHttpClient=builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.104:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit;
    }

}
