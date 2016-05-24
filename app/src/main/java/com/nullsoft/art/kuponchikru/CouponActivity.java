package com.nullsoft.art.kuponchikru;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by art on 03.04.16.
 */
public class CouponActivity extends AppCompatActivity
{
    protected int couponId=0;
//    protected Coupon coupon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button shopsButton=(Button)findViewById(R.id.shops_list_btn);
        shopsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openShopsActivity();
            }
        });

        Button buyCoupon=(Button)findViewById(R.id.buy_btn);
        buyCoupon.setOnClickListener(getBuyBtnOnClickListener());

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            couponId=bundle.getInt("coupon_id");

            AsyncTask load=new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    CouponController.getInstance().getCoupon(couponId);
                    return null;
                }
            };

            load.execute();
        }

        Button button=(Button)findViewById(R.id.activity_favorites_btn);
        button.setOnClickListener(getFavoritesBtnOnClickListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityMsgHandler.getInstance().setActivity(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openShopsActivity()
    {
        Intent intent=new Intent(this, MapActivity.class);
        intent.putExtra("coupon_id", couponId);
        startActivity(intent);
    }

    protected View.OnClickListener getFavoritesBtnOnClickListener()
    {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CouponController.getInstance().saveToFavorites(couponId);
            }
        };
    }

    protected View.OnClickListener getBuyBtnOnClickListener()
    {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CouponController.getInstance().buyCoupon(couponId);
            }
        };
    }

    protected void handleMessage(Message msg)
    {
        switch (msg.what)
        {
            case 0:
            {
                loadData((Coupon)msg.obj);
                break;
            }
            case 1:
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Благодарим за покупку!")
                        .setMessage("Полученный код доступен в разделе \"Мои купоны\"")
                        .setCancelable(false)
                        .setNegativeButton("ОК",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    protected void loadData(Coupon coupon)
    {
        if(coupon!=null)
        {
            getSupportActionBar().setTitle(coupon.SHORT_DESCRIPTION);

            TextView description=(TextView)findViewById(R.id.description);
            description.setText(coupon.DESCRIPTION);

            TextView sellerName=(TextView)findViewById(R.id.seller_name);
            sellerName.setText(coupon.NAME);

            TextView price=(TextView)findViewById(R.id.item_price);
            price.setText(String.format(this.getString(R.string.price),coupon.PRICE));

            TextView fullPrice=(TextView)findViewById(R.id.item_full_price);
            fullPrice.setText(String.format(this.getString(R.string.price), coupon.FULL_PRICE));

            TextView discount=(TextView)findViewById(R.id.discount);
            discount.setText(String.format(this.getString(R.string.coupon_activity_discount), coupon.DISCOUNT));

            TextView dates=(TextView)findViewById(R.id.time);
            dates.setText(String.format(this.getString(R.string.coupon_activity_dates), coupon.CREATION_DATE, coupon.EXPIRATION_DATE));

            ImageView image=(ImageView)findViewById(R.id.imageView);
            Picasso.with(this).load(coupon.COUPON_IMAGE_URL).placeholder(R.drawable.kdpv2).into(image);
        }
    }
}

