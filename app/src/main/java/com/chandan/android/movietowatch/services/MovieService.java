package com.chandan.android.movietowatch.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.chandan.android.movietowatch.model.Movie;
import com.chandan.android.movietowatch.utils.HttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by CHANDAN on 8/3/2017.
 */

public class MovieService extends IntentService {
    private int voteCount;
    private int id;
    private boolean video;
    private double voteAverage;
    private String title;
    private double popularity;
    private String posterPath;
    private String originalLanguage;
    private String originalTitle;
    private List<Integer> genreIds = null;
    private String backdropPath;
    private boolean adult;
    private String overview;
    private String releaseDate;


    private ArrayList<Movie> arrayMovieList = new ArrayList<>();

    public static final String TAG = "MyService";
    public static final String MY_SERVICE_MESSAGE = "myServiceMessage";
    public static final String MY_SERVICE_PAYLOAD = "myServicePayload";


    public MovieService() {
        super("MyServices");
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
            JSONArray itemsArray = jsonObject.getJSONArray("results");

            HashMap<String, String> hashMap = new HashMap<>();
            //processing JSON data
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject movie = itemsArray.getJSONObject(i);

                 id = movie.getInt("id");
                 title = movie.getString("title");
                 originalTitle = movie.getString("original_title");
                 releaseDate = movie.getString("release_date");
                 popularity = movie.getDouble("popularity");
                 voteAverage = movie.getDouble("vote_average");
                 voteCount = movie.getInt("vote_count");
                 posterPath = movie.getString("poster_path");
                 video = movie.getBoolean("video");
                 overview = movie.getString("overview");
                 backdropPath = movie.getString("backdrop_path");

                arrayMovieList.add(new Movie(title, voteCount,  id,  video, voteAverage,  popularity, posterPath,
                         originalTitle,  backdropPath,  overview,  releaseDate));

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


        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
        messageIntent.putExtra(MY_SERVICE_PAYLOAD, arrayMovieList);


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
