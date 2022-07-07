package com.example.afinal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Passcode extends AppCompatActivity {
    TextView resendBtn;
    String mobile;
    Boolean forLogin;
    EditText e1;
    EditText e2;
    EditText e3;
    EditText e4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);
        getSupportActionBar().hide();
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        Bundle bundle = getIntent().getExtras();
        mobile = bundle.getString("mobile");
        forLogin=bundle.getBoolean("forLogin");
        TextView mobileText=findViewById(R.id.mobileNumber);
        mobileText.setText(mobile);
        LinearLayout Container =findViewById(R.id.Passcode_container);
        resendPasscode();
        Container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
            }
        });


        e1=findViewById(R.id.passCodeEd1);
        e2=findViewById(R.id.passCodeEd2);
        e3=findViewById(R.id.passCodeEd3);
        e4=findViewById(R.id.passCodeEd4);
        e1.addTextChangedListener(new GenericTextWatcher(e2, e1));
        e2.addTextChangedListener(new GenericTextWatcher(e3, e1));
        e3.addTextChangedListener(new GenericTextWatcher(e4, e2));


        TextView confirmBtn=findViewById(R.id.confirm_btn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmPassCode();
            }
        });
        resendBtn=findViewById(R.id.resend_btn);
        resendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendPasscode();
            }
        });
    }


    private void sendPasscode(){
        try {
            String sendPasscodeResult = InternetManager.callSendPasscodeToMobile(mobile);
            try {
                JSONObject sendPasscodeResultJSON= new JSONObject(sendPasscodeResult);
                if(sendPasscodeResultJSON.getBoolean("status")==true){
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(Passcode.this);
                    dlgAlert.setMessage("Your passcode is : "+sendPasscodeResultJSON.get("passcode").toString());
                    dlgAlert.setTitle("FinTech");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
        e.printStackTrace();
        }
//        catch (JSONException e) {
//        e.printStackTrace();
//        }
    }
    private void resendPasscode(){
        sendPasscode();
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                resendBtn.setText(String.format("00:%02d",millisUntilFinished/1000));
                resendBtn.setEnabled(false);
            }

            public void onFinish() {
                resendBtn.setText("Resend");
                resendBtn.setEnabled(true);
            }
        }.start();
    }


    private void confirmPassCode(){
        TextView confirmBtn=findViewById(R.id.confirm_btn);
        TextView errorPasscode =findViewById(R.id.errorPasscode);
        String passcode=e1.getText().toString()+e2.getText().toString()+e3.getText().toString()+e4.getText().toString();
        if(passcode.length()!=4){
            errorPasscode.setText("Please fill in the blanks");
            return;
        }
        Integer passcodeNumber=Integer.parseInt(passcode);
        try {
            String confirmPasscode = InternetManager.callVerifyPasscode(mobile,passcodeNumber);
            System.out.println(confirmPasscode);
            JSONObject confirmPasscodeJSON = new JSONObject(confirmPasscode);
            Boolean status=confirmPasscodeJSON.getBoolean("status");
            if(status==false && confirmPasscodeJSON.getString("message").equals("Passcode not match")){

                errorPasscode.setText("Sorry, the code was entered incorrectly");
            }
            else if( status==true){
                if(forLogin){
                    SharedPreferences settings=getSharedPreferences("user_Info",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor= settings.edit();
                    editor.putString("token",confirmPasscodeJSON.get("token").toString());
                    editor.apply();
                    Intent dashboardScreen=new Intent(this, Home.class);
                    startActivity(dashboardScreen);
                }
                else{
                    Intent updatePassword=new Intent(this, updatePassword.class);
                    updatePassword.putExtra("mobile",mobile);
                    startActivity(updatePassword);
                }

                finish();
            }
            else{
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(Passcode.this);
                dlgAlert.setMessage(confirmPasscodeJSON.get("message").toString());
                dlgAlert.setTitle("FinTech");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            }
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
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