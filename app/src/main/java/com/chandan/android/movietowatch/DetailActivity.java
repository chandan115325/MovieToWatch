package com.chandan.android.movietowatch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chandan.android.movietowatch.adapter.CastAdapter;
import com.chandan.android.movietowatch.adapter.CrewAdapter;
import com.chandan.android.movietowatch.adapter.TrailerAdapter;
import com.chandan.android.movietowatch.model.Cast;
import com.chandan.android.movietowatch.model.Crew;
import com.chandan.android.movietowatch.model.Movie;
import com.chandan.android.movietowatch.model.Trailer;
import com.chandan.android.movietowatch.services.CastService;
import com.chandan.android.movietowatch.services.CrewService;
import com.chandan.android.movietowatch.services.TrailerService;
import com.chandan.android.movietowatch.utils.NetworkHelper;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by delaroy on 5/18/17.
 */
public class DetailActivity extends AppCompatActivity {

/*

    ImageView imageView_votes, imageView_moviePoster, imageView_favorites, imageView_watchlist;
    RatingBar ratingBar;
    HorizontalScrollView horizontalScrollView_posters, horizontalScrollView_trailers,
            horizontalScrollView_cast, horizontalScrollView_crew;
    LinearLayout linearLayout_posters, linearLayout_trailers, linearLayout_cast, linearLayout_crew;

    String URL_movieDetails, URL_moviePosters, URL_movieTrailers, URL_movieCastCrew;
    String id, imdb_id;

    ImageLoader imageLoader = ImageLoader.getInstance();

*/
    private Double voteAverage, updatedVoteAverage;
    private int voteCount, updatedVoteCount;
    private static final String JSON_URL_TRAILER = null;

    TextView nameOfMovie, plotSynopsis, userRating, releaseDate;
    ImageView imageView;
    private RecyclerView recyclerView;
    private boolean networkOk;

    private TrailerAdapter adapter;
    private ArrayList<Trailer> trailerList;

    private CastAdapter castAdapter;
    private ArrayList<Cast>castArrayList;

    private CrewAdapter crewAdapter;
    private ArrayList<Crew>crewArrayList;

    private RatingBar ratingBar;
    //private FavoriteDbHelper favoriteDbHelper;
   // private Movie favorite;*/
    private final AppCompatActivity activity = DetailActivity.this;

    Movie movie;
    String thumbnail, movieName, synopsis, rating, dateOfRelease;
    int movie_id;
//getting trailer arraylist
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            trailerList = intent.getParcelableArrayListExtra(TrailerService.MY_SERVICE_PAYLOAD_TRAILER);
            initViewsTrailer(trailerList);
        }
    };

