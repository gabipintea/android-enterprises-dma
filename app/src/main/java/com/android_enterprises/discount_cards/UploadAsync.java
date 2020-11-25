package com.android_enterprises.discount_cards;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static java.net.HttpURLConnection.HTTP_OK;

public class UploadAsync extends AsyncTask<String, Void, String> {
    private static final String TAG = UploadAsync.class.getSimpleName();

    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "DoinBack");
        String result = null;
        String fileName = strings[1];
        HttpURLConnection conn = null;

        try {
            // Open a HTTP  connection to  the URL
            URL url = new URL(strings[0]);
            URLConnection connection = url.openConnection();


            if(connection instanceof HttpURLConnection) {
                Log.d(TAG, "Before opening");
                conn = (HttpURLConnection) connection;


                Log.d(TAG, "Opened");
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy

                conn.setRequestMethod("POST");

                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + "*");
                conn.setRequestProperty("myfile", fileName);

                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(strings[2]);
                dos.flush();
                dos.close();


                int resultCode = conn.getResponseCode();
                Log.d(TAG, "Code: " + resultCode);
                if(resultCode == HTTP_OK) {

                    InputStream is = conn.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is, Charset.forName(StandardCharsets.UTF_8.name()));
                    StringBuilder textBuilder = new StringBuilder();
                    try(Reader reader = new BufferedReader(isr))
                    {
                        int c = 0;
                        while((c = reader.read())!= -1)
                        {
                            textBuilder.append((char)c);
                        }
                    }
                    result = textBuilder.toString();
                }

            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
