package com.android_enterprises.discountcards;

import android.nfc.Tag;
import android.os.Bundle;

import com.android_enterprises.discountcards.model.Shop;
import com.android_enterprises.discountcards.model.ShopAdapter;
import com.android_enterprises.discountcards.model.shopType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    Spinner shopSpinner;
    SeekBar discountValue;
    EditText expiryDateField;

    DBHelper db;

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

                boolean result = db.createCard(selectedShop.getShopId(),"andreimihai.sirbu@gmail.com", discount, expiryDate);
                if(result) {
                    Toast.makeText(AddActivity.this, "Card added Successfully!", Toast.LENGTH_SHORT).show();
                }
                //Log.i("AddActivity","Card added to db");
            }
        });

        shopSpinner = (Spinner) findViewById(R.id.shopSpinner);
        //TODO bring shops from DB and put them in a map for the spinner
        Shop s1 = new Shop(100, "Kaufland", shopType.food, "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d0/Kaufland_Logo.svg/1200px-Kaufland_Logo.svg.png");
        Shop s2 = new Shop(200, "Lidl", shopType.food, "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1d/Lidl_logo.png/600px-Lidl_logo.png");
        Shop s3 = new Shop(300, "Carrefour", shopType.food, "https://upload.wikimedia.org/wikipedia/en/thumb/1/12/Carrefour_logo_no_tag.svg/1024px-Carrefour_logo_no_tag.svg.png");
        Map<Long, Shop> shopMap = new HashMap<>();
        shopMap.put(s1.getShopId(), s1);
        shopMap.put(s2.getShopId(), s2);
        shopMap.put(s3.getShopId(), s3);

        ShopAdapter shopAdapter = new ShopAdapter(shopMap, this);
        shopSpinner.setAdapter(shopAdapter);
    }
}