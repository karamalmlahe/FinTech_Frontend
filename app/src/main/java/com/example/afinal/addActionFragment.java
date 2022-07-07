package com.example.afinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addActionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addActionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public addActionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addActionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static addActionFragment newInstance(String param1, String param2) {
        addActionFragment fragment = new addActionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private  String actionType;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_add_action, container, false);
        Bundle bundle = getArguments();
        TextView actionTypeView=view.findViewById(R.id.actionType);

        TextView sendActionBtn =view.findViewById(R.id.sendActionBtn);

        EditText edTitle=view.findViewById(R.id.ed_title);
        TextInputEditText edSum=view.findViewById(R.id.ed_sum);
        TextInputEditText edDetails=view.findViewById(R.id.ed_details);



        TextInputLayout layoutTitle=view.findViewById(R.id.layout_title);
        TextInputLayout layoutSum=view.findViewById(R.id.layout_sum);
        TextInputLayout layoutDetails=view.findViewById(R.id.layout_details);

        actionType = bundle.getString("actionType");

        if(actionType.equals("Deposit")){
            actionTypeView.setText(actionType);
            actionTypeView.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.layout_bg));
        }
        else{
            actionTypeView.setText(actionType);
            actionTypeView.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.layout_bg_red));
        }



        edTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(layoutTitle.isErrorEnabled()){
                    layoutTitle.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        edSum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(layoutSum.isErrorEnabled()){
                    layoutSum.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        edDetails.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(layoutDetails.isErrorEnabled()){
                    layoutDetails.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        sendActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double sum=0.0;
                if(edTitle.getText().toString().equals("")){
                    layoutTitle.setError(" ");
                }
                if(edDetails.getText().toString().equals("")){
                    layoutDetails.setError(" ");
                }
                if(edSum.getText().toString().equals("")|| edSum.getText().toString().equals("0")){
                    layoutSum.setError(" ");
                }

                if(edTitle.getText().toString().equals("")||edDetails.getText().toString().equals("")||edSum.getText().toString().equals("")){
                    return;
                }

                if(actionType.equals("Deposit")){
                    sum=1.0;
                }
                else{
                    sum=-1.0;
                }


                sum*=Double.parseDouble(edSum.getText().toString());;
                if(sum<0 && user.getBalance()<sum*-1){
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getContext());
                    dlgAlert.setMessage("Sorry, You don't have that amount in your wallet");
                    dlgAlert.setTitle("FinTech");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                    return;
                }
                SharedPreferences settings=getActivity().getSharedPreferences("user_Info", Context.MODE_PRIVATE);
                String token= settings.getString("token","");

                try {
                    String res=InternetManager.callAddAction(token,edTitle.getText().toString(),edDetails.getText().toString(),sum);
                    JSONObject resJson=new JSONObject(res);
                    if(resJson.getBoolean("status")==true){
                        getFragmentManager().popBackStack();
                    }
                    else{
                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getContext());
                        dlgAlert.setMessage(resJson.get("message").toString());
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
        return view;
    }
    private void Confirm(View view){






    }
}