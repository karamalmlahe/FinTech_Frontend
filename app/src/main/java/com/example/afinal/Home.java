package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Home extends AppCompatActivity {
    private String token;
    private static FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(Home.this,R.color.black));
        SharedPreferences settings=getSharedPreferences("user_Info", Context.MODE_PRIVATE);
        token= settings.getString("token","");
        try {
            setContentView(R.layout.loading);
            String dataRes=InternetManager.callGetUserData(token);

            if(dataRes.equals("Forbidden")){
                Intent loginScreen=new Intent(this,Login.class);
                startActivity(loginScreen);
                finish();
            }
            else{
                JSONObject dataResJson=new JSONObject(dataRes);
                setContentView(R.layout.activity_home);
                String firstName= dataResJson.getString("firstName");
                String lastName= dataResJson.getString("lastName");
                String mobile=dataResJson.getString("mobile");
                Double balance=dataResJson.getDouble("balance");
                JSONArray actions=dataResJson.getJSONArray("actions");
                user.getInstnace(firstName,lastName,mobile,balance,actions);

                DashboardFragment dashboardFragment=new DashboardFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.homeContainer, dashboardFragment).commit();

                BottomNavigationView  bottomNavigationView=findViewById(R.id.bottomNavigationView);

                bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        switch (item.getItemId()) {
                            case R.id.navigation_dashboard:
                                DashboardFragment dashboardFragment=new DashboardFragment();
                                Bundle b = new Bundle();
                                b.putString("firstName", firstName);
                                b.putString("lastName", lastName);
                                dashboardFragment.setArguments(b);
                                fragmentTransaction.replace(R.id.homeContainer, dashboardFragment).commit();
                                window.setStatusBarColor(ContextCompat.getColor(Home.this,R.color.black));
                                return true;
                            case R.id.navigation_settings:

                                settingsFragment settingsFragment=new settingsFragment();
                                fragmentTransaction.replace(R.id.homeContainer, settingsFragment).commit();
                                window.setStatusBarColor(ContextCompat.getColor(Home.this,R.color.silver));
                                return true;
                        }
                        return false;
                    }
                });


            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}