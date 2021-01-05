package com.android_enterprises.discountcards;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;

import com.android_enterprises.discountcards.model.Shop;
import com.android_enterprises.discountcards.model.ShopAdapter;
import com.android_enterprises.discountcards.model.User;
import com.android_enterprises.discountcards.model.shopType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    // Debugging TAG
    private static final String TAG = AddActivity.class.getSimpleName();

    Spinner shopSpinner;
    SeekBar discountValue;
    EditText expiryDateField;

    DBHelper db;
    List<Shop> shops = new ArrayList<Shop>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = findViewById(R.id.toolbar);
        shopSpinner = (Spinner)findViewById(R.id.shopSpinner);
        discountValue = (SeekBar)findViewById(R.id.discountValue);
        expiryDateField   = (EditText)findViewById(R.id.expiryDate);
        setSupportActionBar(toolbar);

        db = new DBHelper(this);

        Intent i = getIntent();
        User selectedUser = (User)i.getParcelableExtra("selectedUser");
        if(selectedUser != null ) {
            Log.d(TAG, selectedUser.getEmail());
        } else {
            Log.d(TAG, "No USER arrived");
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO insert the card in the database for the current user (from SharedPreferences?)
//                Snackbar.make(view, "This will add the card to the database", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();



                ShopAdapter adapter = (ShopAdapter) shopSpinner.getAdapter();
                Shop selectedShop = adapter.shopList.get(shopSpinner.getSelectedItemId());

                int discount = discountValue.getProgress();
                String expiryDate = String.valueOf(expiryDateField.getText());

                boolean result = db.createCard(selectedShop.getShopId(),selectedUser.getEmail(), discount, expiryDate);
                if(result) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
            }
        });

        shopSpinner = (Spinner) findViewById(R.id.shopSpinner);

        shops.clear();
        shops = db.getShops();
        //Toast.makeText(this, String.valueOf(shops.size()), Toast.LENGTH_LONG).show();
        Map<Long, Shop> shopMap = new HashMap<>();
        for( Shop shop : shops) {
            shopMap.put(shop.getShopId(), shop);
        }

//        Shop s1 = new Shop(100, "Kaufland", shopType.food, "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d0/Kaufland_Logo.svg/1200px-Kaufland_Logo.svg.png");
//        Shop s2 = new Shop(200, "Lidl", shopType.food, "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1d/Lidl_logo.png/600px-Lidl_logo.png");
//        Shop s3 = new Shop(300, "Carrefour", shopType.food, "https://upload.wikimedia.org/wikipedia/en/thumb/1/12/Carrefour_logo_no_tag.svg/1024px-Carrefour_logo_no_tag.svg.png");
//        Map<Long, Shop> shopMap = new HashMap<>();
//        shopMap.put(s1.getShopId(), s1);
//        shopMap.put(s2.getShopId(), s2);
//        shopMap.put(s3.getShopId(), s3);

        ShopAdapter shopAdapter = new ShopAdapter(shopMap, this);
        shopSpinner.setAdapter(shopAdapter);
    }
}