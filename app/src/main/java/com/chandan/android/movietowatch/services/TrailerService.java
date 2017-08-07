package com.chandan.android.movietowatch.services;

/**
 * Created by CHANDAN on 8/6/2017.
 */

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.chandan.android.movietowatch.model.Trailer;
import com.chandan.android.movietowatch.utils.HttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by CHANDAN on 8/3/2017.
 */

public class TrailerService extends IntentService {

    private String id;
    private String iso6391;
    private String iso31661;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;

    private ArrayList<Trailer> arrayTrailerList = new ArrayList<>();

    public static final String TAG = "MyService";
    public static final String MY_SERVICE_TRAILER = "myServiceTrailer";
    public static final String MY_SERVICE_PAYLOAD_TRAILER = "myServicePayloadTrailer";


    public TrailerService() {
        super("MyServicesTrailer");
    }

    @Override
    protected void onHandleIntent( Intent intent) {
        Uri uri = intent.getData();
        Log.i(TAG, "onHandleIntent: "+uri.toString());

        String response;
        try{
            response = HttpHelper.downloadUrl(uri.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(response);
            Log.i(TAG,"response:" +response);
            JSONArray itemsArray = jsonObject.getJSONArray("results");

            HashMap<String, String> hashMap = new HashMap<>();
            //processing JSON data
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject trailer = itemsArray.getJSONObject(i);

                key = trailer.getString("key");
                name = trailer.getString("name");


                arrayTrailerList.add(new Trailer(key, name));

                /*JSONObject id = movie.getJSONObject("id");
                JSONObject id = movie.getJSONObject("id");
                JSONObject id = movie.getJSONObject("id");
                JSONObject id = movie.getJSONObject("id");
                */
               /* jsonObject = itemsArray.getJSONObject(i);
                if (jsonObject.has("id")) {
                    hashMap.put("id", jsonObject.getString("id"));
                } else {
                    hashMap.put("id", "id NA");
                }
                if (jsonObject.has("original_title")) {
                    hashMap.put("original_title", jsonObject.getString("original_title"));
                } else {
                    hashMap.put("original_title", "Title NA");
                }
                if (jsonObject.has("release_date")) {
                    hashMap.put("release_date", jsonObject.getString("release_date"));
                } else {
                    hashMap.put("release_date", "Date NA");
                }

                if (jsonObject.has("popularity")) {
                    hashMap.put("popularity", jsonObject.getString("popularity"));
                } else {
                    hashMap.put("popularity", "Popularity NA");
                }

                if (jsonObject.has("vote_count")) {
                    hashMap.put("vote_count", jsonObject.getString("vote_count"));
                } else {
                    hashMap.put("vote_count", "Vote NA");
                }

                if (jsonObject.has("vote_average")) {
                    hashMap.put("vote_average", jsonObject.getString("vote_average"));
                } else {
                    hashMap.put("vote_average", "Average Vote NA");
                }

                if (jsonObject.has("poster_path")) {
                    hashMap.put("poster_path", "http://image.tmdb.org/t/p/original" +
                            jsonObject.getString("poster_path"));
                } else {
                    hashMap.put("poster_path", "Poster NA");
                }
               */
                //arrayMovieList.add(hashMap);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Intent messageIntent = new Intent(MY_SERVICE_TRAILER);
        messageIntent.putExtra(MY_SERVICE_PAYLOAD_TRAILER, arrayTrailerList);


        LocalBroadcastManager manager =
                LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
}
