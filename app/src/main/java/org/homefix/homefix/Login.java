package org.homefix.homefix;

import android.content.Intent;
import android.support.annotation.NonNull;
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

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    private String type = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //Set up Login Button and Back Button
        Button loginButton = findViewById(R.id.saveButton);
        ImageButton backButton = findViewById(R.id.loginBackButton);

        // DATABASE
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Setting up previous intent
        final Intent previousIntent = getIntent();
        final String typeUser = previousIntent.getStringExtra("type");

        // BACKEND
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Take the String object from the "username" textbox - make sure it's an email
                //Take the String object from the "password" textbox - make sure it's not null
                if (typeUser.equals("admin")){
                    type = "Admin";
                }
                else if (typeUser.equals("homeOwner")){
                    //Database Verification Step Required
                    //Home Owner
                    type = "HomeOwner";
                }
                else{
                    //Service Provider
                    //Database Verification Step Required
                    type = "ServiceProvider";
                }

                final EditText username = findViewById(R.id.editText3);
                final EditText password = findViewById(R.id.editText4);

                //splitting the username into two fields
                String email = username.getText().toString();
                if (email.contains("@") && password.getText().length()!=0){
                    String [] emailParts = email.split(Pattern.quote("@"));
                    if (emailParts.length==2) {
                        boolean areEmailSectionsValid = true;
                        for(int i=0;i<2;i++){
                            if(emailParts[i].equals("") || (emailParts[i].startsWith(".")) || (emailParts[i].endsWith("."))){  //an email split by @ cannot have either of its sections start with or end with '.'
                                areEmailSectionsValid=false;
                                break;
                            }
                            else if(emailParts[i].contains(".")){
                                String[] emailPartsV2 = emailParts[i].split(Pattern.quote("."));
                                for (int j = 0; j < emailPartsV2.length; j++) {
                                    if (emailPartsV2[j].equals("")) {
                                        areEmailSectionsValid = false;
                                        break;
                                    }
                                }

                            }
                        }
                        if(areEmailSectionsValid){
                            final String email_final = username.getText().toString();
                            final String password_final= password.getText().toString();

                            //Authenticates user's email and password
                            DatabaseReference dr = FirebaseDatabase.getInstance().getReference("User");
                            Database loggingIn = new Database(dr,Login.this,getApplicationContext());
                            loggingIn.loginAuthentication(email_final,password_final,type);


                        }
                    }
                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeUser.equals("homeOwner") || typeUser.equals("sp")){
                    //Navigate to CreateAccount class
                    Intent backToRegister = new Intent(Login.this,CreateAccount.class);
                    backToRegister.putExtra("type",typeUser);
                    startActivity(backToRegister);
                }
                else{
                    //Navigate to Main
                    Intent backToMain = new Intent(Login.this,MainActivity.class);
                    startActivity(backToMain);
                }

            }
        });
        //BACKEND
    }


}