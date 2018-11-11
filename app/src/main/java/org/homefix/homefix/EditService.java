package org.homefix.homefix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditService extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editservice);

        Intent previousIntent = getIntent();
        final String name = previousIntent.getStringExtra("name");
        final String rate= previousIntent.getStringExtra("rate");
        final String info= previousIntent.getStringExtra("info");

        //Instantiate all Buttons
        ImageButton backButton = findViewById(R.id.editServiceBackButton);
        Button saveButton = findViewById(R.id.saveButton);

        //Instantiate all EditText Objects
        final EditText nameField=findViewById(R.id.serviceName);
        final EditText rateField=findViewById(R.id.serviceRate);
        final EditText infoField=findViewById(R.id.serviceDetails);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToServiceInfo = new Intent(EditService.this,ServiceInfo.class);
                backToServiceInfo.putExtra("name",name);
                backToServiceInfo.putExtra("rate",rate);
                backToServiceInfo.putExtra("info",info);
                startActivity(backToServiceInfo);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameField.getText().toString().equals("") || rateField.getText().toString().equals("") || infoField.getText().toString().equals("")) {
                    //Print Error Message
                    Toast nullErrorMessage = Toast.makeText(getApplicationContext(),"Error: One or more fields are null",Toast.LENGTH_LONG);
                    nullErrorMessage.show();
                    //TOAST MESSAGE

                    //WORK ON MAKING A CUSTOM TOAST MESSAGE WITH CUSTOM LAYOUT (!!)
                }
                else{
                    //DB AUTHENTICATE

                    /////////////////
                    /////////////////
                    final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Service");
                    Database db = new Database(dr,nameField,rateField,infoField,EditService.this);
                    db.updateService(name,nameField.getText().toString(),Double.parseDouble(rateField.getText().toString()),infoField.getText().toString());
                    //DB REPLACE INFO

                    /////////////////
                    /////////////////

                    Intent backToServiceinfo = new Intent(EditService.this, ServiceInfo.class);
                    backToServiceinfo.putExtra("name",nameField.getText().toString());
                    backToServiceinfo.putExtra("rate",rateField.getText().toString());
                    backToServiceinfo.putExtra("info",infoField.getText().toString());
                    startActivity(backToServiceinfo);
                }
            }
        });

    }
}