//getting Cast arraylist
    private BroadcastReceiver castBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            castArrayList = intent.getParcelableArrayListExtra(CastService.MY_SERVICE_PAYLOAD_CAST);
            initViewsCast(castArrayList);
        }
    };

    //getting Crew arraylist
    private BroadcastReceiver crewBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            crewArrayList = intent.getParcelableArrayListExtra(CrewService.MY_SERVICE_PAYLOAD_CREW);
            initViewsCrew(crewArrayList);
        }
    };




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initCollapsingToolbar();

        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        imageView = (ImageView) findViewById(R.id.thumbnail_image_header);
        nameOfMovie = (TextView) findViewById(R.id.title);
        plotSynopsis = (TextView) findViewById(R.id.plotsynopsis);
        userRating = (TextView) findViewById(R.id.userrating);
        releaseDate = (TextView) findViewById(R.id.releasedate);

        //registering the Local broadcast receiver
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(TrailerService.MY_SERVICE_TRAILER));

        //registering the local broadcast eceiver for cast
                LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(castBroadcastReceiver,
                        new IntentFilter(CastService.MY_SERVICE_CAST));


        //registering the local broadcast receiver for cast
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(crewBroadcastReceiver,
                        new IntentFilter(CrewService.MY_SERVICE_CREW));


        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("movies")) {

            movie = getIntent().getParcelableExtra("movies");

            thumbnail = movie.getPosterPath();
            movieName = movie.getOriginalTitle();
            synopsis = movie.getOverview();
            rating = Double.toString(movie.getVoteAverage());
            voteCount = movie.getVoteCount();
            voteAverage = movie.getVoteAverage();
            dateOfRelease = movie.getReleaseDate();
            movie_id = movie.getId();
            ratingBar.setRating(Float.parseFloat(rating) / 2);
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    updatedVoteAverage = v + (voteAverage)/2;
                    updatedVoteCount = (movie.getVoteCount())+1;
                    BigDecimal result_vote_avg;
                    result_vote_avg = round(updatedVoteAverage,1);
                    Log.e("Det act Rating passed ", String.valueOf(v));
                    get_Sessionid get_sessionid = new get_Sessionid(movie_id, result_vote_avg, updatedVoteCount);
                    get_sessionid.execute();

                    userRating.setText( result_vote_avg + "/10 "+"("+updatedVoteCount+")");
                   // textView_vote_count.setText(String.valueOf(new_vote_count));
                   // textView_vote_count.append(users);
                }
            });
            //need to change vote and user count in db


            String poster = "https://image.tmdb.org/t/p/w500" + thumbnail;

            Glide.with(this)
                    .load(poster)
                    .placeholder(R.drawable.load)
                    .into(imageView);

            nameOfMovie.setText(movieName);
            plotSynopsis.setText(synopsis);
            //userRating.setText(rating);
            userRating.setText(voteAverage +"/10" + "(" +voteCount +")");
            releaseDate.setText(dateOfRelease);

        } else {
            Toast.makeText(this, "No API Data", Toast.LENGTH_SHORT).show();
        }

        MaterialFavoriteButton materialFavoriteButtonNice =
                (MaterialFavoriteButton) findViewById(R.id.favorite_button);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        materialFavoriteButtonNice.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        if (favorite) {
                            SharedPreferences.Editor editor = getSharedPreferences("com.delaroystudios.movieapp.DetailActivity", MODE_PRIVATE).edit();
                            editor.putBoolean("Favorite Added", true);
                            editor.commit();
                            //saveFavorite();
                            Snackbar.make(buttonView, "Added to Favorite",
                                    Snackbar.LENGTH_SHORT).show();
                        } else {
                            int movie_id = getIntent().getExtras().getInt("id");
                            /*favoriteDbHelper = new FavoriteDbHelper(DetailActivity.this);
                            favoriteDbHelper.deleteFavorite(movie_id);*/

                            SharedPreferences.Editor editor = getSharedPreferences("com.delaroystudios.movieapp.DetailActivity", MODE_PRIVATE).edit();
                            editor.putBoolean("Favorite Removed", true);
                            editor.commit();
                            Snackbar.make(buttonView, "Removed from Favorite",
                                    Snackbar.LENGTH_SHORT).show();
                        }

                    }
                }
        );

        String JSON_URL_TRAILER= " https://api.themoviedb.org/3/movie/"+movie_id +"/videos?api_key=047d097b7479b4e84c76129f2d4566ec";
        loadJSON(JSON_URL_TRAILER);

        String JSON_URL_CAST= " https://api.themoviedb.org/3/movie/"+movie_id +"/credits?api_key=047d097b7479b4e84c76129f2d4566ec";
        loadJSON_Cast(JSON_URL_CAST);

        String JSON_URL_CREW= " https://api.themoviedb.org/3/movie/"+movie_id +"/credits?api_key=047d097b7479b4e84c76129f2d4566ec";
        loadJSON_Crew(JSON_URL_CREW);

    }
    public static BigDecimal round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(getString(R.string.movie_details));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private void initViewsTrailer(ArrayList<Trailer>trailerList) {
       // trailerList = new ArrayList<>();
        adapter = new TrailerAdapter(this, trailerList);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void initViewsCast(ArrayList<Cast>castList) {
        // trailerList = new ArrayList<>();
        castAdapter = new CastAdapter(this, castList );

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_cast);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(castAdapter);
        //adapter.notifyDataSetChanged();

    }

    private void initViewsCrew(ArrayList<Crew>crewList) {
        // trailerList = new ArrayList<>();
        crewAdapter = new CrewAdapter(this, crewList );

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_crew);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(crewAdapter);
        //adapter.notifyDataSetChanged();

    }

    // LoadJson
    public void loadJSON(String jsonURL) {
        networkOk = NetworkHelper.hasNetworkAccess(DetailActivity.this);
        if (networkOk) {
            Intent intent = new Intent(DetailActivity.this, TrailerService.class);
            intent.setData(Uri.parse(jsonURL));
            startService(intent);
        } else {
            Toast.makeText(DetailActivity.this, "Network not available", Toast.LENGTH_LONG).show();
        }
    }

    // LoadJson
    public void loadJSON_Cast(String jsonURL) {
        networkOk = NetworkHelper.hasNetworkAccess(DetailActivity.this);
        if (networkOk) {
            Intent intent = new Intent(DetailActivity.this, CastService.class);
            intent.setData(Uri.parse(jsonURL));
            startService(intent);
        } else {
            Toast.makeText(DetailActivity.this, "Network not available", Toast.LENGTH_LONG).show();
        }
    }
    // LoadJson
    public void loadJSON_Crew(String jsonURL) {
        networkOk = NetworkHelper.hasNetworkAccess(DetailActivity.this);
        if (networkOk) {
            Intent intent = new Intent(DetailActivity.this, CrewService.class);
            intent.setData(Uri.parse(jsonURL));
            startService(intent);
        } else {
            Toast.makeText(DetailActivity.this, "Network not available", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(castBroadcastReceiver);

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(crewBroadcastReceiver);

        super.onDestroy();
    }
}

    /*

    public void saveFavorite(){
        favoriteDbHelper = new FavoriteDbHelper(activity);
        favorite = new Movie();

        Double rate = movie.getVoteAverage();


        favorite.setId(movie_id);
        favorite.setOriginalTitle(movieName);
        favorite.setPosterPath(thumbnail);
        favorite.setVoteAverage(rate);
        favorite.setOverview(synopsis);

        favoriteDbHelper.addFavorite(favorite);
    }
*/


