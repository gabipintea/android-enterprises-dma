package com.android_enterprises.discountcards;

import android.os.Bundle;

import com.android_enterprises.discountcards.model.Shop;
import com.android_enterprises.discountcards.model.ShopAdapter;
import com.android_enterprises.discountcards.model.shopType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    Spinner shopName;
    SeekBar discountValue;
    EditText expiryDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO insert the card in the database for the current user (from SharedPreferences?)
                Snackbar.make(view, "This will add the card to the database", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        shopName = (Spinner) findViewById(R.id.shopSpinner);
        //TODO bring shops from DB and put them in a map for the spinner
        Shop s1 = new Shop(100, "Kaufland", shopType.Food, "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d0/Kaufland_Logo.svg/1200px-Kaufland_Logo.svg.png");
        Shop s2 = new Shop(200, "Lidl", shopType.Food, "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1d/Lidl_logo.png/600px-Lidl_logo.png");
        Shop s3 = new Shop(300, "Carrefour", shopType.Food, "https://upload.wikimedia.org/wikipedia/en/thumb/1/12/Carrefour_logo_no_tag.svg/1024px-Carrefour_logo_no_tag.svg.png");
        Map<Long, Shop> shopMap = new HashMap<>();
        shopMap.put(s1.getShopId(), s1);
        shopMap.put(s2.getShopId(), s2);
        shopMap.put(s3.getShopId(), s3);

        ShopAdapter shopAdapter = new ShopAdapter(shopMap, this);
        shopName.setAdapter(shopAdapter);
    }
}