package com.codepath.flickster;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MovieDetailActivity extends YouTubeBaseActivity {

  AsyncHttpClient httpClient;
  String videosUrl = "https://api.themoviedb.org/3/movie/%s/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
  String youTubeKey;

  YouTubePlayerView youTubePlayerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie_detail);

    String title = getIntent().getStringExtra("title");
    String backdropImageUrl = getIntent().getStringExtra("backdropImageUrl");
    String overview = getIntent().getStringExtra("overview");
    Double rating = getIntent().getDoubleExtra("rating", 0.0);
    Long movieId = getIntent().getLongExtra("movieId", 0L); // Get Movie Id and call API

    //ImageView ivMovieImage = (ImageView) findViewById(R.id.ivMovieImage);
    TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
    TextView tvOverview = (TextView) findViewById(R.id.tvOverview);
    RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);

    // set all values
    /*Picasso.with(getApplicationContext()).load(backdropImageUrl)
        .fit().centerCrop()
        .placeholder(R.mipmap.ic_movie_placeholder)
        .error(R.mipmap.ic_movie_placeholder_error)
        .into(ivMovieImage);*/
    tvTitle.setText(title);
    tvOverview.setText(overview);
    // Rating bar
    ratingBar.setIsIndicator(true);
    ratingBar.setNumStars(5);
    ratingBar.setStepSize(0.5f);
    ratingBar.setRating(rating.floatValue());


    // Get Video
    // View
    youTubePlayerView = (YouTubePlayerView) findViewById(R.id.player);

    // Call API
    httpClient = new AsyncHttpClient();
    fetchVideos(movieId);
  }

  public void fetchVideos(Long movieId){
    String url = String.format(videosUrl, movieId);
    httpClient.get(url, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        JSONArray jsonArray = null;
        try {
          jsonArray = response.getJSONArray("results");
          for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).getString("type").equals("Trailer")) {
              youTubeKey = jsonArray.getJSONObject(i).getString("key");
              break;
            }
          }
          playVideo(youTubeKey);
          Log.d("DEBUG", response.toString());
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });
  }

  public void playVideo(final String youTubeKey){
    youTubePlayerView.initialize("AIzaSyBiA3tIiucjUZw5eXssK7UR5gHv_mnr-F0",
        new YouTubePlayer.OnInitializedListener() {
          @Override
          public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                              YouTubePlayer youTubePlayer, boolean b) {

            // do any work here to cue video, play video, etc.
            youTubePlayer.cueVideo(youTubeKey); // cue
          }

          @Override
          public void onInitializationFailure(YouTubePlayer.Provider provider,
                                              YouTubeInitializationResult youTubeInitializationResult) {

          }
        });
  }



}
