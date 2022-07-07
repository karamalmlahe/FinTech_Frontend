package com.example.afinal;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class InternetManager {
    private  static String urlBase="https://fintech-apii.herokuapp.com";
    public static String calllogin(String mobile,String password) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return login(mobile,password);
            }
        });

        executorService.shutdown();
        return future.get();
    }

    private static String login(String mobile,String password) throws IOException {

        URL url = new URL(urlBase+"/api/accounts/login");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept","application/json");
        connection.setDoOutput(true);
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("mobile",mobile) ;
            jsonParam.put("password", password);
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonParam.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try
        {
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK)
            {
                return null;
            }
            String line;
            BufferedReader in = new BufferedReader( new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while( (line = in.readLine()) != null)
            {
                response.append(line);
            }
            in.close();
            return response.toString();
        }
        finally {
            connection.disconnect();
        }
    }

    public static String callSendPasscodeToMobile(String mobile) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return SendPasscodeToMobile(mobile);
            }
        });

        executorService.shutdown();
        return future.get();
    }

    private static String SendPasscodeToMobile(String mobile) throws IOException {

        URL url = new URL(urlBase+"/api/accounts/sendPasscode");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept","application/json");
        connection.setDoOutput(true);
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("mobile",mobile) ;
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonParam.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try
        {
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK)
            {
                return null;
            }
            String line;
            BufferedReader in = new BufferedReader( new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while( (line = in.readLine()) != null)
            {
                response.append(line);
            }
            in.close();
            return response.toString();
        }
        finally {
            connection.disconnect();
        }
    }


    public static String callVerifyPasscode(String mobile,Number passcode) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return verifyPasscode(mobile,passcode);
            }
        });

        executorService.shutdown();
        return future.get();
    }

    private static String verifyPasscode(String mobile,Number passcode) throws IOException {

        URL url = new URL(urlBase+"/api/accounts/verify");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept","application/json");
        connection.setDoOutput(true);
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("mobile",mobile) ;
            jsonParam.put("passcode",passcode) ;
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonParam.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try
        {
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK)
            {
                return null;
            }
            String line;
            BufferedReader in = new BufferedReader( new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while( (line = in.readLine()) != null)
            {
                response.append(line);
            }
            in.close();
            return response.toString();
        }
        finally {
            connection.disconnect();
        }
    }




    public static String callGetUserData(String token) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return GetUserData(token);
            }
        });

        executorService.shutdown();
        return future.get();
    }

    private static String GetUserData(String token) throws IOException {

        URL url = new URL(urlBase+"/api/accounts/getUserData");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(1000 * 2);
        connection.setReadTimeout(1000 * 5);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization","Bearer "+token);
        try
        {
             if(connection.getResponseCode()==HttpURLConnection.HTTP_FORBIDDEN){
                return"Forbidden";
            }
             else if(connection.getResponseCode() != HttpURLConnection.HTTP_OK)
             {
                 return null;
             }
            String line;
            BufferedReader in = new BufferedReader( new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while( (line = in.readLine()) != null)
            {
                response.append(line);
            }
            in.close();
            return response.toString();
        }
        finally {
            connection.disconnect();
        }
    }


    public static String callAddAction(String token,String title,String details,Double sum) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return addAction(token,title,details,sum);
            }
        });

        executorService.shutdown();
        return future.get();
    }

    private static String addAction(String token,String title,String details,Double sum) throws IOException {

        URL url = new URL(urlBase+"/api/accounts/addAction");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(1000 * 2);
        connection.setReadTimeout(1000 * 5);
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Authorization","Bearer "+token);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept","application/json");
        connection.setDoOutput(true);
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("title",title) ;
            jsonParam.put("details",details) ;
            jsonParam.put("sum",sum) ;
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonParam.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try
        {
            if(connection.getResponseCode()==HttpURLConnection.HTTP_FORBIDDEN){
                return"Forbidden";
            }
            else if(connection.getResponseCode() != HttpURLConnection.HTTP_OK)
            {
                return null;
            }
            String line;
            BufferedReader in = new BufferedReader( new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while( (line = in.readLine()) != null)
            {
                response.append(line);
            }
            in.close();
            return response.toString();
        }
        finally {
            connection.disconnect();
        }
    }


    public static String callUpdateAccount(String token,String firstName,String lastName) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return updateAccount(token,firstName,lastName);
            }
        });

        executorService.shutdown();
        return future.get();
    }

    private static String updateAccount(String token,String firstName,String lastName) throws IOException {

        URL url = new URL(urlBase+"/api/accounts/updateAccount");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(1000 * 2);
        connection.setReadTimeout(1000 * 5);
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Authorization","Bearer "+token);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept","application/json");
        connection.setDoOutput(true);
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("firstName",firstName) ;
            jsonParam.put("lastName",lastName) ;
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonParam.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try
        {
            if(connection.getResponseCode()==HttpURLConnection.HTTP_FORBIDDEN){
                return"Forbidden";
            }
            else if(connection.getResponseCode() != HttpURLConnection.HTTP_OK)
            {
                return null;
            }
            String line;
            BufferedReader in = new BufferedReader( new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while( (line = in.readLine()) != null)
            {
                response.append(line);
            }
            in.close();
            return response.toString();
        }
        finally {
            connection.disconnect();
        }
    }


    public static String callCreateAccount(String firstName,String lastName,String mobile,String password) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return updateCreateAccount(firstName,lastName,mobile,password);
            }
        });

        executorService.shutdown();
        return future.get();
    }

    private static String updateCreateAccount(String firstName,String lastName,String mobile,String password) throws IOException {

        URL url = new URL(urlBase+"/api/accounts/register");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(1000 * 2);
        connection.setReadTimeout(1000 * 5);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept","application/json");
        connection.setDoOutput(true);
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("firstName",firstName) ;
            jsonParam.put("lastName",lastName) ;
            jsonParam.put("mobile",mobile) ;
            jsonParam.put("password",password) ;
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonParam.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try
        {
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK)
            {
                return null;
            }
            String line;
            BufferedReader in = new BufferedReader( new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while( (line = in.readLine()) != null)
            {
                response.append(line);
            }
            in.close();
            return response.toString();
        }
        finally {
            connection.disconnect();
        }
    }

    public static String callforgotPassword(String mobile) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return forgotPassword(mobile);
            }
        });

        executorService.shutdown();
        return future.get();
    }

    private static String forgotPassword(String mobile) throws IOException {

        URL url = new URL(urlBase+"/api/accounts/forgotPassword");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(1000 * 2);
        connection.setReadTimeout(1000 * 5);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept","application/json");
        connection.setDoOutput(true);
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("mobile",mobile) ;
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonParam.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try
        {
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK)
            {
                return null;
            }
            String line;
            BufferedReader in = new BufferedReader( new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while( (line = in.readLine()) != null)
            {
                response.append(line);
            }
            in.close();
            return response.toString();
        }
        finally {
            connection.disconnect();
        }
    }


    public static String callupdatePassword(String mobile,String password) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return updatePassword(mobile,password);
            }
        });

        executorService.shutdown();
        return future.get();
    }

    private static String updatePassword(String mobile,String password) throws IOException {

        URL url = new URL(urlBase+"/api/accounts/updatePassword");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(1000 * 2);
        connection.setReadTimeout(1000 * 5);
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept","application/json");
        connection.setDoOutput(true);
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("mobile",mobile) ;
            jsonParam.put("newPassword",password) ;
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonParam.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try
        {
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK)
            {
                return null;
            }
            String line;
            BufferedReader in = new BufferedReader( new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while( (line = in.readLine()) != null)
            {
                response.append(line);
            }
            in.close();
            return response.toString();
        }
        finally {
            connection.disconnect();
        }
    }


}
