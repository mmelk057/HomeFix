package org.homefix.homefix;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.homefix.homefix.ServiceCategory;

import java.util.List;

public class ServiceListAdapter extends ArrayAdapter<ServiceCategory>{
    //variable which represents the list layout being used
    private int resource;
    private Context context;


    public ServiceListAdapter(@NonNull Context context, int resource,@NonNull List<ServiceCategory> objects) {
        super(context, resource, objects);
        this.resource=resource;
        this.context=context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView,@NonNull ViewGroup parent) {
        //Instantiate all Rate and Service Name Variables
        String serviceName = getItem(position).getName();
        double serviceRate = getItem(position).getRate();

        //Transforming a layout into a view --- ADAPTER ORGANIZES VIEWS
        LayoutInflater convertToView = LayoutInflater.from(context);
        View newView = convertToView.inflate(resource,parent,false);

        TextView serviceET = (TextView) newView.findViewById(R.id.serviceEditText);
        TextView rateET = (TextView) newView.findViewById(R.id.rateEditText);

        serviceET.setText(serviceName);
        rateET.setText(String.valueOf(serviceRate)+"$/hr");
        newView.setTag(getItem(position));
        return newView;
    }
}
