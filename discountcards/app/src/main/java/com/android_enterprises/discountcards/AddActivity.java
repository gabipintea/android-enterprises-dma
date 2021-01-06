package com.android_enterprises.discountcards;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;

import com.android_enterprises.discountcards.model.Shop;
import com.android_enterprises.discountcards.model.ShopAdapter;
import com.android_enterprises.discountcards.model.User;
import com.android_enterprises.discountcards.model.shopType;
import com.android_enterprises.discountcards.ui.controls.MySeekBar;
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
    MySeekBar discountValue;
    EditText expiryDateField;

    DBHelper db;
    List<Shop> shops = new ArrayList<Shop>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = findViewById(R.id.toolbar);
        shopSpinner = (Spinner)findViewById(R.id.shopSpinner);
        discountValue = (MySeekBar)findViewById(R.id.discountValue);
        discountValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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

        ShopAdapter shopAdapter = new ShopAdapter(shopMap, this);
        shopSpinner.setAdapter(shopAdapter);
    }
}