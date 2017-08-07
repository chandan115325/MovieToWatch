package com.chandan.android.movietowatch.utils;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class JSONFunctions_post {
    float new_rating;
    int user_count;

    public JSONFunctions_post(float rating, int user_count) {
        this.new_rating = rating;
        this.user_count = user_count;
    }

    public void postJSON_toURL(String url) {


        StringBuilder sb = new StringBuilder();

        try {
            URL mUrl = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) mUrl.openConnection();

            httpURLConnection.setDoInput(true);

            httpURLConnection.addRequestProperty("Accept", "application/json");
            httpURLConnection.addRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestMethod("POST");



            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("vote_average", new_rating);
            jsonParam.put("vote_count", user_count);

            bufferedWriter.write(jsonParam.toString());
            bufferedWriter.close();


            BufferedReader serverAnswer = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            while ((line = serverAnswer.readLine()) != null) {

                System.out.println("LINE: " + line); //<--If any response from server
                //use it as you need, if server send something back you will get it here.
            }

            os.close();
            serverAnswer.close();

        } catch (Exception e) {
            Log.e("Oops", "Something went wrong");
        }
    }
}
