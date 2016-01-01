package me.takus.hotmovie;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import me.takus.hotmovie.entity.Movie;
import me.takus.hotmovie.model.MovieDatabase;

public class MovieFragment extends Fragment {

    private static final String LOG_TAG = MovieFragment.class.getSimpleName();

    private MovieImageAdapter mMovieAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMovieAdapter = new MovieImageAdapter(getContext());

        View rootView = inflater.inflate(R.layout.grid_view_movie, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.grid_view_movie);
        gridView.setAdapter(mMovieAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Movie movie = mMovieAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra("Movie", movie);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovie();
    }

    private void updateMovie() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortType = prefs.getString(getString(R.string.pref_sort_key), "popularity.desc");

        FetchMovieTask movieTask = new FetchMovieTask();
        movieTask.execute(sortType);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        private RequestQueue queue = Volley.newRequestQueue(getContext());

        @Override
        protected Void doInBackground(final String... params) {
            final String BASE_URL =
                    "http://api.themoviedb.org/3/discover/movie?";
            final String VOTE_PARAM = "vote_count.gte";
            final String SORT_PARAM = "sort_by";
            final String API_PARAM = "api_key";

            String url = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(VOTE_PARAM, "3")
                    .appendQueryParameter(SORT_PARAM, params[0])
                    .appendQueryParameter(API_PARAM, BuildConfig.MOVIE_DATABASE_API_KEY)
                    .build().toString();

            queue.add(new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    (String) null,
                    new Response.Listener<JSONObject>() {
                        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray results = response.getJSONArray("results");
                                List<Movie> movies = new ArrayList<>();
                                for (int i = 0; i < results.length(); i++) {
                                    Movie movie = MovieDatabase.parse(results.getJSONObject(i));
                                    movies.add(movie);
                                }
                                mMovieAdapter.setMovies(movies);
                            } catch (JSONException e) {
                                Log.e(LOG_TAG, e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(LOG_TAG, error.getMessage());
                        }
                    }));

            return null;
        }
    }
}
