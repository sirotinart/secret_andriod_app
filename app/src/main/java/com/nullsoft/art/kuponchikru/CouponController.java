package com.nullsoft.art.kuponchikru;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by art on 26.04.16.
 */
public class CouponController
{
    private final String cacheTable="coupons_cache";
    private final String favoritesTable="user_favorites";
    private final String userCouponsTable="user_coupons";

    private static CouponController instance=new CouponController();

    private CouponController()
    {
        clearCache();
    };

    public static CouponController getInstance()
    {
        return instance;
    }

    public ArrayList<Coupon> getCouponsListFromDb()
    {
        ArrayList<Coupon> data=new ArrayList<Coupon>();

        Cursor c = Database.getDb().query(cacheTable,null, null, null, null, null, "CREATION_DATE desc");
        if(c!=null )
        {
            while(c.moveToNext())
            {
                Coupon coupon=new Coupon(c);
                data.add(coupon);
            }
            c.close();
        }
        return data;
    }

    public void loadCoupons()
    {
        Call<ServerApi.ServerResponse> call = ServerApi.getCouponsService().getCouponsList(getCacheSize());

        call.enqueue(new Callback<ServerApi.ServerResponse>() {
            @Override
            public void onResponse(Call<ServerApi.ServerResponse> call, Response<ServerApi.ServerResponse> response) {
                if(response.body()!=null)
                {
                    if (response.body().success){
                        for(Coupon coupon: response.body().couponsList)
                        {
                            Database.getDb().insert(cacheTable,null,coupon.toContentValues());
                        }
                    }
                    else if(response.body().errorText!=null){
                        Log.d("CouponController Error:", response.body().errorText);
                    }
                    DataUpdateReceiver.getInstance().sendEmptyMessage(0);
                }
            }

            @Override
            public void onFailure(Call<ServerApi.ServerResponse> call, Throwable t) {
                Log.d("CouponController Error:", t.getMessage());
            }
        });
    }

    public void clearCache()
    {
        Database.getDb().delete(cacheTable, null, null);
    }

    public void clearFavoritesCache()
    {
        Database.getDb().delete(favoritesTable, null, null);
    }

    private int getCacheSize()
    {
        Cursor c=Database.getDb().query(cacheTable, new String[] {"count(*)"}, null, null, null, null, null);
        if(c!=null && c.moveToFirst())
        {
            int size=c.getInt(c.getColumnIndex("count(*)"));
            c.close();
            return size;
        }
        else return 0;
    }

