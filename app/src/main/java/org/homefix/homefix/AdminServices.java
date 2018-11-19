package org.homefix.homefix;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminServices extends AppCompatActivity {

    ArrayList<ServiceCategory> services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminservices);
        services = new ArrayList<ServiceCategory>();
        //Instantiate Buttons
        ImageButton back = findViewById(R.id.serviceListBackButton);
        Button addButton = findViewById(R.id.serviceListAddButton);

        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Service").child("Service");
        Database serviceDB = new Database(dr,AdminServices.this,getApplicationContext());
        serviceDB.listServices(R.id.service_list_view);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToWelcome = new Intent(AdminServices.this,AdminOptions.class);
                startActivity(backToWelcome);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAddScreen = new Intent(AdminServices.this,AddService.class);
                toAddScreen.putExtra("previousClass","AdminServices");
                startActivity(toAddScreen);
            }
        });
    }
}
