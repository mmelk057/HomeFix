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

public class AddService extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addservice);

        //Instantiate Back and Save Buttons
        ImageButton back = findViewById(R.id.addServiceBackButton);
        Button save = findViewById(R.id.saveButton);

        //Instantiate EditText Objects
        final EditText nameField=findViewById(R.id.serviceName);
        final EditText rateField=findViewById(R.id.serviceRate);
        final EditText infoField=findViewById(R.id.serviceDetails);

        //Instantiate var to represent previous intent
        Intent prevIntent = getIntent();
        final String prevClass = prevIntent.getStringExtra("previousClass");


        //create a listener for the back button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prevClass.equals("AdminOptions")){ //IF THE VALUE OF THE PREVIOUS INTENT EXTRA IS "XXX"
                    Intent backTrack = new Intent(AddService.this,AdminOptions.class); //Start intent basically means to start a new ACTIVITY
                    startActivity(backTrack);
                }
                else if (prevClass.equals("AdminServices")){
                    Intent backTrack = new Intent(AddService.this,AdminServices.class);
                    startActivity(backTrack);
                }
            }
        });


        //create a listener for the save button
        save.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){

                    if (nameField.getText().toString().equals("") || rateField.getText().toString().equals("") || infoField.getText().toString().equals("")) {
                        //Print Error Message
                        Toast nullErrorMessage = Toast.makeText(getApplicationContext(),"Error: One or more fields are null",Toast.LENGTH_LONG);
                        nullErrorMessage.show();
                        //TOAST MESSAGE

                        //WORK ON MAKING A CUSTOM TOAST MESSAGE WITH CUSTOM LAYOUT (!!)
                    }
                    else{

                        //add Service in DB
                        //////////////////////////////
                        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Service").child("Service"); //Create a database reference to a point in Firebase
                        Database db = new Database(dr,AddService.this,getApplicationContext()); //Setup a database object
                        db.addService(nameField.getText().toString(),rateField.getText().toString(),infoField.getText().toString());

                        if(prevClass.equals("AdminOptions")){
                            Intent backTrack = new Intent(AddService.this,AdminOptions.class);
                            startActivity(backTrack);
                        }
                        else if (prevClass.equals("AdminServices")){
                            Intent backTrack = new Intent(AddService.this,AdminServices.class);
                            startActivity(backTrack);
                        }
                    }





            }
        });
    }
}
