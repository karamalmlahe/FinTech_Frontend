package com.example.afinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link settingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class settingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public settingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment settingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static settingsFragment newInstance(String param1, String param2) {
        settingsFragment fragment = new settingsFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_settings, container, false);

        TextInputLayout layout_firstName_settings=view.findViewById(R.id.layout_firstName_settings);
        TextInputLayout layout_lastName_settings =view.findViewById(R.id.layout_lastName_settings);
        TextInputEditText ed_firstName_settings =view.findViewById(R.id.ed_firstName_settings);
        TextInputEditText ed_lastName_settings =view.findViewById(R.id.ed_lastName_settings);

        LinearLayout container_settings =view.findViewById(R.id.container_settings);
        container_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
            }
        });

        ed_firstName_settings.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(layout_firstName_settings.isErrorEnabled()){
                    layout_firstName_settings.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        ed_lastName_settings.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(layout_lastName_settings.isErrorEnabled()){
                    layout_lastName_settings.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        TextView update_account_settings =view.findViewById(R.id.update_account_settings);
        update_account_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                String firstName=ed_firstName_settings.getText().toString();
                String lastName = ed_lastName_settings.getText().toString();
                if(firstName.equals(""))
                {
                    layout_firstName_settings.setError(" ");
                }
                if(lastName.equals("")){
                    layout_lastName_settings.setError(" ");
                }
                if(firstName.equals("") ||lastName.equals("")){
                    return;
                }

                SharedPreferences settings=getActivity().getSharedPreferences("user_Info", Context.MODE_PRIVATE);
                String token= settings.getString("token","");

                try {
                    String res=InternetManager.callUpdateAccount(token,firstName,lastName);
                    JSONObject resJSON=new JSONObject(res);
                    if(resJSON.getBoolean("status")==true){
                        user.firstName=firstName;
                        user.lastName=lastName;
                        Toast.makeText(getContext(), "succeed", Toast.LENGTH_SHORT).show();
                        ed_firstName_settings.setText("");
                        ed_lastName_settings.setText("");

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

        TextView logout =view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences settings=getActivity().getSharedPreferences("user_Info", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.remove("token");
                editor.apply();

                Intent LoginScreen=new Intent(getActivity(),Login.class);
                startActivity(LoginScreen);
                getActivity().finish();

            }
        });


        return view;
    }

    private void hideKeyboard(){
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)(getActivity().getSystemService(Context.INPUT_METHOD_SERVICE));
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}