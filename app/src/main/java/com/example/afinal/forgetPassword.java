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
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class forgetPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getSupportActionBar().hide();
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        LinearLayout forgot_pass_con =findViewById(R.id.forgot_pass_con);
        forgot_pass_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
            }
        });

        TextInputLayout layout_mobile_forgetPassword =findViewById(R.id.layout_mobile_forgetPassword);
        TextInputEditText ed_mobile_forgetPassword= findViewById(R.id.ed_mobile_forgetPassword);

        ed_mobile_forgetPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(layout_mobile_forgetPassword.isErrorEnabled()){
                    layout_mobile_forgetPassword.setErrorEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        TextView sendCodeBtn=findViewById(R.id.sendCodeBtn);
        sendCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobile =ed_mobile_forgetPassword.getText().toString();
                if(mobile.equals("")){
                    layout_mobile_forgetPassword.setError("Please fill the gaps");
                    return;
                }
                else if(mobile.length()!=10||!mobile.substring(0,2).equals("05")) {
                    layout_mobile_forgetPassword.setError("Please enter a valid number");
                    return;
                }

                try {
                    String res = InternetManager.callforgotPassword("+972"+mobile);
                    if(res.equals(null)){
                        return;
                    }
                    JSONObject resJSON= new JSONObject(res);
                    if(resJSON.getBoolean("status")==true){
                        Intent PasscodeScreen=new Intent(forgetPassword.this,Passcode.class);
                        PasscodeScreen.putExtra("mobile", "+972"+mobile);
                        PasscodeScreen.putExtra("forLogin", false);
                        startActivity(PasscodeScreen);
                        finish();
                    }
                    else if(resJSON.getBoolean("status")==false && resJSON.getString("message").equals("User not exist")){
                        layout_mobile_forgetPassword.setError("This number is not exists");
                    }
                    else{
                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(forgetPassword.this);
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
}