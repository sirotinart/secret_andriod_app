package com.nullsoft.art.kuponchikru;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

/**
 * Created by art on 20.05.16.
 */
public class FavoriteCouponActivity extends CouponActivity
{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button button=(Button)findViewById(R.id.activity_favorites_btn);
        button.setText(R.string.remove_from_favorites);
    }

    @Override
    protected View.OnClickListener getFavoritesBtnOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CouponController.getInstance().removeFromFavorites(couponId);
                finish();
            }
        };
    }
}
