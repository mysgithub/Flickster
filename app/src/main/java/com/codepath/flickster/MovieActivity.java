package com.codepath.flickster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.flickster.adapters.MoviesAdapter;
import com.codepath.flickster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieActivity extends AppCompatActivity {

  ArrayList<Movie> movies;
  MoviesAdapter movieAdapter;
  ListView lvItems;

  private SwipeRefreshLayout swipeContainer;
  AsyncHttpClient httpClient;

  String movieDBUrl = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

  private final int REQUEST_CODE = 20;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie);

    // Lookup the swipe container view
    swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
    // Setup refresh listener which triggers new data loading
    swipeContainer.setOnRefreshListener(onRefreshListener);

    lvItems = (ListView) findViewById(R.id.lvMovies);

    movies = new ArrayList<>();
    movieAdapter = new MoviesAdapter(this, movies);
    lvItems.setAdapter(movieAdapter);
    lvItems.setOnItemClickListener(onItemClickListener);

    httpClient = new AsyncHttpClient();
    fetchMoviesList();
  }

  public void fetchMoviesList(){
    httpClient.get(movieDBUrl, new JsonHttpResponseHandler(){
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

        // Remember to CLEAR OUT old items before appending in the new ones
        movieAdapter.clear();

        JSONArray movieJsonResults = null;
        try {
          movieJsonResults = response.getJSONArray("results");
          movies.addAll(Movie.fromJSONArray(movieJsonResults));
          movieAdapter.notifyDataSetChanged();

          // Now we call setRefreshing(false) to signal refresh has finished
          swipeContainer.setRefreshing(false);

          Log.d("DEBUG", movieJsonResults.toString());
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        super.onFailure(statusCode, headers, responseString, throwable);
      }
    });
  }


  private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
    @Override
    public void onRefresh() {
      // Fetch
      fetchMoviesList();
    }
  };

  AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      launchMovieDetailView(position);
    }
  };


  public void launchMovieDetailView(int position){

    Movie movie = movies.get(position);
    // If rating is greater than 5 then launch YouTube
    Intent i;
    if(movie.getVoteAverage() > 5.00){
      i = new Intent(MovieActivity.this, PlayYouTubeActivity.class);
      i.putExtra("movieId", movie.getMovieId());
    }else{
      // first parameter is the context, second is the class of the activity to launch
      i = new Intent(MovieActivity.this, MovieDetailActivity.class);
      i.putExtra("backdropImageUrl", movie.getBackdropPath());
      i.putExtra("title", movie.getOriginalTitle());
      i.putExtra("overview", movie.getOverview());
      i.putExtra("rating", movie.getVoteAverage());
      i.putExtra("movieId", movie.getMovieId());
    }
    // brings up the second activity
    startActivity(i);
  }

}
