package com.android_enterprises.discountcards.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android_enterprises.discountcards.MainActivity;
import com.android_enterprises.discountcards.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_OK;

public class UserAdapter extends BaseAdapter {
    private static final String TAG = UserAdapter.class.getSimpleName();

    Map<Long, User> userMap;
    LayoutInflater layoutInflater;
    Context context;

    public UserAdapter(Map<Long, User> userMap, Context context) {
        this.userMap = userMap;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return userMap.size();
    }

    @Override
    public Object getItem(int i) {
        Object[] objects = userMap.keySet().toArray();
        return userMap.get(objects[i]);
    }

    @Override
    public long getItemId(int i) {
        Object[] objects = userMap.keySet().toArray();
        return userMap.get(objects[i]).getId();
    }

    private static class UserViewHolder
    {
        //TODO: maybe add the profile picture also?
       public TextView name, email;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        final UserViewHolder holder;

        if(view == null)
        {
            view = layoutInflater.inflate(R.layout.user_items, parent, false);
            holder = new UserAdapter.UserViewHolder();
            holder.name = view.findViewById(R.id.usrTextView);
            holder.email = view.findViewById(R.id.emailTextView);
            view.setTag(holder);
        }
        else {
            holder = (UserViewHolder) view.getTag();
        }
        final User user = userMap.get(getItemId(i));

        if ( user != null ) {
            String username = user.getFirstName() + " " + user.getLastName();
            holder.name.setText(username);
            holder.email.setText(user.getEmail());
        } else {
            Log.d(TAG, "No user extracted form the map");
            Log.d(TAG, String.valueOf(getCount()));
        }


        return view;
    }
}
