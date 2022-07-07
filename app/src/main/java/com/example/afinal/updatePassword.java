package com.example.afinal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
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

public class updatePassword extends AppCompatActivity {
    String mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        getSupportActionBar().hide();
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        Bundle bundle = getIntent().getExtras();
        mobile = bundle.getString("mobile");

        LinearLayout update_pass_con =findViewById(R.id.update_pass_con);
        update_pass_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
            }
        });

        TextInputLayout layout_password_update =findViewById(R.id.layout_password_update);
        TextInputLayout layout_repassword_update =findViewById(R.id.layout_repassword_update);

        TextInputEditText ed_password_update =findViewById(R.id.ed_password_update);
        TextInputEditText ed_repassword_update =findViewById(R.id.ed_repassword_update);

        onChangeText(layout_password_update,ed_password_update);
        onChangeText(layout_repassword_update,ed_repassword_update);

        TextView update_passBtn= findViewById(R.id.update_passBtn);
        update_passBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                String password=ed_password_update.getText().toString();
                String confirmPassword=ed_repassword_update.getText().toString();
                Boolean flag=true;
                if(password.equals("")){
                    layout_password_update.setError("Please fill the gaps");
                    flag=false;
                }
                if(confirmPassword.equals("")){
                    layout_repassword_update.setError("Please fill the gaps");
                    flag=false;
                }
                if(!confirmPassword.equals("")&&!password.equals("")&&!password.equals(confirmPassword)){
                    layout_password_update.setError("Passwords do not match!");
                    ed_repassword_update.setText("");
                    ed_password_update.setError("Passwords do not match!");
                    flag=false;
                }
                if(flag==false){
                    return;
                }
                try {
                    String res=InternetManager.callupdatePassword(mobile,password);

                    if(res.equals(null))
                    {
                        return;
                    }
                    JSONObject resJSON=new JSONObject(res);
                    if(resJSON.getBoolean("status")==true){
                        finish();
                    }
                    else if(resJSON.getBoolean("status")==false && resJSON.getString("message").equals("You are using the same old password")){
                        ed_password_update.setText("");
                        ed_repassword_update.setText("");
                        layout_password_update.setError("You are using the same old password");
                    }
                    else{
                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(updatePassword.this);
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

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}