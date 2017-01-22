package com.shareqube.popularmoviesapp.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.shareqube.popularmoviesapp.Movie;
import com.shareqube.popularmoviesapp.MovieDiscoveryFragment;
import com.shareqube.popularmoviesapp.R;


import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Jude Ben on 6/7/2015.
 */
public class MoviesAdapter extends ArrayAdapter<Movie> {

    String LOG_TAG = MoviesAdapter.class.getSimpleName() ;

    static Context mContext;
    List<Movie> movie_poster;
    List<String> path ;


    int resouse_id ;

    LayoutInflater minflater;
    ViewHolder holder ;


    // ignoring   defaut text view id with super
    public MoviesAdapter(Context context, int resId , List<Movie> movies ) {
        super(context ,resId , movies);

        resouse_id = resId ;
        movie_poster = movies ;
       mContext = context ;

        minflater = (LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        }




    @Override
    public int getCount() {
        return movie_poster.toArray().length;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        Movie movie = getItem(position) ;


        new DownloadPosterToCacheTask().execute(movie.getmMovieposter());

        if(convertView == null){

            convertView = minflater.inflate(R.layout.row,parent , false);
            holder = new ViewHolder();
            holder.moviesPoster = (ImageView)convertView.findViewById(R.id.poster);
            holder.moviesPoster.setContentDescription(movie.getmMovietitle());
            convertView.setTag(holder);
        }
        else{
            holder =(ViewHolder)convertView.getTag() ;
        }









        Glide.with(mContext)
                .load(movie.getmMovieposter()).error(R.drawable.no_poster)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.moviesPoster);







              return convertView;
    }

    static  class ViewHolder{
        ImageView moviesPoster ;




    }


    private class DownloadPosterToCacheTask extends AsyncTask<String, Void, File> {
        @Override
        protected File doInBackground(String... params) {
            FutureTarget<File> future = Glide.with(mContext)
                    .load(params[0])
                    .downloadOnly(256, 256);

            File file = null;
            try {
                file = future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return file;
        }
    }
}
