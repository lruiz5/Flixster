package com.example.flixster.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.flixster.DetailActivity;
import com.example.flixster.R;
import com.example.flixster.models.Movie;
import com.google.android.youtube.player.YouTubePlayerView;

import org.parceler.Parcels;

import java.util.List;

public class MovieAdapters extends RecyclerView.Adapter<MovieAdapters.ViewHolder>{

    Context context;
    List<Movie> movies;

    public MovieAdapters(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    //usually invloves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");

        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);

        return new ViewHolder(movieView);
    }

    //involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder" + position);

        //get the movie at the passed in position
        Movie movie = movies.get(position);
        
        //bind the movie data into the view holder
        holder.bind(movie);
    }

    //returns total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout container;
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
        YouTubePlayerView ytPlayer;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            ytPlayer = itemView.findViewById((R.id.player));
            container = itemView.findViewById(R.id.container);
            ratingBar = itemView.findViewById((R.id.ratingBar));
        }

        public void bind(final Movie movie) {

            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());

            String imageURL;

            //if phone is in landscape
            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                //imageURL = backdrop image
                imageURL = movie.getBackdropPath();
            }
            else {
                //else imageURL = poster image
                imageURL = movie.getPosterPath();
            }

            int radius = 7; // corner radius, higher value = more rounded
            int margin = 0; // crop margin, set to 0 for corners with no crop
            Glide.with(context)
                    .load(imageURL)
                    .apply(new RequestOptions()
                        .placeholder(R.drawable.placeholder))
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(radius)))
                    .into(ivPoster);

            //1. register click listener on the whole row
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //2. navigate to a new activity on click
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("movie", Parcels.wrap(movie));

                    Pair<View, String> p1 = Pair.create((View)tvTitle, "frontTitle");
                    Pair<View, String> p2 = Pair.create((View)tvTitle, "detailTitle");

                    Pair<View, String> p3 = Pair.create((View)tvOverview, "frontOverview");
                    Pair<View, String> p4 = Pair.create((View)tvOverview , "detailOverview");

                    ActivityOptions options;
                    options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, p1, p2, p3, p4);
                    context.startActivity(i, options.toBundle());
                }
            });
        }
    }
}
