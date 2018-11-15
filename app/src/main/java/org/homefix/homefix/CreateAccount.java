package org.homefix.homefix;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;


public class CreateAccount extends AppCompatActivity {
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createaccount);
        //Set up Login Button
        Button loginButton = findViewById(R.id.saveButton);
        Button signinButton = findViewById(R.id.signinbutton);
        ImageButton backButton = findViewById(R.id.createAccountBackButton);

        //Set Up Previous Intent
        final Intent previousIntent = getIntent();
        final String previousType = previousIntent.getStringExtra("type");

        // BACKEND
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Take the String object from the "username" textbox - make sure it's an email
                //Take the String object from the "password" textbox - make sure it's not null

                if (previousType.equals("homeOwner")) {
                    //Database Verification Step Required
                    //Home Owner
                    type = "HomeOwner";
                } else {
                    //Service Provider
                    //Database Verification Step Required
                    type = "ServiceProvider";
                }

                EditText username = findViewById(R.id.usernamebox);
                EditText password = findViewById(R.id.passwordbox);
                EditText confirmpassword = findViewById(R.id.confirmpasswordbox);

                //splitting the username into two fields
                String email = username.getText().toString();
                if (email.contains("@") && password.getText().length() != 0) {
                    String[] emailParts = email.split(Pattern.quote("@"));
                    if (emailParts.length == 2) {
                        boolean areEmailSectionsValid = true;
                        for (int i = 0; i < 2; i++) {
                            if (emailParts[i].equals("") || (emailParts[i].startsWith(".")) || (emailParts[i].endsWith("."))) {  //an email split by @ cannot have either of its sections start with or end with '.'
                                areEmailSectionsValid = false;
                                break;
                            } else if (emailParts[i].contains(".")) {
                                String[] emailPartsV2 = emailParts[i].split(Pattern.quote("."));
                                for (int j = 0; j < emailPartsV2.length; j++) {
                                    if (emailPartsV2[j].equals("")) {
                                        areEmailSectionsValid = false;
                                        break;
                                    }
                                }

                            }
                        }
                        boolean e = validatePassword(password.getText().toString(),confirmpassword.getText().toString());
                        if (areEmailSectionsValid && e) {
                            String email_final = username.getText().toString();
                            String password_final = password.getText().toString();

                            //Authenticate that user is not in database, then add user to Authentication table and to the database
                            DatabaseReference dr = FirebaseDatabase.getInstance().getReference("User");
                            Database creatingAccount = new Database(dr,CreateAccount.this,getApplicationContext());
                            creatingAccount.registerUser(email_final,password_final,type);

                        }
                    }

                }
            }
            //BACKEND

        });

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // CHANGING SCREEN
                Intent toLogin = new Intent(CreateAccount.this,Login.class);
                toLogin.putExtra("type",previousType);
                startActivity(toLogin);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to Main
                Intent toMain = new Intent(CreateAccount.this,MainActivity.class);
                startActivity(toMain);
            }
        });

    }

    private boolean validatePassword(String pass1, String pass2){
        if(!pass1.equals(pass2)){
            Toast.makeText(this, "Passwords entered do not match!", Toast.LENGTH_LONG);
            return false;
        }
        else{
            return true;
        }
    }

}