package org.homefix.homefix;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ServiceProviderSeviceSelectionPage extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_provider_select_services_screen);

        //Set up buttons
        ImageButton backButton = findViewById(R.id.select_service_back_button);

        //get previous intent
        final Intent prevIntent = getIntent();
        final String userEmail = prevIntent.getStringExtra("user");

        //Database Reference Setup
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Service").child("Service");
        Database serviceDB = new Database(dr,ServiceProviderSeviceSelectionPage.this,getApplicationContext());
        serviceDB.listAllOfferedServices(R.id.offered_services_listview,userEmail);
        serviceDB.findServiceProviderThenListServices(R.id.chosen_services_listview,userEmail);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toProfilePage = new Intent(ServiceProviderSeviceSelectionPage.this,ServiceProviderProfilePage.class);
                toProfilePage.putExtra("user",userEmail);
                if (prevIntent.getStringExtra("name")!=null){
                    toProfilePage.putExtra("name",prevIntent.getStringExtra("name"));
                    toProfilePage.putExtra("address",prevIntent.getStringExtra("address"));
                    toProfilePage.putExtra("phoneNumber",prevIntent.getStringExtra("phoneNumber"));
                    toProfilePage.putExtra("desc",prevIntent.getStringExtra("desc"));
                }
                startActivity(toProfilePage);
            }
        });

    }
}
