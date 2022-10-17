package com.example.storagemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ConsumablesCustomAdapter extends ArrayAdapter<Consumable> {

    private Context context;
    private int resource;

    public ConsumablesCustomAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Consumable> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String id = String.valueOf(getItem(position).getId());
        String name = getItem(position).getName();
        int typeID = getItem(position).getConsumableTypeID();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        String consumableType = databaseHelper.getConsumableType(typeID);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView textConsumableID = convertView.findViewById(R.id.textConsumableID);
        TextView textConsumableName = convertView.findViewById(R.id.textConsumableName);
        TextView textConsumableTypeName = convertView.findViewById(R.id.textConsumableTypeName);

        textConsumableID.setText(id);
        textConsumableName.setText(name);
        textConsumableTypeName.setText(consumableType);

        return convertView;
    }
}
