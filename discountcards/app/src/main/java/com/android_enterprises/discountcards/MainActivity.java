package com.android_enterprises.discountcards;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Spinner;
import android.widget.Toast;

import com.android_enterprises.discountcards.model.Shop;
import com.android_enterprises.discountcards.model.ShopAdapter;
import com.android_enterprises.discountcards.model.User;
import com.android_enterprises.discountcards.model.UserAdapter;
import com.android_enterprises.discountcards.model.shopType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static Menu menuNav;
    private static MenuItem itemCardslist;
    private static MenuItem itemCardsview;
    private static NavController navController;


    Spinner userSpinner;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddActivity.class);
                startActivity(i);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_cardsview, R.id.nav_cardslist)
                .setDrawerLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        //TODO maybe this username can be used for DB lookups? or it's email address?
        String strUserName = SP.getString("username", "NA");

        boolean couponNotifications = SP.getBoolean("couponNotifications",false);
        String cardsType = SP.getString("cardsType","1");
        Toast.makeText(this, "Welcome back, " + strUserName, Toast.LENGTH_LONG).show();

        menuNav = navigationView.getMenu();
        itemCardslist = menuNav.findItem(R.id.nav_cardslist);
        itemCardsview = menuNav.findItem(R.id.nav_cardsview);
        if ( cardsType.equals("1") ) {
            //Log.d(TAG, "Vrea carduri");

            if( itemCardslist != null && itemCardsview != null ) {
                itemCardslist.setVisible(false);
                itemCardsview.setVisible(true);
                navController.getGraph().setStartDestination(R.id.nav_cardsview);

            } else {
                Log.d(TAG, "NOT FOUND dar vrea carduri");
            }
        } else if ( cardsType.equals("2") ){
            //Log.d(TAG, "Vrea lista");
            if( itemCardslist != null && itemCardsview != null ) {
                itemCardslist.setVisible(true);
                itemCardsview.setVisible(false);
                navController.getGraph().setStartDestination(R.id.nav_cardslist);

            } else {
                Log.d(TAG, "NOT FOUND dar vrea lista");
            }
        }

        View headerView = navigationView.getHeaderView(0);
        userSpinner = (Spinner) headerView.findViewById(R.id.userSpinner);

        //TODO bring users from DB and put them in a map for the spinner
//        Date birthdayDate = stringToDate("2020/01/01", "yyyy/MM/dd");

        //Log.d(TAG, String.valueOf(birthdayDate));
        User u1 = new User(1,"John", "Doe", "01/01/2000", "john.doe@gmail.com");
        User u2 = new User(2,"Xi", "Cho", "01/01/2000", "xi.cho@gmail.com");
        User u3 = new User(3,"Franck", "Stank", "01/01/2000", "franck.stank@gmail.com");

        Map<Long, User> userMap = new HashMap<>();
        userMap.put(u1.getId(), u1);
        userMap.put(u2.getId(), u2);
        userMap.put(u3.getId(), u3);
        Log.d(TAG, String.valueOf(userMap.size()));

        if ( userSpinner != null ) {
            UserAdapter userAdapter = new UserAdapter(userMap, this);
            userSpinner.setAdapter(userAdapter);
        } else {
            Log.d(TAG, "No User Spinner found");
        }

    }

    private Date stringToDate(String aDate,String aFormat) {

        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;

    }



    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "resumed");



        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String cardsType = SP.getString("cardsType","1");


        if ( cardsType.equals("1") ) {
            //Log.d(TAG, "Vrea carduri");

            if( itemCardslist != null && itemCardsview != null ) {
                itemCardslist.setVisible(false);
                itemCardsview.setVisible(true);
                navController.getGraph().setStartDestination(R.id.nav_cardsview);
                navController.navigate(R.id.nav_cardsview);

            } else {
                Log.d(TAG, "NOT FOUND dar vrea carduri");
            }
        } else if ( cardsType.equals("2") ){
            //Log.d(TAG, "Vrea lista");
            if( itemCardslist != null && itemCardsview != null ) {
                itemCardslist.setVisible(true);
                itemCardsview.setVisible(false);
                navController.getGraph().setStartDestination(R.id.nav_cardslist);
                navController.navigate(R.id.nav_cardslist);

            } else {
                Log.d(TAG, "NOT FOUND dar vrea lista");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void showSettings(MenuItem item) {
        Intent i = new Intent(getApplicationContext(), MyPreferenceActivity.class);
        startActivity(i);
    }
}