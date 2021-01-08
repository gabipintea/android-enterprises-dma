package com.android_enterprises.discountcards;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.android_enterprises.discountcards.model.DiscountCard;
import com.android_enterprises.discountcards.model.Shop;
import com.android_enterprises.discountcards.model.User;
import com.android_enterprises.discountcards.ui.dialogs.CardDialog;
import com.android_enterprises.discountcards.ui.dialogs.ScanDialog;
import com.android_enterprises.discountcards.ui.dialogs.UserDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.net.HttpURLConnection.HTTP_OK;

public class ShowDetails extends AppCompatActivity implements CardDialog.CardDialogListener, OnMapReadyCallback, ScanDialog.ScanDialogListener {

    private static final String TAG = ShowDetails.class.getSimpleName();

    DBHelper db;
    DiscountCard mDiscountCard;
    private Matcher matcher;

    private static final String DATE_PATTERN =
            "^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)(\\d{4})$";

    public boolean validateDate(final String date) {

        matcher = Pattern.compile(DATE_PATTERN).matcher(date);

        if (matcher.matches()) {
            matcher.reset();

            if (matcher.find()) {
                String day = matcher.group(1);
                String month = matcher.group(4);
                int year = Integer.parseInt(matcher.group(10));

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBHelper(this);
        setContentView(R.layout.activity_show_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Intent i = getIntent();
        DiscountCard discountCard = (DiscountCard) i.getParcelableExtra("selectedCard");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScanDialog();
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
                        URL url = new URL(shop.getLogoUrl());
//                        Log.d(TAG, java.net.URLEncoder.encode(shop.getLogoUrl(),"UTF-8"));
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
        if(validateDate(expiryDate)) {
            if(db.editCard(mDiscountCard.getShopId(), mDiscountCard.getUserEmail(),discount, expiryDate)) {
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Card was not added", Toast.LENGTH_LONG).show();
            }
        } else if(!validateDate(expiryDate)) {
            Toast.makeText(getApplicationContext(), "Invalid Date!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        String currentShopName = db.getShop(mDiscountCard.getShopId()).getShopName();
        if(currentShopName.equals("Lidl")) {
            Lidl(googleMap);
        } else if(currentShopName.equals("Kaufland")) {
            Kaufland(googleMap);
        } else if(currentShopName.equals("Carrefour")) {
            Carrefour(googleMap);
        } else {
            UnknownShop(googleMap);
        }
    }

    private void Kaufland(GoogleMap googleMap)
    {
        UiSettings ui = googleMap.getUiSettings();
        ui.setZoomControlsEnabled(true);
        googleMap.clear();
        LatLng latLng;
        MarkerOptions markerOptions;

        ArrayList<String> Kaufland = new ArrayList<String>();
        Kaufland.add("Soseaua Pipera-Tunari nr. 2/II, Voluntari 077190");
        Kaufland.add("Str. Barbu Văcărescu 120, București 020284");
        Kaufland.add("Șoseaua Pantelimon 244-246, București 021901");
        Kaufland.add("Șoseaua Colentina 6, București 021173");
        Kaufland.add("Bulevardul Bucureștii Noi 50B, București 012363");

        for(String location : Kaufland) {
            List<Address> addressList = null;
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = (Address) addressList.get(0);

            // Creating an instance of GeoPoint, to display in Google Map
            latLng = new LatLng(address.getLatitude(), address.getLongitude());


            markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(location);

            googleMap.addMarker(markerOptions);
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(44.456032241853826, 26.111870227937555)));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
    }

    private void Lidl(GoogleMap googleMap)
    {
        UiSettings ui = googleMap.getUiSettings();
        ui.setZoomControlsEnabled(true);
        googleMap.clear();
        LatLng latLng;
        MarkerOptions markerOptions;

        ArrayList<String> Lidl = new ArrayList<String>();
        Lidl.add("Strada Emil Racoviță 51, Voluntari 077190");
        Lidl.add("Șoseaua București Nord 14, Voluntari 077191");
        Lidl.add("Bulevardul Eroilor 120, Voluntari 077190");
        Lidl.add("Strada Erou Iancu Nicolae 88, Voluntari 077190");
        Lidl.add("Șoseaua Străulești 69F, București 013334");

        for(String location : Lidl) {
            List<Address> addressList = null;
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = (Address) addressList.get(0);

            // Creating an instance of GeoPoint, to display in Google Map
            latLng = new LatLng(address.getLatitude(), address.getLongitude());


            markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(location);

            googleMap.addMarker(markerOptions);
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(44.456032241853826, 26.111870227937555)));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
    }

    private void Carrefour(GoogleMap googleMap)
    {
        UiSettings ui = googleMap.getUiSettings();
        ui.setZoomControlsEnabled(true);
        googleMap.clear();
        LatLng latLng;
        MarkerOptions markerOptions;

        ArrayList<String> Carrefour = new ArrayList<String>();
        Carrefour.add("Str. Ing, Strada George Constantinescu nr.4B, București 020339");
        Carrefour.add("Calea Floreasca 246B, București");
        Carrefour.add("Strada Gara Herăstrău Nr. 4C, București 020334");
        Carrefour.add("Șoseaua Fundeni nr. 159, București 022324");
        Carrefour.add("Str. Barbu Văcărescu 154-158, București 020284");

        for(String location : Carrefour) {
            List<Address> addressList = null;
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = (Address) addressList.get(0);

            // Creating an instance of GeoPoint, to display in Google Map
            latLng = new LatLng(address.getLatitude(), address.getLongitude());


            markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(location);

            googleMap.addMarker(markerOptions);
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(44.456032241853826, 26.111870227937555)));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
    }

    private void UnknownShop(GoogleMap googleMap) {
        UiSettings ui = googleMap.getUiSettings();
        ui.setZoomControlsEnabled(true);
        googleMap.clear();
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(44.456032241853826, 26.111870227937555)));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(11));
    }

    public void openScanDialog() {
        ScanDialog scanDialog = new ScanDialog();
        scanDialog.show(getSupportFragmentManager(), "scan dialog");
    }

    @Override
    public void applyQRCode() {
        Toast.makeText(this, "Scanned", Toast.LENGTH_SHORT).show();
    }
}