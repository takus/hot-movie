package me.takus.hotmovie;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import me.takus.hotmovie.entity.Movie;

public class DetailFragment extends Fragment {

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.detail_view_movie, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("Movie")) {
            Movie movie = (Movie) intent.getSerializableExtra("Movie");

            Picasso.with(getContext()).load(movie.getPosterUrl()).into((ImageView) rootView.findViewById(R.id.detail_poster));
            ((TextView) rootView.findViewById(R.id.detail_title)).setText(movie.getTitle());
            ((TextView) rootView.findViewById(R.id.detail_date)).setText(movie.getReleaseDate());
            ((TextView) rootView.findViewById(R.id.detail_overview)).setText(
                    movie.getOverview()
                            + "\n\n"
                            + "(Rate = " + movie.getVoteAverage() + " / " + "10)"
            );
        }

        return rootView;
    }

}