    public Coupon getCoupon(int couponId)
    {
        Coupon coupon=null;

        Cursor c=Database.getDb().query(cacheTable, null, "COUPON_ID = ?", new String[]{String.valueOf(couponId)}, null, null,null);
        if(c!=null && c.moveToFirst())
        {
            coupon=new Coupon(c);
            c.close();

            Message message=new Message();
            message.what=0;
            message.obj=coupon;
            ActivityMsgHandler.getInstance().sendMessage(message);
        }
        else
        {
            Call<ServerApi.ServerResponse> call=ServerApi.getCouponsService().getCoupon(couponId);
            call.enqueue(new Callback<ServerApi.ServerResponse>() {
                @Override
                public void onResponse(Call<ServerApi.ServerResponse> call, Response<ServerApi.ServerResponse> response) {
                    if(response.body()!=null)
                    {
                        if(response.body().success)
                        {
                            Message message=new Message();
                            message.what=0;
                            message.obj=response.body().coupon;
                            ActivityMsgHandler.getInstance().sendMessage(message);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ServerApi.ServerResponse> call, Throwable t) {
                    Log.d("Error:", t.getMessage());
                }
            });
        }

        return coupon;
    }

    public void saveToFavorites(int couponId)
    {
        Cursor c=Database.getDb().query(favoritesTable, null, "COUPON_ID = ?",
                new String[] {String.valueOf(couponId)},null, null, null);
        if(c==null || !c.moveToNext())
        {
            ContentValues cv=new ContentValues();
            cv.put("COUPON_ID", couponId);
            Database.getDb().insert(favoritesTable, null, cv);
        }
    }

    public void loadFavorites()
    {
        Cursor c=Database.getDb().query(favoritesTable, null, null, null, null, null, null);
        if(c!=null)
        {
            while (c.moveToNext())
            {
                final int couponId=c.getInt(c.getColumnIndex("COUPON_ID"));

                Call<ServerApi.ServerResponse> call=ServerApi.getCouponsService().getCoupon(couponId);
                call.enqueue(new Callback<ServerApi.ServerResponse>(){

                    @Override
                    public void onResponse(Call<ServerApi.ServerResponse> call, Response<ServerApi.ServerResponse> response) {
                        if(response.body()!=null)
                        {
                            if(response.body().success)
                            {
                                Database.getDb().insert(cacheTable,null, response.body().coupon.toContentValues());
                                DataUpdateReceiver.getInstance().sendEmptyMessage(0);
                            }
                            else if(response.body().errorText.equals("Купон не найден!"))
                            {
                                Database.getDb().delete(favoritesTable, "COUPON_ID = ?", new String[] {String.valueOf(couponId)});
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ServerApi.ServerResponse> call, Throwable t) {
                        Log.d("CouponController Error:", t.getMessage());
                    }
                });
            }
            c.close();
        }

    }

    public void removeFromFavorites(int couponId)
    {
        Database.getDb().delete(favoritesTable, "COUPON_ID = ?", new String[]{String.valueOf(couponId)});
        loadFavorites();
        DataUpdateReceiver.getInstance().sendEmptyMessage(0);
    }

    public void loadUserCoupons()
    {
        User user=UserController.getController().get();
        Call<ServerApi.ServerResponse> call=ServerApi.getCouponsService().getUserCouponsList(user.USER_ID);

        call.enqueue(new Callback<ServerApi.ServerResponse>() {
            @Override
            public void onResponse(Call<ServerApi.ServerResponse> call, Response<ServerApi.ServerResponse> response) {
                if(response.body()!=null)
                {
                    if (response.body().success)
                    {
                        for(CouponWithCode coupon: response.body().userCouponsList)
                        {
                            Database.getDb().insert(userCouponsTable, null, coupon.toContentValues());
                            DataUpdateReceiver.getInstance().sendEmptyMessage(0);
                        }
                    }
                    else if(response.body().errorText!=null)
                    {
                        Log.d("CouponController Error:", response.body().errorText);
                    }
                }
                else
                {
                    Log.d("CouponController Error:", "server error");
                }

            }

            @Override
            public void onFailure(Call<ServerApi.ServerResponse> call, Throwable t) {
                Log.d("CouponController Error:", t.getMessage());
            }
        });
    }

    public CouponWithCode getUserCoupon(String couponCode)
    {
        Cursor c=Database.getDb().query(userCouponsTable, null, "CODE = ?", new String[]{couponCode}, null, null, null);
        if(c!=null && c.moveToFirst())
        {
            CouponWithCode couponWithCode=new CouponWithCode(c);
            c.close();
            return couponWithCode;
        }

        return null;
    }

    public ArrayList<Coupon> getHistoryListFromDb()
    {
        ArrayList<Coupon> data=new ArrayList<>();
        Cursor c=Database.getDb().query(userCouponsTable, null, null, null, null, null, null);
        if(c!=null)
        {
            while(c.moveToNext())
            {
                CouponWithCode couponWithCode=new CouponWithCode(c);
                data.add(couponWithCode);
            }
            c.close();
        }
        return data;
    }

    public void clearUserCache()
    {
        Database.getDb().delete(userCouponsTable,null, null);
    }

    public void buyCoupon(int couponId)
    {
        User user=UserController.getController().get();

        Call<ServerApi.ServerResponse> call=ServerApi.getCouponsService().buyCoupon(user.USER_ID, couponId);
        call.enqueue(new Callback<ServerApi.ServerResponse>() {
            @Override
            public void onResponse(Call<ServerApi.ServerResponse> call, Response<ServerApi.ServerResponse> response) {
                if(response.body()!=null)
                {
                    if(response.body().success)
                    {
                        ActivityMsgHandler.getInstance().sendEmptyMessage(1);
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerApi.ServerResponse> call, Throwable t) {
                Log.d("Error:", t.getMessage());
            }
        });
    }
}
