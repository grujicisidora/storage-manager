package com.example.storagemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UsersCustomAdapter extends ArrayAdapter<User> {

    private Context context;
    private int resource;

    public UsersCustomAdapter(Context context, int resource, ArrayList<User> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String id = String.valueOf(getItem(position).getId());
        String name = getItem(position).getFirstName() + " " + getItem(position).getLastName();
        String username = getItem(position).getUsername();

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView textID = convertView.findViewById(R.id.textID);
        TextView textName = convertView.findViewById(R.id.textName);
        TextView textUsername = convertView.findViewById(R.id.textUsername);

        textID.setText(id);
        textName.setText(name);
        textUsername.setText(username);

        return convertView;
    }

}
