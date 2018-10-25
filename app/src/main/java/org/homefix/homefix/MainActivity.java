package org.homefix.homefix;

import android.content.Intent;
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
                Intent toLogin = new Intent(MainActivity.this,Login.class);
                toLogin.putExtra("type","admin");
                startActivity(toLogin);
            }
        });

        homeOwnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLogin = new Intent(MainActivity.this,Login.class);
                toLogin.putExtra("type","homeOwner");
                startActivity(toLogin);
            }
        });

        spButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLogin = new Intent(MainActivity.this,Login.class);
                toLogin.putExtra("type","sp");
                startActivity(toLogin);
            }
        });
    }
}
