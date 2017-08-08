package com.chandan.android.movietowatch;

import android.os.AsyncTask;
import android.util.Log;

import com.chandan.android.movietowatch.utils.JSONdata;
import com.chandan.android.movietowatch.utils.POST_Rating;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class get_Sessionid extends AsyncTask<String, String, String> {
    String guest_session = "http://api.themoviedb.org/3/authentication/guest_session/new?api_key=8496be0b2149805afa458ab8ec27560c";
    JSONObject jsonObject_guest;
    String guest_id;
    float rating;
    int user_count,movie_id;
    BigDecimal vote;

    public get_Sessionid(int id, BigDecimal new_vote_average, int new_userCount) {
        this.vote = new_vote_average;
        this.movie_id = id;
        this.user_count = new_userCount;
    }

    @Override
    protected String doInBackground(String... strings) {
        rating = Float.valueOf(vote.toString());
        JSONdata jsoNdata = new JSONdata();
        jsonObject_guest = jsoNdata.getJSONFromURL(guest_session);
        if (jsonObject_guest != null && jsonObject_guest.has("guest_session_id")){
            try {
                guest_id = String.valueOf(jsonObject_guest.get("guest_session_id"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return guest_id;
    }

    @Override
    protected void onPostExecute(String Guest_id) {
        super.onPostExecute(Guest_id);
        Log.e("session id_guest id ", Guest_id);
        Log.e("Things passed", movie_id+", "+ rating+", "+ Guest_id+", " +user_count);
        POST_Rating post_rating = new POST_Rating(movie_id, rating, Guest_id, user_count);
        post_rating.execute();
    }
}
