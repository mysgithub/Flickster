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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shyam Rokde on 7/23/16.
 */
public class MoviesAdapter extends ArrayAdapter<Movie> {

  public enum LayoutTypeValues {
    POPULAR, REGULAR
  }


  public MoviesAdapter(Context context, ArrayList<Movie> movies){
    super(context, 0, movies);
  }

  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    // Get item at position
    int type = getItemViewType(position);
    if(type == LayoutTypeValues.REGULAR.ordinal()){
      ViewHolderDetail viewHolder;
      if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie, parent, false);
        viewHolder = new ViewHolderDetail(convertView);

        convertView.setTag(viewHolder);
      }else{
        viewHolder = (ViewHolderDetail) convertView.getTag();
      }
      // Position object
      Movie movie = getItem(position);

      //Set Values
      viewHolder.ivImage.setImageResource(0);
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

    }else if(type == LayoutTypeValues.POPULAR.ordinal()){
      ViewHolderImage viewHolder;
      if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie_backdrop, parent, false);
        viewHolder = new ViewHolderImage(convertView);

        convertView.setTag(viewHolder);
      }else{
        viewHolder = (ViewHolderImage) convertView.getTag();
      }
      // Position object
      Movie movie = getItem(position);
      viewHolder.ivImage.setImageResource(0);
      //Set Values
      Picasso.with(getContext()).load(movie.getBackdropPath())
          .fit().centerCrop()
          .placeholder(R.mipmap.ic_movie_placeholder)
          .error(R.mipmap.ic_movie_placeholder_error)
          .into(viewHolder.ivImage);
    }


    // Return the completed view to render on screen
    return convertView;
  }


  @Override
  public int getViewTypeCount() {
    return LayoutTypeValues.values().length;
  }

  @Override
  public int getItemViewType(int position) {
    Movie movie = getItem(position);
    int type;
    if(movie.getVoteAverage() > 5.00){
      type = LayoutTypeValues.POPULAR.ordinal();
    }else{
      type = LayoutTypeValues.REGULAR.ordinal();
    }
    return type;
  }



  // View lookup cache
  static class ViewHolderDetail {
    @BindView(R.id.ivMovieImage) ImageView ivImage;
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvOverview) TextView tvOverview;

    public ViewHolderDetail(View view){
      ButterKnife.bind(this, view);
    }
  }

  static class ViewHolderImage {
    @BindView(R.id.ivMovieImage) ImageView ivImage;

    public ViewHolderImage(View view){
      ButterKnife.bind(this, view);
    }
  }

}
