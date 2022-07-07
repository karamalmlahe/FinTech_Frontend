package com.example.afinal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class action {
    private String title;
    private String details;
    private Double sum;
    private String  createdAt;

    public action(String title, String details, Double sum, String createdAt) {
        this.title = title;
        this.details = details;
        this.sum = sum;
        this.createdAt = createdAt;
    }
    public action(JSONObject ac){
        try {
            this.title=ac.getString("title");
            this.details=ac.getString("details");
            this.sum=ac.getDouble("sum");
            this.createdAt=ac.getString("createdAt");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public Double getSum() {
        return sum;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
