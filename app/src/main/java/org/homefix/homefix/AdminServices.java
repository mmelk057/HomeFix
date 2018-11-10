package org.homefix.homefix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class AdminServices extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminservices);

        //Instantiate Buttons
        ImageButton back = findViewById(R.id.serviceListBackButton);
        Button addButton = findViewById(R.id.serviceListAddButton);

        ServiceCategory a = new ServiceCategory("Plumbing",120,"Null");
        ServiceCategory b = new ServiceCategory("Gardening",20,"Null");

        ArrayList<ServiceCategory> services = new ArrayList<ServiceCategory>();
        services.add(a);
        services.add(b);

        ServiceListAdapter adapt = new ServiceListAdapter(getBaseContext(),R.layout.service_list_layout,services);
        final ListView serviceList = (ListView) findViewById(R.id.service_list_view);
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
