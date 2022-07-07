package com.example.afinal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link dashboardContainerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class dashboardContainerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public dashboardContainerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment dashboardContainerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static dashboardContainerFragment newInstance(String param1, String param2) {
        dashboardContainerFragment fragment = new dashboardContainerFragment();
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
    String firstName;
    String lastName;
    String mobile;
    Double balance;
    JSONArray actions;
    actionsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            SharedPreferences settings=getActivity().getSharedPreferences("user_Info", Context.MODE_PRIVATE);
            String token= settings.getString("token","");
            String dataRes=InternetManager.callGetUserData(token);

            JSONObject dataResJson=new JSONObject(dataRes);
            this.firstName= dataResJson.getString("firstName");
            this.lastName= dataResJson.getString("lastName");
            this.mobile=dataResJson.getString("mobile");
            this.balance=dataResJson.getDouble("balance");
            this.actions=dataResJson.getJSONArray("actions");
            user.firstName=firstName;
            user.lastName=lastName;
            user.mobile=mobile;
            user.balance=balance;
            user.actions=actions;

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        View view=inflater.inflate(R.layout.fragment_dashboard_container, container, false);
        TextView depositBtn=view.findViewById(R.id.depositBtn);
        depositBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addActionFragment addActionFragment=new addActionFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle b = new Bundle();
                b.putString("actionType", "Deposit");
                addActionFragment.setArguments(b);
                fragmentTransaction.setReorderingAllowed(true);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.dashboardContainer, addActionFragment).commit();
            }
        });
        TextView withdrawBtn=view.findViewById(R.id.withdrawBtn);
        withdrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addActionFragment addActionFragment=new addActionFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle b = new Bundle();
                b.putString("actionType", "Withdraw");
                addActionFragment.setArguments(b);
                fragmentTransaction.setReorderingAllowed(true);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.dashboardContainer, addActionFragment).commit();
            }
        });




        TextView bl=view.findViewById(R.id.bl);
        DecimalFormat twoPlaces = new DecimalFormat("0.00");

        String moneyString = twoPlaces.format(balance);
        bl.setText(moneyString+"₪");

        ListView listView=view.findViewById(R.id.listView);
        ArrayList<action> arrayOfActions = user.actionsFromJson(actions);
        if(adapter!=null){
            adapter.clear();
        }
        adapter = new actionsAdapter(this.getContext(), arrayOfActions);

        listView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return view;
    }
}