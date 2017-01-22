package com.shareqube.popularmoviesapp;

/**
 * Created by Jude Ben on 6/11/2015.
 */


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.io.InputStream;

/**
 * A placeholder fragment containing a simple view.
 */
public  class MovieDetailFragment extends Fragment {
    String LOG_TAG = MovieDetailFragment.class.getSimpleName() ;

    ViewHolder holder ;

    public MovieDetailFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_setting , menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId() ;

        switch (id){
            case R.id.action_settings :
                Intent settingInent  = new Intent(getActivity() ,SettingsActivity.class) ;
                startActivity(settingInent);
                break;

            default:
                return false ;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState){


            Intent intent = getActivity().getIntent();
            View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
            holder = new ViewHolder(rootView);
            rootView.setTag(holder);
            if (intent != null & intent.hasExtra(getString(R.string.intent_extra))) {
                Movie movieDetails = intent.getParcelableExtra(getString(R.string.intent_extra));



            // try to load  Glide Image from from Diskcache

            Glide.with(this).load(movieDetails.getmMovieposter()).diskCacheStrategy(DiskCacheStrategy.SOURCE).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    Log.i(LOG_TAG, "Listener onException: " + e.toString());
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    Log.e(LOG_TAG, "onResourceReady with resource = " + resource);
                    Log.i(LOG_TAG, "onResourceReady from memory cache = " + isFromMemoryCache);
                    return false;
                }
            }).error(R.drawable.no_poster)
                    .into(new SimpleTarget<GlideDrawable>(256, 256) {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            Log.i(LOG_TAG, "GlideDrawalble = '" + resource + "'");
                            holder.detailPoster.setImageDrawable(resource.getCurrent());
                        }
                    });

     /*
            Glide.with(this).load(movieDetails.getmMovieposter()).error(R.drawable.installerposter)
                    .override(400, 400)
                    .into(holder.detailPoster);
                    */
            ViewCompat.setTransitionName(holder.detailPoster, MovieDetail.Transision_name);
            holder.movieOverview.setText(movieDetails.getmMovieOverview());
            holder.movieRelease.setText(movieDetails.getmMovieReleaseDate());
            holder.movieTitle.setText(movieDetails.getmMovietitle());

            Float rating = Float.parseFloat(movieDetails.getmMovieRating());
            holder.movieRating.setRating(rating);

            holder.movieRatingValue.setText(movieDetails.getmMovieRating());






        }


        return rootView;
    }



    static class ViewHolder{
        ImageView detailPoster ;
        TextView movieTitle ;
        TextView  movieRelease ;
        TextView movieOverview ;
        RatingBar movieRating ;
        TextView  movieRatingValue ;

        public ViewHolder(View v){
            detailPoster = (ImageView) v.findViewById(R.id.detailPoster_view);
            movieTitle = (TextView) v.findViewById(R.id.movie_title_view) ;
            movieRelease = (TextView)v.findViewById(R.id.movie_release_view) ;
            movieOverview = (TextView) v.findViewById(R.id.movie_overview_view);
            movieRating = ( RatingBar) v.findViewById(R.id.movieRatingBar) ;
            movieRatingValue = (TextView) v.findViewById(R.id.movieRatingValue);
        }

    }
}