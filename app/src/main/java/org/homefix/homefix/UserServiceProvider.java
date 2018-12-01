package org.homefix.homefix;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

import java.util.List;


public class UserServiceProvider extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homeowner_service_provider_screen);

        // initialize buttons the list of services

        ImageButton homeOwnerServcePBackButton = findViewById(R.id.homeOwnerServiceProviderBackButton);
        TextView ServiceName = findViewById(R.id.serviceNameTitle);

        //set the intent of the buttons
        homeOwnerServcePBackButton.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               Intent backToAllCurrentBookings =  new Intent( UserServiceProvider.this, UserMainScreen.class);
               startActivity(backToAllCurrentBookings);
           }
        });



    }
    public class UserServiceListAdapter extends ArrayAdapter<ServiceProvider> {
        //variable which represents the list layout being used

        private int resource;
        private Context context;
        private Activity activity;

        public UserServiceListAdapter(@NonNull Context context, int resource, @NonNull List<ServiceProvider> objects, Activity activity) {
            super(context, resource, objects);
            this.resource = resource;
            this.context = context;
            this.activity = activity;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)  {
            String CompanyName = getItem(position).getCompanyName();
            String Ratings = getItem(position).getRating();

            //Transforming a layout into a view --- ADAPTER ORGANIZES VIEWS
            LayoutInflater convertToView= LayoutInflater.from(context);
            View newView = convertToView.inflate(resource,parent,false);

            TextView ServiceProviderName = (TextView) newView.findViewById(R.id.company_name_2);
            TextView Rating = (TextView) newView.findViewById(R.id.company_rating_2);

            ServiceProviderName.setText(CompanyName);
            Rating.setText(Ratings);

            ArrayList<String> tempValues = new ArrayList<>(2);
            tempValues.add("Review");
            tempValues.add("Date");
            ArrayAdapter<String> sp = new ArrayAdapter<>(context,R.layout.support_simple_spinner_dropdown_item,tempValues);
            ListView lv = activity.findViewById(R.id.inner_sp_list_view);
            lv.setAdapter(sp);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //if position is 0 ; aka "Review", do something
                    //else if position 1, aka "Date", do something


                    if (position== 0){
                        Intent Reviews = new Intent (UserServiceProvider.this, UserReviewActivity.class);
                        startActivity(Reviews);


                        }
                        else if (position == 1){
                        // will go to date picker

                        }

                    }
                }
            );

            newView.setTag(getItem(position));
            return newView;




        }
    }




}
