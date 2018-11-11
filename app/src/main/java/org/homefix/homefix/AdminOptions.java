package org.homefix.homefix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class AdminOptions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_options);

        //Instantiating Buttons for Sign out, Adding a Service and Showing the Service List
        Button signOut = findViewById(R.id.signOutButton);
        Button addServ = findViewById(R.id.addServiceButton);
        Button showList = findViewById(R.id.serviceListAddButton);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToMainActivity = new Intent(AdminOptions.this,MainActivity.class);
                startActivity(backToMainActivity);
            }
        });

        addServ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAddService = new Intent(AdminOptions.this,AddService.class);
                toAddService.putExtra("previousClass","AdminOptions");
                startActivity(toAddService);
            }
        });

        showList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toShowList = new Intent(AdminOptions.this,AdminServices.class);
                startActivity(toShowList);
            }
        });
    }
}
