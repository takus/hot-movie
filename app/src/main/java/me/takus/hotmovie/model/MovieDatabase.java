package me.takus.hotmovie.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.takus.hotmovie.entity.Movie;

public class MovieDatabase {

    private static final String LOG_TAG = MovieDatabase.class.getSimpleName();

    public static Movie parse(JSONObject json) {
        Movie movie = null;
        try {
            movie = movie.builder()
                    .title(json.getString("original_title"))
                    .overview(json.getString("overview"))
                    .posterUrl("http://image.tmdb.org/t/p/w185/" + json.getString("poster_path"))
                    .releaseDate(json.getString("release_date"))
                    .popularity(json.getDouble("popularity"))
                    .voteAverage(json.getDouble("vote_average"))
                    .build();
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return movie;
    }
}
