package com.example.afinal;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        LinearLayout Container =findViewById(R.id.login_container);
        Container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
            }
        });

        TextView forgotPassBtn=findViewById(R.id.forgot_pass_btn);
        forgotPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgetPassword=new Intent(Login.this,forgetPassword.class);
                startActivity(forgetPassword);
            }
        });

        TextView to_register=findViewById(R.id.to_register);
        to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerScreen=new Intent(Login.this,register.class);
                startActivity(registerScreen);
            }
        });

        TextView SignInBtn =findViewById(R.id.sign_in_btn);
        SignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn();

            }
        });
    }
    private  void SignIn (){ ;

        TextInputEditText edMobile=findViewById(R.id.ed_mobile);
        TextInputEditText edPassword=findViewById(R.id.ed_password);
        TextInputLayout layoutPassword=findViewById(R.id.layout_password);
        TextInputLayout layoutMobile=findViewById(R.id.layout_mobile);

        edMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(layoutMobile.isErrorEnabled()){
                    layoutMobile.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        edPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(layoutPassword.isErrorEnabled()){
                    layoutPassword.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        String mobile="+972"+edMobile.getText().toString();
        String Password = edPassword.getText().toString();
        hideKeyboard();
        TextView btnLogin=findViewById(R.id.sign_in_btn);
        try {
            String loginResult=InternetManager.calllogin(mobile,Password);
            JSONObject loginResultJSON = new JSONObject(loginResult);
            if(loginResultJSON.get("status").equals(false)){

                String Message =loginResultJSON.get("message").toString();
                if(Message.equals("User not found")){
                    edMobile.setText("");
                    edPassword.setText("");
                    layoutMobile.setError(" ");
                }
                else if(Message.equals("Your password is not match")){
                    layoutMobile.setErrorEnabled(false);

                    edPassword.setText("");
                    layoutPassword.setError(" ");
                }
                else if(Message.equals("Your account is not active")){
                    Intent PasscodeScreen=new Intent(this,Passcode.class);
                    PasscodeScreen.putExtra("mobile", mobile);
                    PasscodeScreen.putExtra("forLogin", true);
                    startActivity(PasscodeScreen);
                }
                else{
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(Login.this);
                    dlgAlert.setMessage(loginResultJSON.get("message").toString());
                    dlgAlert.setTitle("FinTech");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                }
            }
            else {
                SharedPreferences settings=getSharedPreferences("user_Info",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor= settings.edit();
                editor.putString("token",loginResultJSON.get("token").toString());
                editor.apply();
                Intent dashboardScreen=new Intent(this, Home.class);
//                dashboardScreen.putExtra("token",loginResultJSON.get("token").toString());
                startActivity(dashboardScreen);
                finish();
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
