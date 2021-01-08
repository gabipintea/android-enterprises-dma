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
import com.android_enterprises.discountcards.ui.dialogs.CardDialog;
import com.android_enterprises.discountcards.ui.dialogs.UserDialog;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddActivity extends AppCompatActivity {
    // Debugging TAG
    private static final String TAG = AddActivity.class.getSimpleName();

    private Pattern pattern;
    private Matcher matcher;

    private static final String DATE_PATTERN =
            "(0?[1-9]|1[012])[\\/.-](0?[1-9]|[12][0-9]|3[01])[\\/.-]((19|20)\\d\\d)";

    public boolean validate(final String date) {

        matcher = pattern.matcher(date);

        if (matcher.matches()) {
            matcher.reset();

            if (matcher.find()) {
                String day = matcher.group(1);
                String month = matcher.group(2);
                int year = Integer.parseInt(matcher.group(3));

                if (day.equals("31") &&
                        (month.equals("4") || month.equals("6") || month.equals("9") ||
                                month.equals("11") || month.equals("04") || month.equals("06") ||
                                month.equals("09"))) {
                    return false; // only 1,3,5,7,8,10,12 has 31 days
                } else if (month.equals("2") || month.equals("02")) {
                    //leap year
                    if (year % 4 == 0) {
                        if (day.equals("30") || day.equals("31")) {
                            return false;
                        } else {
                            return true;
                        }
                    } else {
                        if (day.equals("29") || day.equals("30") || day.equals("31")) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

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
        shopSpinner = (Spinner) findViewById(R.id.shopSpinner);
        discountValue = (MySeekBar) findViewById(R.id.discountValue);
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
        expiryDateField = (EditText) findViewById(R.id.expiryDate);
        setSupportActionBar(toolbar);

        db = new DBHelper(this);

        Intent i = getIntent();
        User selectedUser = (User) i.getParcelableExtra("selectedUser");
        if (selectedUser != null) {
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
                matcher = Pattern.compile(DATE_PATTERN).matcher(expiryDate);

                boolean result=false;

                if(matcher.matches()) {
                    result = db.createCard(selectedShop.getShopId(), selectedUser.getEmail(), discount, expiryDate);
                } else if(!matcher.matches()) {
                    Toast.makeText(getApplicationContext(), "Invalid Date!", Toast.LENGTH_SHORT).show();
                }
                if (result) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        FloatingActionButton camfab = findViewById(R.id.fsb);
        camfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "This scans a QR code", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        shopSpinner = (Spinner) findViewById(R.id.shopSpinner);

        shops.clear();
        shops = db.getShops();
        //Toast.makeText(this, String.valueOf(shops.size()), Toast.LENGTH_LONG).show();
        Map<Long, Shop> shopMap = new HashMap<>();
        for (Shop shop : shops) {
            shopMap.put(shop.getShopId(), shop);
        }

        ShopAdapter shopAdapter = new ShopAdapter(shopMap, this);
        shopSpinner.setAdapter(shopAdapter);
    }
}