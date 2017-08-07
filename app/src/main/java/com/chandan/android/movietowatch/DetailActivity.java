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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chandan.android.movietowatch.adapter.TrailerAdapter;
import com.chandan.android.movietowatch.model.Movie;
import com.chandan.android.movietowatch.model.Trailer;
import com.chandan.android.movietowatch.services.TrailerService;
import com.chandan.android.movietowatch.utils.NetworkHelper;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by delaroy on 5/18/17.
 */
public class DetailActivity extends AppCompatActivity {

    ImageView imageView_votes, imageView_moviePoster, imageView_favorites, imageView_watchlist;
    RatingBar ratingBar;
    HorizontalScrollView horizontalScrollView_posters, horizontalScrollView_trailers,
            horizontalScrollView_cast, horizontalScrollView_crew;
    LinearLayout linearLayout_posters, linearLayout_trailers, linearLayout_cast, linearLayout_crew;

    String URL_movieDetails, URL_moviePosters, URL_movieTrailers, URL_movieCastCrew;
    String id, imdb_id;

    ImageLoader imageLoader = ImageLoader.getInstance();

    private static final String JSON_URL_TRAILER = null;

    TextView nameOfMovie, plotSynopsis, userRating, releaseDate;
    ImageView imageView;
    private RecyclerView recyclerView;
    private boolean networkOk;
    private TrailerAdapter adapter;
    private ArrayList<Trailer> trailerList;
    //private FavoriteDbHelper favoriteDbHelper;
   // private Movie favorite;*/
    private final AppCompatActivity activity = DetailActivity.this;

    Movie movie;
    String thumbnail, movieName, synopsis, rating, dateOfRelease;
    int movie_id;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            trailerList = intent.getParcelableArrayListExtra(TrailerService.MY_SERVICE_PAYLOAD_TRAILER);
            initViews(trailerList);
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

        imageView = (ImageView) findViewById(R.id.thumbnail_image_header);
        nameOfMovie = (TextView) findViewById(R.id.title);
        plotSynopsis = (TextView) findViewById(R.id.plotsynopsis);
        userRating = (TextView) findViewById(R.id.userrating);
        releaseDate = (TextView) findViewById(R.id.releasedate);

        //registering the Local broadcast receiver
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(TrailerService.MY_SERVICE_TRAILER));


        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("movies")) {

            movie = getIntent().getParcelableExtra("movies");

            thumbnail = movie.getPosterPath();
            movieName = movie.getOriginalTitle();
            synopsis = movie.getOverview();
            rating = Double.toString(movie.getVoteAverage());
            dateOfRelease = movie.getReleaseDate();
            movie_id = movie.getId();

            String poster = "https://image.tmdb.org/t/p/w500" + thumbnail;

            Glide.with(this)
                    .load(poster)
                    .placeholder(R.drawable.ic_action_name)
                    .into(imageView);

            nameOfMovie.setText(movieName);
            plotSynopsis.setText(synopsis);
            userRating.setText(rating);
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

    private void initViews(ArrayList<Trailer>trailerList) {
       // trailerList = new ArrayList<>();
        adapter = new TrailerAdapter(this, trailerList);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

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




    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
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


