package com.android_enterprises.discountcards;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android_enterprises.discountcards.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyPreferenceActivity extends PreferenceActivity {
    //TODO add validations for fields other than birthday
    private Pattern pattern;
    private static Matcher matcherDate, matcherEmail;

    private static final String DATE_PATTERN =
            "(0?[1-9]|1[012])[\\/.-](0?[1-9]|[12][0-9]|3[01])[\\/.-]((19|20)\\d\\d)";

    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9.!#$%&'+\\/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:.[a-zA-Z0-9-]+)$";

    public boolean validateDate(final String date) {

        matcherDate = pattern.matcher(date);

        if (matcherDate.matches()) {
            matcherDate.reset();

            if (matcherDate.find()) {
                String day = matcherDate.group(1);
                String month = matcherDate.group(2);
                int year = Integer.parseInt(matcherDate.group(3));

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
                    //Log.d(TAG, "ONCHANGE");
                    String fName = prefs.getString("firstname", "NA");
                    String lName = prefs.getString("lastname", "NA");
                    String mail = prefs.getString("email", "NA");
                    String bDay = prefs.getString("birthday", "NA");

                    String oldMail = selectedUser.getEmail();

                    matcherDate = Pattern.compile(DATE_PATTERN).matcher(bDay);
                    matcherEmail = Pattern.compile(EMAIL_PATTERN).matcher(mail);

                    db = new DBHelper(getActivity());
                    if(matcherDate.matches() && matcherEmail.matches()) {
                        if (db.editUser(fName, lName, mail, bDay, oldMail)) {
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
                            db.close();
                            Toast.makeText(getActivity(), "Oops! Something went wrong!", Toast.LENGTH_LONG).show();
                        }
                        prefs.unregisterOnSharedPreferenceChangeListener(this);
                    } else if(!matcherDate.matches()) {
                        Toast.makeText(getActivity(), "Invalid Birthday!", Toast.LENGTH_SHORT).show();
                    } else if(!matcherEmail.matches()) {
                        Toast.makeText(getActivity(), "Invalid Email!", Toast.LENGTH_SHORT).show();
                    }

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