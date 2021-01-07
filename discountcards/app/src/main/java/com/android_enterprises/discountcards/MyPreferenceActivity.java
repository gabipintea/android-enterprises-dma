package com.android_enterprises.discountcards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android_enterprises.discountcards.model.User;

public class MyPreferenceActivity extends PreferenceActivity {

    private final String TAG = MyPreferenceActivity.class.getSimpleName();
    static User selectedUser;
    static MyPreferenceFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        selectedUser = (User)i.getParcelableExtra("selectedUser");
        mFragment = new MyPreferenceFragment();

        getFragmentManager().beginTransaction().replace(android.R.id.content, mFragment).commit();


    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        private final String TAG = MyPreferenceFragment.class.getSimpleName();
        DBHelper db;
        static SharedPreferences prefs;
        static SharedPreferences.OnSharedPreferenceChangeListener listener;
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);


            prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
            listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                    Log.d(TAG, "ONCHANGE");
                    String fName = prefs.getString("firstname", "NA");
                    String lName = prefs.getString("lastname", "NA");
                    String mail = prefs.getString("email", "NA");
                    String bDay = prefs.getString("birthday", "NA");

                    String oldMail = selectedUser.getEmail();

                    db = new DBHelper(getActivity());
                    if(db.editUser(fName,lName,mail,bDay,oldMail)) {
                        db.close();
                        Toast.makeText(getActivity(), "User modified succesfully", Toast.LENGTH_LONG).show();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(getActivity(), MainActivity.class);
                                startActivity(i);
                                getActivity().finish();
                            }
                        }, 2000);
                    } else {
                        Toast.makeText(getActivity(), "Oops! Something went wrong!", Toast.LENGTH_LONG).show();
                    }
                    prefs.unregisterOnSharedPreferenceChangeListener(this);


                }
            };
            prefs.registerOnSharedPreferenceChangeListener(listener);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "Back pressed");
        MyPreferenceFragment.prefs.unregisterOnSharedPreferenceChangeListener(MyPreferenceFragment.listener);
    }
}