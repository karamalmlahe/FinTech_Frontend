package com.example.afinal;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link actionDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class actionDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public actionDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment actionDetailstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static actionDetailsFragment newInstance(String param1, String param2) {
        actionDetailsFragment fragment = new actionDetailsFragment();
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
        View view= inflater.inflate(R.layout.fragment_action_details, container, false);
        Bundle bundle = getArguments();

        String title = bundle.getString("title");
        String date = bundle.getString("date");
        String details = bundle.getString("details");
        Double sum = bundle.getDouble("sum");

        TextView title_details=view.findViewById(R.id.title_details);
        TextView date_details=view.findViewById(R.id.date_details);
        TextView sum_details=view.findViewById(R.id.sum_details);
        TextView p_details=view.findViewById(R.id.p_details);
        LinearLayout layout_color =view.findViewById(R.id.layout_color);
        if(sum<0){
            layout_color.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.layout_bg_red));
        }
        else{
            layout_color.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.layout_bg));
        }

        DecimalFormat twoPlaces = new DecimalFormat("0.00");
        String moneyString = twoPlaces.format(sum);
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat dd = new SimpleDateFormat("dd-MM-yyyy");
        try {
            String datee = dd.format(inputFormat.parse(date));
            date_details.setText(datee);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        title_details.setText(title);

        p_details.setText("Details : "+details);
        sum_details.setText(moneyString+"₪");

        return view;
    }
}