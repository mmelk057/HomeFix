package org.homefix.homefix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Login extends AppCompatActivity {

    DatabaseReference dr;
    EditText username;
    EditText password;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //Set up Login Button
        Button loginButton = (Button)findViewById(R.id.Button2);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Take the String object from the "username" textbox - make sure it's an email
                //Take the String object from the "password" textbox - make sure it's not null
                Intent previousIntent = getIntent();
                if (previousIntent.getStringExtra("type").equals("admin")){
                        type = "Admin";
                }
                else if (previousIntent.getStringExtra("type").equals("homeOwner")){
                    //Database Verification Step Required
                    //Home Owner
                    type = "HomeOwner";
                }
                else{
                    //Service Provider
                    //Database Verification Step Required
                    type = "ServiceProvider";
                }
                addUser();
                username = (EditText)findViewById(R.id.editText3);
                password = (EditText)findViewById(R.id.editText4);
                if (username.getText().toString().contains("@") && password.getText().length()!=0) { //If username is an email and password isn't null
                    //Intent toWelcome = new Intent(Login.this,Welcome.class);
                    //toWelcome.putExtra("user",username.getText().toString());
                    //startActivity(toWelcome);

                }

            }
        });
    }

    private void addUser() {
        String email = username.getText().toString().trim();
        String _password = password.getText().toString().trim();
        User user = new User(email, _password, type);
        final Task<Void> voidTask = dr.child("User").child("ListOfUsers").child(type).child(user.getUsername()).setValue(user);
        Toast.makeText(this, "User added", Toast.LENGTH_LONG).show();
    }

}
