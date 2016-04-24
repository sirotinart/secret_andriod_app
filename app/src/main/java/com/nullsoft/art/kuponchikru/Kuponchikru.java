package com.nullsoft.art.kuponchikru;

import android.app.Application;
import android.content.Context;

/**
 * Created by art on 24.04.16.
 */
public class Kuponchikru extends Application
{
    private static Context context;

    public void onCreate() {
        super.onCreate();
        Kuponchikru.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }
}
