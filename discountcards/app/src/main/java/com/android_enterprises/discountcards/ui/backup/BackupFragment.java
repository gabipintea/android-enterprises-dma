package com.android_enterprises.discountcards.ui.backup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Xml;
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
import com.android_enterprises.discountcards.model.shopType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class BackupFragment extends Fragment {

    private static final String TAG = BackupFragment.class.getSimpleName();


    public Button exportLocalCSV, importLocalCSV, exportLocalJSON, importLocalJSON, exportLocalXML, importLocalXML;

    public DBHelper db;

    public static BackupFragment newInstance() {
        return new BackupFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.backup_fragment, container, false);
        exportLocalCSV = root.findViewById(R.id.local_export_csv);
        importLocalCSV = root.findViewById(R.id.local_import_csv);
        exportLocalJSON = root.findViewById(R.id.local_export_json);
        importLocalJSON = root.findViewById(R.id.local_import_json);
        exportLocalXML = root.findViewById(R.id.local_export_xml);
        importLocalXML = root.findViewById(R.id.local_import_xml);


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


                ArrayList<User> newusers = db.getUsers();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                preferences.edit().putString("firstname", newusers.get(0).getFirstName()).apply();
                preferences.edit().putString("lastname", newusers.get(0).getLastName()).apply();
                preferences.edit().putString("email", newusers.get(0).getEmail()).apply();
                preferences.edit().putString("birthday", newusers.get(0).getBirthday()).apply();
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
                ArrayList<User> newusers = db.getUsers();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                preferences.edit().putString("firstname", newusers.get(0).getFirstName()).apply();
                preferences.edit().putString("lastname", newusers.get(0).getLastName()).apply();
                preferences.edit().putString("email", newusers.get(0).getEmail()).apply();
                preferences.edit().putString("birthday", newusers.get(0).getBirthday()).apply();
                Intent i = new Intent(getContext(), MainActivity.class);
                startActivity(i);

            }
        });

        exportLocalXML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<User> users = db.getUsers();
                ArrayList<Shop> shops = db.getShops();
                ArrayList<DiscountCard> discountCards = db.getCards();

                File exportXML = new File( getContext().getFilesDir() + "/db.xml");
                FileOutputStream fosXML = null;
                XmlSerializer serializer = Xml.newSerializer();
                try {
                    exportXML.createNewFile();
                    fosXML = new FileOutputStream(exportXML);

                    serializer.setOutput(fosXML, "UTF-8");
                    serializer.startDocument(null, Boolean.valueOf(true));
                    serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

                    serializer.startTag(null, "database");

                    serializer.startTag(null, "users");
                    serializer.attribute(null, "number", String.valueOf(users.size()));
                    for(User u : users) {
                        serializer.startTag(null, "user");

                        serializer.startTag(null, "firstName");
                        serializer.text(u.getFirstName());
                        serializer.endTag(null, "firstName");

                        serializer.startTag(null, "lastName");
                        serializer.text(u.getLastName());
                        serializer.endTag(null, "lastName");

                        serializer.startTag(null, "email");
                        serializer.text(u.getEmail());
                        serializer.endTag(null, "email");

                        serializer.startTag(null, "birthday");
                        serializer.text(u.getBirthday());
                        serializer.endTag(null, "birthday");

                        serializer.endTag(null, "user");
                    }
                    serializer.endTag(null, "users");

                    serializer.startTag(null, "shops");
                    serializer.attribute(null, "number", String.valueOf(shops.size()));
                    for(Shop s : shops) {
                        serializer.startTag(null, "shop");

                        serializer.startTag(null, "shopId");
                        serializer.text(String.valueOf(s.getShopId()));
                        serializer.endTag(null, "shopId");

                        serializer.startTag(null, "shopName");
                        serializer.text(s.getShopName());
                        serializer.endTag(null, "shopName");

                        serializer.startTag(null, "shopType");
                        serializer.text(String.valueOf(s.getType().getId()));
                        serializer.endTag(null, "shopType");

                        serializer.startTag(null, "logoURL");
                        serializer.text(s.getLogoUrl());
                        serializer.endTag(null, "logoURL");

                        serializer.endTag(null, "shop");
                    }
                    serializer.endTag(null, "shops");

                    serializer.startTag(null, "cards");
                    serializer.attribute(null, "number", String.valueOf(discountCards.size()));
                    for(DiscountCard c : discountCards) {
                        serializer.startTag(null, "card");

                        serializer.startTag(null, "shopId");
                        serializer.text(String.valueOf(c.getShopId()));
                        serializer.endTag(null, "shopId");

                        serializer.startTag(null, "userEmail");
                        serializer.text(c.getUserEmail());
                        serializer.endTag(null, "userEmail");

                        serializer.startTag(null, "discount");
                        serializer.text(String.valueOf(c.getDiscount()));
                        serializer.endTag(null, "discount");

                        serializer.startTag(null, "expiryDate");
                        serializer.text(c.getExpiryDate());
                        serializer.endTag(null, "expiryDate");

                        serializer.endTag(null, "card");
                    }
                    serializer.endTag(null,"cards");

                    serializer.endTag(null,"database");

                    serializer.endDocument();
                    serializer.flush();
                    fosXML.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getContext(), "Saved to " + getContext().getFilesDir() + "/",
                        Toast.LENGTH_LONG).show();

            }
        });

        importLocalXML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.clearDatabase();

                User user = new User();
                Shop shop = new Shop();
                DiscountCard card = new DiscountCard();
                String text = "";

                try {
                    String path = getContext().getFilesDir()+"/db.xml";
                    File fileDatabase = new File(path);
                    InputStream isDatabase = new FileInputStream(fileDatabase);

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser parser = factory.newPullParser();

                    parser.setInput(isDatabase,null);

                    int eventType = parser.getEventType();
                    while(eventType != XmlPullParser.END_DOCUMENT) {
                        String tagName = parser.getName();
                        switch (eventType) {
                            case XmlPullParser.START_TAG:
                                if(tagName.equalsIgnoreCase("user")) {
                                    user = new User();
                                }
                                break;

                            case XmlPullParser.TEXT:
                                text = parser.getText();
                                break;

                            case XmlPullParser.END_TAG:
                                if(tagName.equalsIgnoreCase("user")) {
                                    db.registerUser(user.getFirstName(),user.getLastName(),user.getEmail(),user.getBirthday());
                                } else if(tagName.equalsIgnoreCase("firstName")) {
                                    user.setFirstName(text);
                                } else if(tagName.equalsIgnoreCase("lastName")) {
                                    user.setLastName(text);
                                } else if(tagName.equalsIgnoreCase("email")) {
                                    user.setEmail(text);
                                } else if(tagName.equalsIgnoreCase("birthday")) {
                                    user.setBirthday(text);
                                }

                                if(tagName.equalsIgnoreCase("shop")) {
                                    db.registerShop(shop.getShopName(), shop.getType().getId(), shop.getLogoUrl());
                                } else if(tagName.equalsIgnoreCase("shopName")) {
                                    shop.setShopName(text);
                                } else if(tagName.equalsIgnoreCase("shopType")) {
                                    shop.setType(shopType.fromId(Integer.parseInt(text)));
                                } else if(tagName.equalsIgnoreCase("logoURL")) {
                                    shop.setLogoUrl(text);
                                }

                                if(tagName.equalsIgnoreCase("card")) {
                                    db.createCard(card.getShopId(), card.getUserEmail(), card.getDiscount(), card.getExpiryDate());
                                } else if(tagName.equalsIgnoreCase("shopId")) {
                                    card.setShopId(Integer.parseInt(text));
                                } else if(tagName.equalsIgnoreCase("userEmail")) {
                                    card.setUserEmail(text);
                                } else if(tagName.equalsIgnoreCase("discount")) {
                                    card.setDiscount(Integer.parseInt(text));
                                } else if(tagName.equalsIgnoreCase("expiryDate")) {
                                    card.setExpiryDate(text);
                                }
                                break;

                            default:
                                break;
                        }
                        eventType=parser.next();
                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getContext(), "Imported DB",
                        Toast.LENGTH_SHORT).show();

                ArrayList<User> newusers = db.getUsers();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                preferences.edit().putString("firstname", newusers.get(0).getFirstName()).apply();
                preferences.edit().putString("lastname", newusers.get(0).getLastName()).apply();
                preferences.edit().putString("email", newusers.get(0).getEmail()).apply();
                preferences.edit().putString("birthday", newusers.get(0).getBirthday()).apply();
                Intent i = new Intent(getContext(), MainActivity.class);
                startActivity(i);
            }
        });

        return root;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}