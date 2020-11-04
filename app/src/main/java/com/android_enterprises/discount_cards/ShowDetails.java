package com.android_enterprises.discount_cards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ShowDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String shopName = extras.getString("shopName");
        String discountValue = extras.getString("discountValue");
        String expiryDate = extras.getString("expiryDate");

        Toast.makeText(this, shopName + " " + discountValue + " " + expiryDate, Toast.LENGTH_LONG).show();
    }


    public void returnAddCard(View view) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", "Added");
        setResult(RESULT_OK, returnIntent);
        finish();
    }

}