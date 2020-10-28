package com.android_enterprises.discount_cards;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Spinner shopName;
    SeekBar discountValue;
    EditText expiryDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showDetails(View view) {
        Intent intent = new Intent(this, ShowDetails.class);

        shopName = (Spinner)findViewById(R.id.shopSpinner);
        discountValue = (SeekBar)findViewById(R.id.discountValue);
        expiryDate   = (EditText)findViewById(R.id.expiryDate);

        Bundle bundle = new Bundle();

        bundle.putString("shopName", shopName.getSelectedItem().toString());
        bundle.putString("discountValue", String.valueOf(discountValue.getProgress()));
        bundle.putString("expiryDate", expiryDate.getText().toString());
        intent.putExtras(bundle);
//        intent.putExtra("param1", "Text from main");
//      startActivity(intent);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100) {
            if(resultCode == RESULT_OK) {
                String result = data.getStringExtra("result");
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
            }
        }
    }
}