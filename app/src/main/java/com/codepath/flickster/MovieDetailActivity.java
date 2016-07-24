package com.codepath.flickster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie_detail);

    String title = getIntent().getStringExtra("title");
    String backdropImageUrl = getIntent().getStringExtra("backdropImageUrl");
    String overview = getIntent().getStringExtra("overview");
    Double rating = getIntent().getDoubleExtra("rating", 0.0);

    ImageView ivMovieImage = (ImageView) findViewById(R.id.ivMovieImage);
    TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
    TextView tvOverview = (TextView) findViewById(R.id.tvOverview);
    RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);

    // set all values
    Picasso.with(getApplicationContext()).load(backdropImageUrl)
        .fit().centerCrop()
        .placeholder(R.mipmap.ic_movie_placeholder)
        .error(R.mipmap.ic_movie_placeholder_error)
        .into(ivMovieImage);
    tvTitle.setText(title);
    tvOverview.setText(overview);
    // Rating bar
    ratingBar.setIsIndicator(true);
    ratingBar.setNumStars(5);
    ratingBar.setStepSize(0.5f);
    ratingBar.setRating(rating.floatValue());
  }
}
