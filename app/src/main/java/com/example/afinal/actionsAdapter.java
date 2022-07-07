package com.example.afinal;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class actionsAdapter extends ArrayAdapter<action> {

    public actionsAdapter(Context context, ArrayList<action> Arractions) {
        super(context, 0, Arractions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        action action = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_actions_adapters, parent, false);
        }
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat month= new SimpleDateFormat("MMM");
        SimpleDateFormat day= new SimpleDateFormat("dd");
        TextView adapterDate=(TextView) convertView.findViewById(R.id.date_of_action);
        TextView adapterTitle = (TextView) convertView.findViewById(R.id.title_of_action);
        TextView adapterSum=(TextView) convertView.findViewById(R.id.sum_of_action);
        try {
            String monthStr = month.format(inputFormat.parse(action.getCreatedAt()));
            String dayStr = day.format(inputFormat.parse(action.getCreatedAt()));

            adapterDate.setText(dayStr+"\n"+monthStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionDetailsFragment actionDetails=new actionDetailsFragment();
                FragmentActivity activity = (FragmentActivity)(getContext());
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle b = new Bundle();
                b.putString("title", action.getTitle());
                b.putString("date", action.getCreatedAt());
                b.putString("details", action.getDetails());
                b.putDouble("sum", action.getSum());
                actionDetails.setArguments(b);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.dashboardContainer, actionDetails).commit();
            }
        });

        Double sum=action.getSum();
        DecimalFormat twoPlaces = new DecimalFormat("0.00");
        String moneyString = twoPlaces.format(sum);
        adapterTitle.setText(action.getTitle());
        adapterSum.setText(moneyString+"₪");
        if(sum>0.0){
            adapterSum.setTextColor(Color.parseColor("#08A045"));
        }
        else{
            adapterSum.setTextColor(Color.parseColor("#B02E0C"));
        }


        return convertView;
    }
}