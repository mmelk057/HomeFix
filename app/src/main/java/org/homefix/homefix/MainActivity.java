package org.homefix.homefix;

import android.content.Intent;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logopage);

        //instantiating all buttons
        Button adminButton =(Button)findViewById(R.id.adminRadio);
        Button homeOwnerButton = (Button)findViewById(R.id.userRadio);
        Button spButton =(Button)findViewById(R.id.spRadio);

        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLogin = new Intent(MainActivity.this,Login.class); //Navigation tool to go from one activity to another. Params: {Activity from, Activity To}
                toLogin.putExtra("type","admin"); //Extra information to add during navigation from one activity to another
                startActivity(toLogin); //starting an intent!
            }
        });

        homeOwnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAccount = new Intent(MainActivity.this,CreateAccount.class);
                toAccount.putExtra("type","homeOwner");
                System.out.println(toAccount);
                startActivity(toAccount);
            }
        });

        spButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAccount = new Intent(MainActivity.this,CreateAccount.class);
                toAccount.putExtra("type","sp");
                startActivity(toAccount);
            }
        });
    }
}
