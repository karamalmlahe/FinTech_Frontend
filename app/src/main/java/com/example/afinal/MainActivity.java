package com.example.afinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings=getSharedPreferences("user_Info", Context.MODE_PRIVATE);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));
        String token= settings.getString("token","");
        if(!token.equals("")){
            Intent dashboardScreen=new Intent(this, Home.class);
            startActivity(dashboardScreen);
        }
        else{
            Intent LoginScreen=new Intent(this,Login.class);
            startActivity(LoginScreen);
        }
        finish();

    }
}