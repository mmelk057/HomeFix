package org.homefix.homefix;

import java.util.ArrayList;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

import src.HomeOwner;
import src.ServiceProvider;

public class Service {
    private DatabaseReference dr;
    private ServiceProvider serviceProv;
    private ArrayList<HomeOwner> homeOwners;
    private String serviceName;
    private String info;
    private boolean approved;
    private boolean isActive;
    private boolean isAvailable;
    private EditText name;
    private EditText rate;
    private EditText details;

    public Service(String serviceName,String info,boolean approved){
        this.serviceName = serviceName;
        this.info = info;
        this.approved=approved;
    }
    public Service(String serviceName,String info) {
        this.serviceName = serviceName;
        this.info = info;
        this.approved = false;
    }


    public boolean containsHomeOwner(HomeOwner ho){
        return true; //temporary
    }

    public boolean addHomeOwner(HomeOwner ho){
        return true; //temporary
    }

    public boolean removeHomeOwner(HomeOwner ho){
        return true; //temporary
    }

    public boolean deleteService(){
        return true; //temporary
    }

    public boolean isServiceAvailable(){
        return true; //temporary
    }

    public ServiceProvider getServiceProvider(){
        return serviceProv;
    }

    public String getServiceName(){
        return serviceName;
    }

    public void setServiceName(String newName){
        serviceName=newName;
    }

    public String getInfo(){
        return info;
    }

    public void setInfo(String newInfo){
        info = newInfo;
    }
}
