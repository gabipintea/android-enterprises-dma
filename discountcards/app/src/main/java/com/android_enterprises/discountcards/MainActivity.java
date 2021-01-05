package com.android_enterprises.discountcards;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Debugging TAG
    private static final String TAG = MainActivity.class.getSimpleName();

    private static Menu menuNav;
    private static MenuItem itemCardslist;
    private static MenuItem itemCardsview;
    private static NavController navController;

    private AppBarConfiguration mAppBarConfiguration;

    DBHelper db;
    User selectedUser;
    Spinner userSpinner;
    List<User> users = new ArrayList<User>();


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

        //Get preferences
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String cardsType = SP.getString("cardsType","1");
        String username = SP.getString("username", "John");
        String email = SP.getString("email", "john.doe@gmail.com");
        Toast.makeText(this, "Welcome back, " + username, Toast.LENGTH_LONG).show();

        //Change menuItem according to preference (list or cards)
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

        //Populate userSpinner from DB
        db = new DBHelper(this);
        users = db.getUsers();

        View headerView = navigationView.getHeaderView(0);
        userSpinner = (Spinner) headerView.findViewById(R.id.userSpinner);

        // Sample Static Data (uncomment and manually add them in the userMap)
//        User u1 = new User(1,"John", "Doe", "01/01/2000", "john.doe@gmail.com");
//        User u2 = new User(2,"Xi", "Cho", "01/01/2000", "xi.cho@gmail.com");
//        User u3 = new User(3,"Franck", "Stank", "01/01/2000", "franck.stank@gmail.com");

        // Sample DB Data (uncomment the following lines to add at least three users)
        if( users.size() < 3 ) {
            //First delete all the sample data if there are present
            boolean deleted = db.deleteUser("john.doe@gmail.com");
            deleted = db.deleteUser("xi.cho@gmail.com");
            deleted = db.deleteUser("franck.stank@gmail.com");

            //Then register the sample users
            boolean registered = db.insertSampleUsers();
            if ( registered ) {
                Log.d(TAG, "Users registered");
                users.clear();
                users = db.getUsers();
            }
        }

        Map<Long, User> userMap = new HashMap<>();
        for ( User u : users) {
            userMap.put(u.getId(), u);
        }

        UserAdapter userAdapter = new UserAdapter(userMap, this);
        if ( userSpinner != null ) {
            userSpinner.setAdapter(userAdapter);

            //Get the current user based on SharedPreferences email and select it in the spinner
            selectedUser = db.getUser(email);
            for( int position = 0; position < userAdapter.getCount(); position++ ) {
                if(userAdapter.getItemEmail(position).equals(selectedUser.getEmail())) {
                    userSpinner.setSelection(position);
                }
            }
        } else {
            Log.d(TAG, "No User Spinner found");
        }

        //If user changed, change the SharedPreference
        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedUser = userAdapter.userMap.get(userSpinner.getSelectedItemId());
                SP.edit().putString("username", selectedUser.getFirstName()).apply();
                SP.edit().putString("email", selectedUser.getEmail()).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Nothing yet
            }
        });
    }

    //Simple stringToDate function
//    private Date stringToDate(String aDate,String aFormat) {
//
//        if(aDate==null) return null;
//        ParsePosition pos = new ParsePosition(0);
//        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
//        Date stringDate = simpledateformat.parse(aDate, pos);
//        return stringDate;
//
//    }



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