package com.nullsoft.art.kuponchikru;

import android.preference.PreferenceManager;

import java.io.IOException;
import java.util.HashSet;
import java.util.prefs.Preferences;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by art on 25.04.16.
 */
public class ReceivedCookiesInterceptor implements Interceptor {
    public static final String PREF_COOKIES = "PREF_COOKIES";

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();

            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }

            PreferenceManager.getDefaultSharedPreferences(Kuponchikru.getAppContext()).edit()
                    .putStringSet(PREF_COOKIES, cookies)
                    .apply();
        }

        return originalResponse;
    }
}