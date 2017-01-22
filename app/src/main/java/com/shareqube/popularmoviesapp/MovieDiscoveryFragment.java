package com.shareqube.popularmoviesapp;

/**
 * Created by Jude Ben on 6/8/2015.
 */


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.shareqube.popularmoviesapp.adapter.MoviesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public  class MovieDiscoveryFragment extends Fragment {

    String LOG_TAG = MovieDiscoveryFragment.class.getSimpleName() ;

    Movie movie;




    MoviesAdapter mMoviesAdapter ;



    public MovieDiscoveryFragment() {

    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    // saving the data into Parcelable object
    @Override
    public void onSaveInstanceState(Bundle outState) {

        if(movie != null){
          outState.putParcelable("mMovie", movie);
        }
        super.onSaveInstanceState(outState);
    }

// restoring parcelable data on device change
    @Override
    public void onViewStateRestored( Bundle savedInstanceState) {

        if(savedInstanceState != null){
            movie = savedInstanceState.getParcelable("mMovie");
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_discovery_screen , menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId() ;
        switch (id ){

            case R.id.action_settings:

                Intent settingIntent = new Intent(getActivity() ,SettingsActivity.class) ;
                startActivity(settingIntent);
                break;

            default:
                return false ;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateMovies(){
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();

        SharedPreferences settingPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String sort_order = settingPref.getString(getString(R.string.movie_sort_key),getString(R.string.movie_sort_order_default_value));

       // Log.e(LOG_TAG , "all keys"+ sort_order_test) ;



        fetchMoviesTask.execute(sort_order);


    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discovery_screen, container, false);





        return rootView;
    }


    public  class  FetchMoviesTask extends AsyncTask<String ,Void ,List<Movie>> {

        final String LOG_TAG = FetchMoviesTask.class.getSimpleName() ;
        public Movie movie ;
        public  String[] moviePoster ;





        final String BASE_MOVIE_POSTER_URL  = "http://image.tmdb.org/t/p/" ;
        String[] IMAGE_SIZES = {"w185","w92", "w154" , "w342" , "w500" ,"w780"} ;
        @Override
        protected List<Movie> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;
            String API_KEY = "23d233badd61bb79fba75efdee30cc86" ;

            List <Movie> movies  =  new ArrayList<>() ;




            try {
                // Construct the URL for the http://api.themoviedb.org/ api
                // http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=23d233badd61bb79fba75efdee30cc86

                final  String BASE_URL = "http://api.themoviedb.org/3/discover/movie?" ;

                final String SORT_BY_PARAM ="sort_by" ;
                final String API_KEY_PARAM = "api_key" ;



                Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY_PARAM, params[0])
                        .appendQueryParameter(API_KEY_PARAM,API_KEY)
                        .build() ;

                URL url =  new URL(buildUri.toString()) ;


                Log.e(LOG_TAG, "Built URL" + buildUri.toString()) ;


                // Create the request to theMovieDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();

                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return  null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));



                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null ;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }

            }



            // return statement

            try{
                // prepares the data and parse the json
                 movies = getMoviesDataFromJson(moviesJsonStr);

            } catch (JSONException e){
                Log.e(LOG_TAG , e.getMessage() , e);
                e.printStackTrace();
            }




            return movies;

        }

        @Override
        protected void onPostExecute(List<Movie> result) {


           // List<String> poster_paths =new ArrayList<String>();
            List<Movie> movies_list = new ArrayList<>();



            if(result != null) {


                for (int i = 0; i < result.toArray().length; i++) {

                    Movie movies = result.get(i);
                    //movies.getmMovieposter()

                   // poster_paths.addAll(result);
                    movies_list.addAll(result);

                }

            }else {
                mMoviesAdapter.clear();
            }


            GridView moviesPosterGrid = (GridView) getActivity().findViewById(R.id.movie_gridview) ;

            mMoviesAdapter   =  new MoviesAdapter(getActivity(), R.layout.fragment_discovery_screen  , result);
            moviesPosterGrid.setAdapter(mMoviesAdapter);




            moviesPosterGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    Movie discover = mMoviesAdapter.getItem(position);
                    Intent movieDetailIntent = new Intent(getActivity(), MovieDetail.class);


                    movieDetailIntent.putExtra("movie", discover);

                    //Transition
                    // reading and following
                    //https://github.com/googlesamples/android-ActivitySceneTransitionBasic

                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            getActivity(),
                            new Pair<View, String>(view.findViewById(R.id.poster),
                                    MovieDetail.Transision_name));

                    ActivityCompat.startActivity(getActivity(), movieDetailIntent, activityOptionsCompat.toBundle());
                    startActivity(movieDetailIntent);

                }
            });

           // mMoviesAdapter.notifyDataSetChanged();





            class ViewHolder {
                 ImageView moviePoster;
                 TextView movieTitle;
                 RatingBar movieRating ;
                 TextView  movieOverview ;



                 public ViewHolder(View v) {

                 }
             }

            //end of View Holder classs
        }



//construct absolute path for the movie poster
        public String getMoviePosterAbsolutePath(String relative_path){


            return  BASE_MOVIE_POSTER_URL + IMAGE_SIZES[0] + relative_path ;
        }

        public ArrayList<Movie> getMoviesDataFromJson(String moviesJsonStr ) throws JSONException{

            final String MOVIE_RESULT = "results" ;

            final String MOVIE_ID = "id";

            final String MOVIE_TITLE = "title" ;
            final String MOVIE_POSTER_RELATIVE_PATH = "poster_path"
                    ;
            final String MOVIE_RELEASE_DATE = "release_date" ;

            final String PLOT_SYNOPSIS = "overview";

            final String USER_RATING = "vote_average";

            JSONObject moviesJson = new JSONObject(moviesJsonStr) ;
            JSONArray moviesResultArray = moviesJson.getJSONArray(MOVIE_RESULT);

            ArrayList<Movie>  resultMovies = new ArrayList<Movie>(moviesResultArray.length());


            for (int i = 0 ; i< moviesResultArray.length() ; i++){

                // get the first json object for the movies

                JSONObject moviesDiscover = moviesResultArray.getJSONObject(i);

              int movie_id =   Integer.parseInt(moviesDiscover.getString(MOVIE_ID));


                String movie_title = moviesDiscover.getString(MOVIE_TITLE) ;

                String movie_poster =  getMoviePosterAbsolutePath(moviesDiscover.getString(MOVIE_POSTER_RELATIVE_PATH));

                String release_date  = moviesDiscover.getString(MOVIE_RELEASE_DATE) ;

                String movie_overview = moviesDiscover.getString(PLOT_SYNOPSIS) ;

                String user_rating = moviesDiscover.getString(USER_RATING) ;

                // populate the Movie Objects with the json data

                 movie = new Movie(movie_id ,movie_title ,movie_poster ,release_date ,movie_overview ,user_rating) ;



                resultMovies.add(movie);





            }




            return   resultMovies ;

        }






    }


}

