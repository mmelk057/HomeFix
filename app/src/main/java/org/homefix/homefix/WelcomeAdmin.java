package org.homefix.homefix;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class WelcomeAdmin extends Activity {

    ListView l1;
    ArrayAdapter<String> adapter;
    String[] default_items = new String[]{"Username", "Email"};
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcomeadmin);
        l1 = (ListView)findViewById(R.id.listView);
        Intent previousIntent = getIntent();
        String username = previousIntent.getStringExtra("user");
        TextView welcomeUser =(TextView)findViewById(R.id.welcomeUser);
        welcomeUser.setText("Welcome,"+username);
    }

}
