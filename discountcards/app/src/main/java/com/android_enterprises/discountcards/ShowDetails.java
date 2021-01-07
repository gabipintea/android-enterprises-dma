package com.android_enterprises.discountcards;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.android_enterprises.discountcards.model.DiscountCard;
import com.android_enterprises.discountcards.model.Shop;
import com.android_enterprises.discountcards.model.User;
import com.android_enterprises.discountcards.ui.dialogs.CardDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import static java.net.HttpURLConnection.HTTP_OK;

public class ShowDetails extends AppCompatActivity implements CardDialog.CardDialogListener {

    private static final String TAG = ShowDetails.class.getSimpleName();

    DBHelper db;
    DiscountCard mDiscountCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DBHelper(this);
        Intent i = getIntent();
        DiscountCard discountCard = (DiscountCard) i.getParcelableExtra("selectedCard");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO show the QR code within a popup and increase the screen brightness
                Snackbar.make(view, "This shows the QR code", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton deletefab = findViewById(R.id.deletefab);
        deletefab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(db.deleteCard(discountCard.getShopId(), discountCard.getUserEmail())) {
                    Intent back = new Intent(view.getContext(), MainActivity.class);
                    startActivity(back);
                    finish();
                } else {
                    Snackbar.make(view, "Deletion failed", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        FloatingActionButton editfab = findViewById(R.id.editfab);
        editfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("discount", mDiscountCard.getDiscount());
                bundle.putString("expirydate", mDiscountCard.getExpiryDate());
                openDialog(bundle);
            }
        });

        if(discountCard != null ) {
            mDiscountCard = discountCard;

            ImageView logo = findViewById(R.id.logoView);
            TextView shopName = findViewById(R.id.tvName);
            TextView expiryDate = findViewById(R.id.tvExpiryDate);
            TextView discount = findViewById(R.id.tvDiscount);

            long shopId = discountCard.getShopId();
            Shop shop = db.getShop(shopId);

            shopName.setText(shop.getShopName());
            expiryDate.setText(discountCard.getExpiryDate());
            String discountValue = String.valueOf(discountCard.getDiscount() + "%");
            discount.setText(discountValue);

            //SHOP_LOGO
            @SuppressLint("HandlerLeak") final Handler handler = new Handler()
            {
                @SuppressLint("HandlerLeak")
                @Override
                public void handleMessage(@NonNull Message msg) {
                    //Log.d(TAG, "----------image received from thread------------");
                    Bundle data = msg.getData();
                    Bitmap image = data.getParcelable("image");
                    logo.setImageBitmap(image);
                }
            };

            //Log.d(TAG, "----------downloadImage method------------");

            Thread downloadThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    //Log.d(TAG, "----------download content run------------");
                    HttpURLConnection httpURLConnection = null;
                    try {
                        URL url = new URL(java.net.URLEncoder.encode(shop.getLogoUrl(),"UTF-8"));
                        URLConnection connection = url.openConnection();
                        if(connection instanceof HttpURLConnection)
                        {
                            httpURLConnection = (HttpURLConnection) connection;
                            httpURLConnection.connect();
                            int resultCode = httpURLConnection.getResponseCode();
                            if(resultCode == HTTP_OK)
                            {
                                InputStream is = httpURLConnection.getInputStream();
                                Bitmap bitmap = BitmapFactory.decodeStream(is);
                                Message message = handler.obtainMessage();
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("image", bitmap);
                                message.setData(bundle);
                                handler.sendMessage(message);
                                //Log.d(TAG, "----------download finished with success------------");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            downloadThread.start();
            //Log.d(TAG, "----------download thread started------------");
            //TODO show a Google Map with the nearest shop

        } else {
            Toast.makeText(this, "No card here", Toast.LENGTH_LONG).show();
        }
    }

    public void openDialog(Bundle bundle) {
        CardDialog cardDialog = new CardDialog();
        cardDialog.setArguments(bundle);
        cardDialog.show(getSupportFragmentManager(), "card dialog");
    }

    @Override
    public void applyTexts(int discount, String expiryDate) {
        if(db.editCard(mDiscountCard.getShopId(), mDiscountCard.getUserEmail(),discount, expiryDate)) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Card was not added", Toast.LENGTH_LONG).show();
        }
    }
}