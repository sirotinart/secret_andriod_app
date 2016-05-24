package com.nullsoft.art.kuponchikru;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.squareup.picasso.Picasso;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by art on 22.05.16.
 */
public class CodeCouponActivity extends AppCompatActivity
{
    protected CouponWithCode coupon;
    protected int couponId;
    protected String coupon_code;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_coupon_code);

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

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            coupon_code=bundle.getString("unique_code");

            coupon=CouponController.getInstance().getUserCoupon(coupon_code);

            couponId=coupon.COUPON_ID;
        }

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

            try{
                Bitmap barcode_bitmap = encodeAsBitmap(coupon.CODE, BarcodeFormat.QR_CODE, 700, 700);
                image.setImageBitmap(barcode_bitmap);
            } catch (WriterException e) {
                Log.d("Error:", e.getMessage());
            }

        }
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

    private static Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height)
            throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;

        hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
}
