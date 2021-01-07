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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android_enterprises.discountcards.model.User;
import com.android_enterprises.discountcards.model.UserAdapter;
import com.android_enterprises.discountcards.ui.dialogs.UserDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements UserDialog.UserDialogListener {

    // Debugging TAG
    private static final String TAG = MainActivity.class.getSimpleName();

    private static Menu menuNav;
    private static MenuItem itemCardslist;
    private static MenuItem itemCardsview;
    private static MenuItem shopAdmin;
    private static NavController navController;

    private AppBarConfiguration mAppBarConfiguration;

    private static String lastCardsType = "1";

    DBHelper db;
    Bundle bundle;
    User selectedUser;
    Spinner userSpinner;
    List<User> users = new ArrayList<User>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Insert sample data for a fresh DB
        //Toggle the sample boolean to true
        //Run the app and stop it afterwards
        //Toggle the sample boolean to false
        //Run the app as you wish
        boolean sample = false;

        if(sample) {
            //#######################INSERT SCRIPT##############################
            db = new DBHelper(this);
            boolean insertedSample = db.insertSampleData();
            Log.d(TAG, String.valueOf(insertedSample));
            //##################################################################
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddActivity.class);
                i.putExtra("selectedUser", selectedUser);
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
        final String[] cardsType = {SP.getString("cardsType", "1")};
        String firstname = SP.getString("firstname", "John");
        String email = SP.getString("email", "john.doe@gmail.com");
        //Toast.makeText(this, "Welcome back, " + firstname, Toast.LENGTH_LONG).show();

        bundle = new Bundle();
        bundle.putString("email", email);


        //Change menuItem according to preferences
        menuNav = navigationView.getMenu();
        itemCardslist = menuNav.findItem(R.id.nav_cardslist);
        itemCardsview = menuNav.findItem(R.id.nav_cardsview);
        shopAdmin = menuNav.findItem(R.id.nav_shop_admin);

        if(email.equals("admin@admin.com")) {
            shopAdmin.setVisible(true);
        } else {
            shopAdmin.setVisible(false);
        }

        if ( cardsType[0].equals("1") ) {
            //Log.d(TAG, "Vrea carduri");

            if( itemCardslist != null && itemCardsview != null ) {
                itemCardslist.setVisible(false);
                itemCardsview.setVisible(true);
                //navController.getGraph().setStartDestination(R.id.nav_cardsview);
                navController.navigate(R.id.nav_cardsview, bundle);
                lastCardsType = "1";

            } else {
                Log.d(TAG, "NOT FOUND dar vrea carduri");
            }
        } else if ( cardsType[0].equals("2") ){
            //Log.d(TAG, "Vrea lista");
            if( itemCardslist != null && itemCardsview != null ) {
                itemCardslist.setVisible(true);
                itemCardsview.setVisible(false);
                //navController.getGraph().setStartDestination(R.id.nav_cardslist);
                navController.navigate(R.id.nav_cardslist, bundle);
                lastCardsType = "2";

            } else {
                Log.d(TAG, "NOT FOUND dar vrea lista");
            }
        }

        //##########USERS HEADER##########
        //Populate userSpinner from DB
        if(!sample) {
            db = new DBHelper(this);
        }

        users = db.getUsers();

        View headerView = navigationView.getHeaderView(0);
        userSpinner = (Spinner) headerView.findViewById(R.id.userSpinner);


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
                SP.edit().putString("firstname", selectedUser.getFirstName()).apply();
                SP.edit().putString("lastname", selectedUser.getLastName()).apply();
                SP.edit().putString("email", selectedUser.getEmail()).apply();
                SP.edit().putString("birthday", selectedUser.getBirthday()).apply();

                cardsType[0] = SP.getString("cardsType","1");

                if(cardsType[0].equals("1")) {
                    lastCardsType = "1";
                    bundle.clear();
                    bundle.putString("email", selectedUser.getEmail());
                    navController.navigate(R.id.nav_cardsview, bundle);

                } else if(cardsType[0].equals("2")) {
                    lastCardsType = "2";
                    bundle.clear();
                    bundle.putString("email", selectedUser.getEmail());
                    navController.navigate(R.id.nav_cardslist, bundle);
                }

                if(selectedUser.getEmail().equals("admin@admin.com")) {
                    shopAdmin.setVisible(true);
                } else {
                    shopAdmin.setVisible(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Nothing yet
            }
        });

        Button addUser = (Button) headerView.findViewById(R.id.btnAddUser);
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(), "Let's add a new user", Toast.LENGTH_SHORT).show();
                openDialog();
            }
        });
        //#########END USERS HEADER###########
    }
    public void openDialog() {
        UserDialog userDialog = new UserDialog();
        userDialog.show(getSupportFragmentManager(), "user dialog");
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
        //Log.d(TAG, "resumed");

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String cardsType = SP.getString("cardsType","1");


        if ( cardsType.equals("1") && lastCardsType.equals("2") ) {
            //Log.d(TAG, "Vrea carduri");

            if( itemCardslist != null && itemCardsview != null ) {
                itemCardslist.setVisible(false);
                itemCardsview.setVisible(true);
                //navController.getGraph().setStartDestination(R.id.nav_cardsview);
                lastCardsType = "1";
                bundle.clear();
                bundle.putString("email", selectedUser.getEmail());
                navController.navigate(R.id.nav_cardsview, bundle);


            } else {
                Log.d(TAG, "NOT FOUND dar vrea carduri");
            }
        } else if ( cardsType.equals("2") && lastCardsType.equals("1") ){
            //Log.d(TAG, "Vrea lista");
            if( itemCardslist != null && itemCardsview != null ) {
                itemCardslist.setVisible(true);
                itemCardsview.setVisible(false);
                //navController.getGraph().setStartDestination(R.id.nav_cardslist);
                lastCardsType = "2";
                bundle.clear();
                bundle.putString("email", selectedUser.getEmail());
                navController.navigate(R.id.nav_cardslist, bundle);

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
        i.putExtra("selectedUser", selectedUser);
        startActivity(i);
    }

    @Override
    public void applyTexts(String firstname, String lastname, String email, String birthdate) {
        //Toast.makeText(getApplicationContext(), firstname+" "+lastname, Toast.LENGTH_LONG).show();
        if(db.registerUser(firstname,lastname,email,birthdate)) {
            SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            SP.edit().putString("firstname", firstname).apply();
            SP.edit().putString("lastname", lastname).apply();
            SP.edit().putString("email", email).apply();
            SP.edit().putString("birthday", birthdate).apply();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else {
            Toast.makeText(getApplicationContext(), firstname+" "+lastname+" was not added", Toast.LENGTH_LONG).show();
        }
    }
}