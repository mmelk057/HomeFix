package org.homefix.homefix;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AvailabilityListAdapter extends ArrayAdapter<String>{

    private int resource;
    private Context context;


    public AvailabilityListAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Transforming a layout into a view --- ADAPTER ORGANIZES VIEWS
        LayoutInflater convertToView = LayoutInflater.from(context);
        View newView = convertToView.inflate(resource,parent,false);
        TextView listItem = newView.findViewById(R.id.simple_dropdown_list_item);
        newView.setPadding(0,0,0,20);
        listItem.setText(getItem(position));
        newView.setTag(getItem(position));
        return newView;
    }
}
