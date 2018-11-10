package org.homefix.homefix;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class CreateAccount extends AppCompatActivity {
    private DatabaseReference dr;
    private EditText username;
    private EditText password;
    private EditText confirmpassword;
    private String type = "";
    private FirebaseAuth mAuth; // Authorization HERE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createaccount);
        //Set up Login Button
        Button loginButton = findViewById(R.id.createaccountbutton);
        Button signinButton = findViewById(R.id.signinbutton);

        // DATABASE
        mAuth = FirebaseAuth.getInstance();
        dr = FirebaseDatabase.getInstance().getReference("User");

        // DATABASE

        // BACKEND
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Take the String object from the "username" textbox - make sure it's an email
                //Take the String object from the "password" textbox - make sure it's not null

                username = (EditText) findViewById(R.id.usernamebox);
                password = (EditText) findViewById(R.id.passwordbox);
                confirmpassword = (EditText) findViewById(R.id.confirmpasswordbox);

                Intent previousIntent = getIntent();
                System.out.println(previousIntent.getStringExtra("type"));
                if (previousIntent.getStringExtra("type").equals("homeOwner")) {
                    //Database Verification Step Required
                    //Home Owner
                    type = "HomeOwner";
                } else {
                    //Service Provider
                    //Database Verification Step Required
                    type = "ServiceProvider";
                }

                EditText username = (EditText) findViewById(R.id.usernamebox);
                EditText password = (EditText) findViewById(R.id.passwordbox);
                EditText confirmpassword = (EditText) findViewById(R.id.confirmpasswordbox);

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
                        boolean e = validatePassword();
                        if (areEmailSectionsValid && e) {
                            authenticate(true);
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(type) // DISPLAY NAME IS TYPE
                                    .build();
                    }
                }

            }
        }
        //BACKEND

    });

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLogin = new Intent(CreateAccount.this,Login.class);
                toLogin.putExtra("type",type);
                startActivity(toLogin);
            }
        });

    }

    private boolean validatePassword(){

       String p1 = password.getText().toString().trim();
       String p2 = confirmpassword.getText().toString().trim();
       if(!p1.equals(p2)){
           Toast.makeText(this, "Passwords entered do not match!", Toast.LENGTH_LONG);
           return false;
       }
       return true;
    }



    private void addUserToDatabase(String type) {

        String id = dr.push().getKey();
        String email = username.getText().toString().trim();
        User user = new User(email, type);
        dr.child("ListOfUsers").child(id).setValue(user);
        Toast.makeText(this, "User added", Toast.LENGTH_LONG).show();
    }


    /*
     * TODO updateUI method --> should make automatic login
     *
     */
    public void updateUI(FirebaseUser user) {
         if (user != null && type.equals("")) {
            // IF USER
            Intent toWelcomeUser = new Intent(CreateAccount.this, Welcome.class);
            toWelcomeUser.putExtra("user", username.getText().toString());
            try {
                startActivity(toWelcomeUser);
            } catch (Exception e) {
                System.out.println("Error Starting Activity: " + e.getMessage() + "\n" + e.getStackTrace());
            }

        } else {
            username.setText("");
            password.setText("");
        }

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
                            addUserToDatabase(type); // ADDING TO DB Separately
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("login.java", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccount.this, "Creation failed.",
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
                                Log.d("createaccount.java", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("createaccount.java", "signInWithEmail:failure", task.getException());
                                Toast.makeText(CreateAccount.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });

            return true;
        }

}