package com.example.storagemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class DevicesCustomAdapter extends ArrayAdapter<Device>{

    private Context context;
    private int resource;

    public DevicesCustomAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String id = String.valueOf(getItem(position).getId());
        String name = getItem(position).getName();
        int typeID = getItem(position).getDeviceTypeID();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        String deviceType = databaseHelper.getDeviceType(typeID);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView textDeviceID = convertView.findViewById(R.id.textDeviceID);
        TextView textDeviceName = convertView.findViewById(R.id.textDeviceName);
        TextView textDeviceTypeName = convertView.findViewById(R.id.textDeviceTypeName);

        textDeviceID.setText(id);
        textDeviceName.setText(name);
        textDeviceTypeName.setText(deviceType);

        return convertView;
    }
}
