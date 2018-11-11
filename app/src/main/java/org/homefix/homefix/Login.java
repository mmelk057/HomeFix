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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    private DatabaseReference dr;
    private EditText username;
    private EditText password;
    private String type = "";
    private FirebaseAuth mAuth; // Authorization HERE


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //Set up Login Button
        Button loginButton = findViewById(R.id.createaccountbutton);

        // DATABASE
        mAuth = FirebaseAuth.getInstance();
        Button loginButton = findViewById(R.id.editServiceSaveButton);
        ImageButton backButton = findViewById(R.id.loginBackButton);
        dr = FirebaseDatabase.getInstance().getReference("User");

        // DATABASE


        // BACKEND
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Take the String object from the "username" textbox - make sure it's an email
                //Take the String object from the "password" textbox - make sure it's not null

                username = (EditText)findViewById(R.id.editText3);
                password = (EditText)findViewById(R.id.editText4);
                Intent previousIntent = getIntent();
                System.out.println(previousIntent.getStringExtra("type"));
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

                EditText username = (EditText)findViewById(R.id.editText3);
                EditText password = (EditText)findViewById(R.id.editText4);

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

                            if (type.equals("HomeOwner") || type.equals("ServiceProvider")) {
                                authenticate(true);
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(type) // DISPLAY NAME IS TYPE
                                        .build();

                            }
                            else{

                                authenticate(false);


                            }
                        }
                    }
                }

            }
        });
        //BACKEND
    }
//    private void addUserToDatabase(String type) {
//
//        String id = dr.push().getKey();
//        String email = username.getText().toString().trim();
//        User user = new User(email, type);
//        if (type.equals("HomeOwner") || type.equals("ServiceProvider")) {
//            dr.child("ListOfUsers").child(id).setValue(user);
//            Toast.makeText(this, "User added", Toast.LENGTH_LONG).show();
//        }
//        else if (type.equals("Admin")){
//            dr.child("Admin").child(id).setValue(user);
//            Toast.makeText(this, "Admin added", Toast.LENGTH_LONG).show();
//        }
//    }


    /*
     * TODO updateUI method --> should make automatic login
     *
     */
    public void updateUI(FirebaseUser user){
        if(user != null && type.equals("Admin")){
            // IF ADMIN
            Intent toWelcomeAdmin = new Intent(Login.this, WelcomeAdmin.class);
            toWelcomeAdmin.putExtra("user", username.getText().toString());
            try {
                startActivity(toWelcomeAdmin);
            } catch (Exception e) {
                System.out.println("Error Starting Activity: " + e.getMessage() + "\n" + e.getStackTrace());

            }
        } else if(user != null && type.equals("")){
            // IF USER
            Intent toWelcomeUser = new Intent(Login.this, Welcome.class);
            toWelcomeUser.putExtra("user", username.getText().toString());
            try {
                startActivity(toWelcomeUser);
            } catch (Exception e) {
                System.out.println("Error Starting Activity: " + e.getMessage() + "\n" + e.getStackTrace());
            }

        }
        else {
            username.setText("");
            password.setText("");
        }
                                Intent toWelcomeAdmin = new Intent(Login.this, AdminOptions.class);
                                toWelcomeAdmin.putExtra("user", username.getText().toString());
                                addUser(type);
                                try {
                                    startActivity(toWelcomeAdmin);
                                } catch (Exception e) {
                                    System.out.println("Error Starting Activity: " + e.getMessage() + "\n" + e.getStackTrace());

                                }
                            }
                        }
                    }
                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToMain = new Intent(Login.this,MainActivity.class);
                startActivity(backToMain);
            }
        });
    }


    private boolean createNewUser(){
        String email = username.getText().toString().trim();
        String _password = password.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(email, _password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("login.java", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            //addUserToDatabase(type); // ADDING TO DB Separately
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("login.java", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Creation failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);

                        }

                    }
                });
        return true;
    }



    /*
     * Authenticates user based on fireAuth
     * Accepts String, String, Boolean
     * Returns True on success
     */
    private boolean authenticate(boolean shouldCreateNewOnNotExist){
        if(shouldCreateNewOnNotExist){
            createNewUser();
        }
        String email = username.getText().toString().trim();
        String _password = password.getText().toString().trim();
        mAuth.signInWithEmailAndPassword(email, _password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("login.java", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("login.java", "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });

        return true;
    }


}