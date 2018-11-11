package org.homefix.homefix;

import android.content.Intent;
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


        // TEMPORARY INPUTS
//        ServiceCategory a = new ServiceCategory("Plumbing",120,"Null");
//        ServiceCategory b = new ServiceCategory("Gardening",20,"Null");
//        services.add(a);
//        services.add(b);

        listServices();

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


    public void listServices(){
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Service").child("Service");
        dR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot service: dataSnapshot.getChildren()){
                    String info = (String)service.child("info").getValue();
                    String name = (String)service.child("name").getValue();
                    String rate = Long.toString((long)service.child("rate").getValue());
                    if (info!=null && name!= null && rate != null) {
                        ServiceCategory s = new ServiceCategory(name,Double.parseDouble(rate),info);
                        services.add(s);
                    }

                }
                ServiceListAdapter adapt = new ServiceListAdapter(getBaseContext(),R.layout.service_list_layout,services);
                ListView serviceList = findViewById(R.id.service_list_view);
                serviceList.setAdapter(adapt);
                serviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent toServiceInfo = new Intent(AdminServices.this,ServiceInfo.class);
                        ServiceCategory sc = (ServiceCategory) view.getTag();
                        toServiceInfo.putExtra("name",sc.getName());
                        toServiceInfo.putExtra("rate",String.valueOf(sc.getRate()));
                        toServiceInfo.putExtra("info",sc.getInfo());
                        startActivity(toServiceInfo);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
