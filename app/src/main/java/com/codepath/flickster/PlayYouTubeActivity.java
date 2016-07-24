package com.codepath.flickster;

import android.os.Bundle;
import android.util.Log;

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

public class PlayYouTubeActivity extends YouTubeBaseActivity {


  AsyncHttpClient httpClient;
  String videosUrl = "https://api.themoviedb.org/3/movie/%s/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
  String youTubeKey;

  YouTubePlayerView youTubePlayerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_play_you_tube);

    // Get Movie Id and call API
    Long movieId = getIntent().getLongExtra("movieId", 0L);
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
            youTubePlayer.loadVideo(youTubeKey); // play
          }
          @Override
          public void onInitializationFailure(YouTubePlayer.Provider provider,
                                              YouTubeInitializationResult youTubeInitializationResult) {

          }
        });
  }
}
