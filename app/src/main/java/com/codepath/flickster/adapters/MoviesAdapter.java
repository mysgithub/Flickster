package com.codepath.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.flickster.R;
import com.codepath.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Shyam Rokde on 7/23/16.
 */
public class MoviesAdapter extends ArrayAdapter<Movie> {

  public MoviesAdapter(Context context, ArrayList<Movie> movies){
    super(context, 0, movies);
  }

  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    // Get item at position
    Movie movie = getItem(position);

    // Check if an existing view is being reused, otherwise inflate the view
    ViewHolder viewHolder; // view lookup cache stored in tag
    if(convertView == null){
      // If there's no view to re-use, inflate a brand new view for row
      viewHolder = new ViewHolder();

      LayoutInflater inflater = LayoutInflater.from(getContext());
      convertView = inflater.inflate(R.layout.item_movie, parent, false);

      viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
      viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
      viewHolder.tvOverview = (TextView) convertView.findViewById(R.id.tvOverview);

      // Cache the viewHolder object inside the fresh view
      convertView.setTag(viewHolder);
    }else {
      // View is being recycled, retrieve the viewHolder object from tag
      viewHolder = (ViewHolder) convertView.getTag();
    }

    // Lookup view for data population
    viewHolder.ivImage.setImageResource(0); // clear image resource
    // Populate the data into the template view using the data object
    viewHolder.tvTitle.setText(movie.getOriginalTitle());
    viewHolder.tvOverview.setText(movie.getOverview());

    int orientation = getContext().getResources().getConfiguration().orientation;
    if(orientation == Configuration.ORIENTATION_PORTRAIT){
      Picasso.with(getContext()).load(movie.getPosterPath())
          .resize(275, 0)
          .placeholder(R.mipmap.ic_movie_placeholder)
          .error(R.mipmap.ic_movie_placeholder_error)
          .into(viewHolder.ivImage);
    }else if(orientation == Configuration.ORIENTATION_LANDSCAPE){
      Picasso.with(getContext()).load(movie.getBackdropPath())
          .resize(500, 0)
          .placeholder(R.mipmap.ic_movie_placeholder)
          .error(R.mipmap.ic_movie_placeholder_error)
          .into(viewHolder.ivImage);
    }


    // Return the completed view to render on screen
    return convertView;
  }

  // View lookup cache
  private static class ViewHolder {
    ImageView ivImage;
    TextView tvTitle;
    TextView tvOverview;
  }
}
