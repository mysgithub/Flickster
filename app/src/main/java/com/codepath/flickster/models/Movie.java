package com.codepath.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Shyam Rokde on 7/23/16.
 */
public class Movie {
  private String posterPath;
  private String originalTitle;
  private String overview;
  private boolean isVideo;
  private double voteAverage;

  public Movie(JSONObject jsonObject) throws JSONException{
    this.posterPath = jsonObject.getString("poster_path");
    this.originalTitle = jsonObject.getString("original_title");
    this.overview = jsonObject.getString("overview");
    this.isVideo = jsonObject.getBoolean("video");
    this.voteAverage = jsonObject.getDouble("vote_average");
  }

  public static ArrayList<Movie> fromJSONArray(JSONArray array){
    ArrayList<Movie> movieList = new ArrayList<>();

    for (int i=0; i < array.length(); i++) {
      try {
        movieList.add(new Movie(array.getJSONObject(i)));
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return  movieList;
  }


  public String getPosterPath() {
    return String.format("https://image.tmdb.org/t/p/w342%s", posterPath);
  }

  public void setPosterPath(String posterPath) {

    this.posterPath = posterPath;
  }

  public String getOriginalTitle() {
    return originalTitle;
  }

  public void setOriginalTitle(String originalTitle) {
    this.originalTitle = originalTitle;
  }

  public String getOverview() {
    return overview;
  }

  public void setOverview(String overview) {
    this.overview = overview;
  }

  public boolean isVideo() {
    return isVideo;
  }

  public void setIsVideo(boolean isVideo) {
    this.isVideo = isVideo;
  }

  public double getVoteAverage() {
    return voteAverage;
  }

  public void setVoteAverage(double voteAverage) {
    this.voteAverage = voteAverage;
  }
}
