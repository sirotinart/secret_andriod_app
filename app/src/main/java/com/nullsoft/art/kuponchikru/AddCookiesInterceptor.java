package com.nullsoft.art.kuponchikru;

import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.util.HashSet;


import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by art on 25.04.16.
 */
public class AddCookiesInterceptor implements Interceptor {
    public static final String PREF_COOKIES = "PREF_COOKIES";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        HashSet<String> preferences = (HashSet<String>) PreferenceManager.getDefaultSharedPreferences(Kuponchikru.getAppContext()).getStringSet(PREF_COOKIES, new HashSet<String>());

        for (String cookie : preferences) {
            builder.addHeader("Cookie", cookie);
            Log.v("OkHttp", "Adding Header: " + cookie);
        }

        return chain.proceed(builder.build());
    }
}