package com.android_enterprises.discount_cards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android_enterprises.discount_cards.model.CardDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShowDetails extends AppCompatActivity {

    private static final String TAG = ShowDetails.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        final CardDbHelper databaseHelper = new CardDbHelper(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        long shopId = extras.getLong("shopId");
        String discountValue = extras.getString("discountValue");
        String expiryDate = extras.getString("expiryDate");

        try {

            JSONObject jsonCard = new JSONObject();
            jsonCard.put("shopId", shopId);
            jsonCard.put("discountValue", discountValue);
            jsonCard.put("expiryDate", expiryDate);

            Log.d(TAG, jsonCard.toString());

            databaseHelper.insert(jsonCard);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, shopId + " " + discountValue + " " + expiryDate, Toast.LENGTH_LONG).show();
    }


    public void returnAddCard(View view) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", "Added");
        setResult(RESULT_OK, returnIntent);
        finish();
    }

}