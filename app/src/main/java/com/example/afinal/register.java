package com.example.afinal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));


        LinearLayout register_container =findViewById(R.id.register_container);
        register_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
            }
        });

        TextView to_login =findViewById(R.id.to_login);
        to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextInputLayout layout_firstName_register =findViewById(R.id.layout_firstName_register);
        TextInputLayout layout_lastName_register =findViewById(R.id.layout_lastName_register);
        TextInputLayout layout_mobile_register =findViewById(R.id.layout_mobile_register);
        TextInputLayout layout_password_register =findViewById(R.id.layout_password_register);
        TextInputLayout layout_repassword_register =findViewById(R.id.layout_repassword_register);

        TextInputEditText ed_firstName_register= findViewById(R.id.ed_firstName_register);
        TextInputEditText ed_lastName_register= findViewById(R.id.ed_lastName_register);
        TextInputEditText ed_mobile_register= findViewById(R.id.ed_mobile_register);
        TextInputEditText ed_password_register= findViewById(R.id.ed_password_register);
        TextInputEditText ed_repassword_register= findViewById(R.id.ed_repassword_register);

        onChangeText(layout_firstName_register,ed_firstName_register);
        onChangeText(layout_lastName_register,ed_lastName_register);
        onChangeText(layout_mobile_register,ed_mobile_register);
        onChangeText(layout_password_register,ed_password_register);
        onChangeText(layout_repassword_register,ed_repassword_register);

        TextView sendActionBtn= findViewById(R.id.sendActionBtn);
        sendActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean flag=true;
                String firstName=ed_firstName_register.getText().toString();
                String lastName=ed_lastName_register.getText().toString();
                String mobile=ed_mobile_register.getText().toString();
                String password=ed_password_register.getText().toString();
                String rePassword=ed_repassword_register.getText().toString();
                if(firstName.equals("")){
                    layout_firstName_register.setError("Please fill the gaps");
                    flag=false;
                }
                if(lastName.equals("")){
                    layout_lastName_register.setError("Please fill the gaps");
                    flag=false;
                }
                if(mobile.equals("")){
                    layout_mobile_register.setError("Please fill the gaps");
                    flag=false;
                }
                else if(mobile.length()!=10||!mobile.substring(0,2).equals("05")) {
                    layout_mobile_register.setError("Please enter a valid number");
                    flag = false;
                }
                if(password.equals("")){
                    layout_password_register.setError("Please fill the gaps");
                    flag=false;
                }
                if(rePassword.equals("")){
                    layout_repassword_register.setError("Please fill the gaps");
                    flag=false;
                }
                if(!rePassword.equals("")&&!password.equals("")&&!password.equals(rePassword)){
                    layout_password_register.setError("Passwords do not match!");
                    ed_repassword_register.setText("");
                    layout_repassword_register.setError("Passwords do not match!");
                    flag=false;
                }

                if(flag==false){
                    return;
                }
                try {
                    String res=InternetManager.callCreateAccount(firstName,lastName,"+972"+mobile,password);
                    JSONObject resJSON=new JSONObject(res);
                    if(resJSON.getBoolean("status")==true){
                        Intent PasscodeScreen=new Intent(register.this,Passcode.class);
                        PasscodeScreen.putExtra("mobile", "+972"+mobile);
                        PasscodeScreen.putExtra("forLogin", true);
                        startActivity(PasscodeScreen);
                        finish();
                    }
                    else if(resJSON.getBoolean("status")==false&&resJSON.getString("message").equals("User is already exist")){
                        ed_mobile_register.setText("");
                        layout_mobile_register.setError("This number already exists");
                    }
                    else {
                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(register.this);
                        dlgAlert.setMessage(resJSON.get("message").toString());
                        dlgAlert.setTitle("FinTech");
                        dlgAlert.setPositiveButton("OK", null);
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void onChangeText(TextInputLayout layout,TextInputEditText ed){
        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(layout.isErrorEnabled()){
                    layout.setErrorEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}