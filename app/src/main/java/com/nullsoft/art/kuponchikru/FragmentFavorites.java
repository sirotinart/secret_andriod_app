package com.nullsoft.art.kuponchikru;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

/**
 * Created by art on 19.05.16.
 */
public class FragmentFavorites extends FragmentMain
{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar=(Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.favorites_screen_name);
    }


    @Override
    protected void openCouponActivity(Object object) {
        int couponId=(int)object;

        Intent intent=new Intent(getContext(), FavoriteCouponActivity.class);
        intent.putExtra("coupon_id", couponId);
        startActivity(intent);
    }

    @Override
    public void loadData() {
        CouponController.getInstance().loadFavorites();
    }

    @Override
    protected CouponListItemAdapter getAdapter() {
        return new FavoriteListItemAdapter(new MHandler());
    }
}
