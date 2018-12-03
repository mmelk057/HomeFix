package org.homefix.homefix;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import java.util.Calendar;
import java.util.List;


public class UserServiceProvider extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homeowner_service_provider_screen);

        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("User").child("ServiceProvider");
        Database serviceDB = new Database(dr,UserServiceProvider.this,getApplicationContext());
        serviceDB.listServiceandRating(R.id.homeowner_service_providerlv);
        // initialize buttons the list of services

        ImageButton homeOwnerServcePBackButton = findViewById(R.id.homeOwnerServiceProviderBackButton);
        TextView ServiceName = findViewById(R.id.serviceNameTitle);
        //Button Review = findViewById(R.id.Review);
        //Button Date = findViewById(R.id.Date);

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
            this.activity= activity;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent)  {
            String CompanyName = getItem(position).getCompanyName();
            String Ratings = getItem(position).getRating();

            //Transforming a layout into a view --- ADAPTER ORGANIZES VIEWS
            LayoutInflater convertToView= LayoutInflater.from(context);
            View newView = convertToView.inflate(resource,parent,false);

            TextView ServiceProviderName = (TextView) newView.findViewById(R.id.company_name_2);
            TextView Rating = (TextView) newView.findViewById(R.id.company_rating_2);
            Button Review = (Button) newView.findViewById(R.id.Review);
            Button Date = (Button)newView.findViewById(R.id.Date);


            ///convertView.setTag(newView);
            Review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getContext(),"Button was cicked " +position, Toast.LENGTH_SHORT);
                    Intent writeReview =new Intent(context, UserReviewActivity.class);
                    activity.startActivity(writeReview);
                }
           });
            Date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar c;
                    DatePickerDialog dg;

                    c= Calendar.getInstance();
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    int month = c.get(Calendar.MONTH);
                    int year = c.get(Calendar.YEAR);

                    dg = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int mYear, int mMonth, int mDay) {
                            Intent it = new Intent(activity,UserAvailabilitiesActivity.class);
                            it.putExtra("date",mDay);
                            it.putExtra("month",mMonth);
                            it.putExtra("year",mYear);
                            activity.startActivity(it);
                        }
                    },day,month,year);
                    dg.show();

                }

            });


            try {
                ServiceProviderName.setText(CompanyName);
            }
            catch(NullPointerException e){
                ServiceProviderName.setText("[Not Currently Available]");
            }

            try {
                Rating.setText(Ratings);
            }
            catch(NullPointerException e){
                Rating.setText("[Not Currently Available]");
            }

            ArrayList<String> tempValues = new ArrayList<>(2);
            //tempValues.add("r");
//            tempValues.add("D");
            ArrayAdapter<String> sp = new ArrayAdapter<>(context,R.layout.support_simple_spinner_dropdown_item,tempValues);
            ListView lv = newView.findViewById(R.id.homeowner_service_providerlv);
            //lv.setItemsCanFocus(true);

           // lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                //@Override
                //public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //if position is 0 ; aka "Review", do something
                    //else if position 1, aka "Date", do something


                    //if (position== 0){
                        //Intent Reviews = new Intent (UserServiceProvider.this, UserReviewActivity.class);
                        //startActivity(Reviews);


                      //  }
                        //else if (position == 1){
                        // will go to date picker

                        //}

                    //}
               // }
           //);

            newView.setTag(getItem(position));
            return newView;




        }
    }




}
