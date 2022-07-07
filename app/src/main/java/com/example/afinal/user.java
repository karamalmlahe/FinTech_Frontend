package com.example.afinal;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public  class user {

    private static user instnace ;
    public static user getInstnace(String firstName, String lastName, String mobile, Double balance, JSONArray actions) {
        if (instnace == null)
            instnace=new user(firstName,lastName,mobile,balance,actions);
        return instnace;
    }

    public static String firstName;
    public static String lastName;
    public static String mobile;
    public static Double balance;
    public static JSONArray actions;

    private user(String firstName, String lastName, String mobile, Double balance, JSONArray actions) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobile = mobile;
        this.balance = balance;
        this.actions = actions;
    }

    public static String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public static String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public static String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public static  Double getBalance() {
        return balance;
    }

    public  void setBalance(Double balance) {
        this.balance = balance;
    }

    public static JSONArray getActions() {
        return actions;
    }

    public  void setActions(JSONArray actions) {
        this.actions = actions;
    }

    public static ArrayList<action> actionsFromJson(JSONArray jsonObjects) {
        ArrayList<action> actions = new ArrayList<action>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                actions.add(new action(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.reverse(actions);
        return actions;
    }
}
