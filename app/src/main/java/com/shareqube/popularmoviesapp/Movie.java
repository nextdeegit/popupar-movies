package com.shareqube.popularmoviesapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jude Ben on 6/8/2015.
 */
public class Movie implements Parcelable {


    private int mMovieId;
    private String mMovietitle;
    String mMovieposter;
    String mMovieReleaseDate;
    String mMovieOverview;
    String mMovieRating;


    public Movie(int id, String title, String poster, String releaseDate, String overview, String user_rating) {
        this.mMovieId = id;
        this.mMovietitle = title;
        this.mMovieposter = poster;
        this.mMovieReleaseDate = releaseDate;
        this.mMovieOverview = overview;
        this.mMovieRating = user_rating;


    }




    public String getmMovietitle() {
        return mMovietitle;
    }

    public String getmMovieRating() {
        return mMovieRating;
    }

    public String getmMovieposter() {
        return mMovieposter;
    }

    public String getmMovieReleaseDate() {
        return mMovieReleaseDate;
    }

    public String getmMovieOverview() {
        return mMovieOverview;
    }

    public int getmMovieId() {
        return mMovieId;
    }

    // setters
    public void setmMovieId(int id) {
        mMovieId = id;
    }

    public void setmMovietitle(String title) {
        mMovietitle = title;
    }

    public void setmMovieposter(String poster) {
        mMovieposter = poster;

    }

    //Todo Change it to date later
    public void setmMovieReleaseDate(String date) {
        mMovieReleaseDate = date;
    }

    public void setmMovieOverview(String overview) {
        mMovieOverview = overview;
    }

    public void setmMovieRating(String rating) {
        mMovieRating = rating;
    }





    protected Movie(Parcel in) {
        mMovieId = in.readInt();
        mMovietitle = in.readString();
        mMovieposter = in.readString();
        mMovieReleaseDate = in.readString();
        mMovieOverview = in.readString();
        mMovieRating = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mMovieId);
        dest.writeString(mMovietitle);
        dest.writeString(mMovieposter);
        dest.writeString(mMovieReleaseDate);
        dest.writeString(mMovieOverview);
        dest.writeString(mMovieRating);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}