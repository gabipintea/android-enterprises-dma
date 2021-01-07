package com.android_enterprises.discountcards.ui.backup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android_enterprises.discountcards.DBHelper;
import com.android_enterprises.discountcards.MainActivity;
import com.android_enterprises.discountcards.R;
import com.android_enterprises.discountcards.ShowDetails;
import com.android_enterprises.discountcards.model.DiscountCard;
import com.android_enterprises.discountcards.model.Shop;
import com.android_enterprises.discountcards.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class BackupFragment extends Fragment {

    private static final String TAG = BackupFragment.class.getSimpleName();

    private BackupViewModel mViewModel;

    public Button exportLocalCSV, importLocalCSV, exportLocalJSON, importLocalJSON;

    public DBHelper db;

    public static BackupFragment newInstance() {
        return new BackupFragment();
    }
    //TODO Export and import JSON, XML, CSV, TXT local and online

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        mViewModel =
//                new ViewModelProvider(this).get(BackupViewModel.class);
        View root = inflater.inflate(R.layout.backup_fragment, container, false);
        exportLocalCSV = root.findViewById(R.id.local_export_csv);
        importLocalCSV = root.findViewById(R.id.local_import_csv);
        exportLocalJSON = root.findViewById(R.id.local_export_json);
        importLocalJSON = root.findViewById(R.id.local_import_json);


        db = new DBHelper(this.getContext());

        exportLocalCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(),"I was pushed", Toast.LENGTH_SHORT).show();
                ArrayList<User> users = db.getUsers();
                ArrayList<Shop> shops = db.getShops();
                ArrayList<DiscountCard> discountCards = db.getCards();

                StringBuilder usersData = new StringBuilder();
                StringBuilder shopsData = new StringBuilder();
                StringBuilder cardsData = new StringBuilder();

                usersData.append("firstName,lastName,email,birthday");
                for (int i = 0; i < users.size(); i++) {
                    usersData.append("\n" + users.get(i).getFirstName() + "," +
                            users.get(i).getLastName() + "," +
                            users.get(i).getEmail() + "," +
                            users.get(i).getBirthday());
                }

                shopsData.append("shopId,shopName,shopType,logoURL");
                for (int i = 0; i < shops.size(); i++) {
                    shopsData.append("\n" + shops.get(i).getShopId() + "," +
                            shops.get(i).getShopName() + "," +
                            shops.get(i).getType().getId() + "," +
                            shops.get(i).getLogoUrl());
                }

                cardsData.append("shopId,userEmail,discount,expiryDate");
                for (int i = 0; i < discountCards.size(); i++) {
                    cardsData.append("\n" + discountCards.get(i).getShopId() + "," +
                            discountCards.get(i).getUserEmail() + "," +
                            discountCards.get(i).getDiscount() + "," +
                            discountCards.get(i).getExpiryDate());
                }

                FileOutputStream outUsers = null;
                FileOutputStream outShops = null;
                FileOutputStream outCards = null;

                try {
                    outUsers = getActivity().openFileOutput("users.csv", Context.MODE_PRIVATE);
                    outUsers.write((usersData.toString()).getBytes());

                    outShops = getActivity().openFileOutput("shops.csv", Context.MODE_PRIVATE);
                    outShops.write((shopsData.toString()).getBytes());

                    outCards = getActivity().openFileOutput("cards.csv", Context.MODE_PRIVATE);
                    outCards.write((cardsData.toString()).getBytes());

                    Toast.makeText(getContext(), "Saved to " + getContext().getFilesDir() + "/",
                            Toast.LENGTH_LONG).show();

                    //send on gmail / save to google drive
                    //might be useful

//                    Context context = getActivity().getApplicationContext();
//                    File fileLocation = new File(getActivity().getFilesDir(),"users.csv");
//                    Uri path = FileProvider.getUriForFile(context, "com.android_enterprises.discountcards.fileprovider", fileLocation);
//                    Intent fileIntent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
//                    fileIntent.setType("text/csv");
//                    fileIntent.putExtra(Intent.EXTRA_SUBJECT, "UserData");
//                    fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    fileIntent.putExtra(Intent.EXTRA_STREAM, path);
//                    startActivity(Intent.createChooser(fileIntent, "Export data"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (outUsers != null) {
                        try {
                            outUsers.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (outShops != null) {
                        try {
                            outShops.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (outCards != null) {
                        try {
                            outCards.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        importLocalCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.clearDatabase();

                CSVReader inUsers = null;
                CSVReader inShops = null;
                CSVReader inCards = null;

                try {
                    String[] nextLine;

                    inUsers = new CSVReader(new InputStreamReader(getActivity().openFileInput("users.csv")));
                    String fName, lName, email, birthday;

                    nextLine = inUsers.readNext();
                    while ((nextLine = inUsers.readNext()) != null) {
                        fName = nextLine[0];
                        lName = nextLine[1];
                        email = nextLine[2];
                        birthday = nextLine[3];

                        db.registerUser(fName, lName, email, birthday);
                    }

                    inShops = new CSVReader(new InputStreamReader(getActivity().openFileInput("shops.csv")));
                    String shopName, logoURL;
                    int shopType;

                    nextLine = inShops.readNext();
                    while ((nextLine = inShops.readNext()) != null) {
                        shopName = nextLine[1];
                        shopType = Integer.parseInt(nextLine[2]);
                        //logoURL = String.valueOf(java.net.URLEncoder.encode(nextLine[3], "UTF-8"));
                        logoURL = nextLine[3];
                        //Log.d(TAG, logoURL);

                        db.registerShop(shopName, shopType, logoURL);
                    }

                    inCards = new CSVReader(new InputStreamReader(getActivity().openFileInput("cards.csv")));
                    long shopId;
                    int discount;
                    String userEmail, expiryDate;

                    nextLine = inCards.readNext();
                    while ((nextLine = inCards.readNext()) != null) {
                        shopId = Integer.parseInt(nextLine[0]);
                        userEmail = nextLine[1];
                        discount = Integer.parseInt(nextLine[2]);
                        expiryDate = nextLine[3];


                        db.createCard(shopId, userEmail, discount, expiryDate);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent i = new Intent(getContext(), MainActivity.class);
                startActivity(i);

            }
        });

        exportLocalJSON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<User> users = db.getUsers();
                ArrayList<Shop> shops = db.getShops();
                ArrayList<DiscountCard> discountCards = db.getCards();

                Gson gson = new Gson();
//                Gson gsonUsers = new Gson();
//                Gson gsonShops = new Gson();
//                Gson gsonCards = new Gson();

                String jsonUsers = gson.toJson(users);
                String jsonShops = gson.toJson(shops);
                String jsonCards = gson.toJson(discountCards);
//                String jsonUsers = gsonUsers.toJson(users);
//                String jsonShops = gsonShops.toJson(shops);
//                String jsonCards = gsonCards.toJson(discountCards);


                String pathUsers = getContext().getFilesDir() + "/users.json";
                String pathShops = getContext().getFilesDir() + "/shops.json";
                String pathCards = getContext().getFilesDir() + "/cards.json";

                File fileUsers = new File(pathUsers);
                File fileShops = new File(pathShops);
                File fileCards = new File(pathCards);

                try {
                    FileOutputStream fosUsers = new FileOutputStream(fileUsers);
                    FileOutputStream fosShops = new FileOutputStream(fileShops);
                    FileOutputStream fosCards = new FileOutputStream(fileCards);


                    fosUsers.write(jsonUsers.getBytes());
                    fosShops.write(jsonShops.getBytes());
                    fosCards.write(jsonCards.getBytes());

                    fosUsers.close();
                    fosShops.close();
                    fosCards.close();

                    Toast.makeText(getContext(), "Saved to " + getContext().getFilesDir() + "/",
                            Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

        importLocalJSON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.clearDatabase();

                Gson gson = new Gson();
                String users = "", shops = "", cards = "";

                try {
                    String pathUsers = getContext().getFilesDir() + "/users.json";
                    String pathShops = getContext().getFilesDir() + "/shops.json";
                    String pathCards = getContext().getFilesDir() + "/cards.json";

                    File fileUsers = new File(pathUsers);
                    File fileShops = new File(pathShops);
                    File fileCards = new File(pathCards);

                    InputStream isUsers = new FileInputStream(fileUsers);
                    InputStream isShops = new FileInputStream(fileShops);
                    InputStream isCards = new FileInputStream(fileCards);

                    StringBuilder sbUsers = new StringBuilder();
                    StringBuilder sbShops = new StringBuilder();
                    StringBuilder sbCards = new StringBuilder();

                    if (isUsers != null) {
                        InputStreamReader inputStreamReader = new InputStreamReader(isUsers);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String receiveString = "";
                        while ((receiveString = bufferedReader.readLine()) != null) {
                            sbUsers.append(receiveString);
                        }
                        isUsers.close();
                        users = sbUsers.toString();
                    }

                    if (isShops != null) {
                        InputStreamReader inputStreamReader = new InputStreamReader(isShops);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String receiveString = "";
                        while ((receiveString = bufferedReader.readLine()) != null) {
                            sbShops.append(receiveString);
                        }
                        isShops.close();
                        shops = sbShops.toString();
                    }

                    if (isCards != null) {
                        InputStreamReader inputStreamReader = new InputStreamReader(isCards);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String receiveString = "";
                        while ((receiveString = bufferedReader.readLine()) != null) {
                            sbCards.append(receiveString);
                        }
                        isCards.close();
                        cards = sbCards.toString();
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Type userType = new TypeToken<ArrayList<User>>() {
                }.getType();
                ArrayList<User> userArrayList = gson.fromJson(users, userType);

                for (User user : userArrayList
                ) {
                    db.registerUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getBirthday());
                }

                Type shopType = new TypeToken<ArrayList<Shop>>() {
                }.getType();
                ArrayList<Shop> shopArrayList = gson.fromJson(shops, shopType);

                for (int i = 0; i<shopArrayList.size();i++) {
                    db.registerShop(shopArrayList.get(i).getShopName(),shopArrayList.get(i).getType().getId(),shopArrayList.get(i).getLogoUrl());
                }

                Type cardType = new TypeToken<ArrayList<DiscountCard>>() {
                }.getType();
                ArrayList<DiscountCard> cardArrayList = gson.fromJson(cards, cardType);

                for (int i = 0; i<cardArrayList.size();i++) {
                    db.createCard(cardArrayList.get(i).getShopId(),cardArrayList.get(i).getUserEmail(),cardArrayList.get(i).getDiscount(),cardArrayList.get(i).getExpiryDate());
                }

//                Toast.makeText(getContext(), "Imported DB from JSON",
//                        Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(), MainActivity.class);
                startActivity(i);

            }
        });

//        final TextView textView = root.findViewById(R.id.text_backup);
//        mViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BackupViewModel.class);
    }


}