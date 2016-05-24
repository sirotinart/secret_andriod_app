package com.nullsoft.art.kuponchikru;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

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

    public static int dpToPx(int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
