package app.com.example.ahmed.popmovie;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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

/**
 * Created by ahmed on 31/10/16.
 */


public class MovieFragment extends Fragment {

    MovieAdapter adapter;
    private GridView gridView;
    private String baseUrl;

    public MovieFragment() {
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.popular) {
            getActivity().setTitle("Popular Movies");
            baseUrl = ("https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&");
            Uri builtUri = Uri.parse(baseUrl).buildUpon()
                    .appendQueryParameter("api_key", "5649c8a1382653f1cd95d5ef97604364")
                    .build();
            UpdateMovie(builtUri.toString());
            return true;
        } else if (id == R.id.rate) {
            getActivity().setTitle("Highest Rated Movies");

            baseUrl = ("https://api.themoviedb.org/3/discover/movie?sort_by=top_rated&");
            Uri builtUri = Uri.parse(baseUrl).buildUpon()
                    .appendQueryParameter("api_key", "5649c8a1382653f1cd95d5ef97604364")
                    .build();
            UpdateMovie(builtUri.toString());
            return true;
        } else {
            getActivity().setTitle("Favorite Movies");

            adapter = new MovieAdapter(getActivity(), getMovieData());
            gridView.setAdapter(adapter);
        }
        return super.onOptionsItemSelected(item);
    }
    private ArrayList<MovieModel> getMovieData(){
        ArrayList<MovieModel> listmovie =new ArrayList<>();
        MovieDBHelper helper =new MovieDBHelper(getActivity());
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.query(MovieDBHelper.TABLE_NAME , new String[]
                {MovieDBHelper.COL_ID, MovieDBHelper.COL_TITLE,
                MovieDBHelper.COL_OVERVIEW, MovieDBHelper.COL_POSTER,
                MovieDBHelper.COL_DATE, MovieDBHelper.COL_VOTE},
                null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                String movie_id = c.getString(c.getColumnIndex(MovieDBHelper.COL_ID));
                String movie_name = c.getString(c.getColumnIndex(MovieDBHelper.COL_TITLE));
                String movie_vote = c.getString(c.getColumnIndex(MovieDBHelper.COL_VOTE));
                String movie_release = c.getString(c.getColumnIndex(MovieDBHelper.COL_DATE));
                String movie_poster = c.getString(c.getColumnIndex(MovieDBHelper.COL_POSTER));
                String movie_over = c.getString(c.getColumnIndex(MovieDBHelper.COL_OVERVIEW));


                listmovie.add(new MovieModel(movie_id, movie_poster, movie_name, movie_over, movie_vote, movie_release));

            } while (c.moveToNext());
        }

        return listmovie;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        gridView = (GridView) rootView.findViewById(R.id.gridView_film);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieModel mov = (MovieModel) parent.getItemAtPosition(position);
                ((MainActivity) getActivity()).setMovie(mov);

            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable  Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        baseUrl = ("https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&");
        Uri builtUri = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter("api_key", "5649c8a1382653f1cd95d5ef97604364")
                .build();
        UpdateMovie(builtUri.toString());
    }

    public void UpdateMovie(String url) {
        new FetchMovieTask().execute(url);

    }

    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<MovieModel>> {
        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();


        @Override
        protected ArrayList<MovieModel> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;

            try {

                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    Log.e("inputstream", " filmFragment");
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                movieJsonStr = buffer.toString();

            } catch (IOException e) {

                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {

                    }
                }
            }
            try {
                return getmovieDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        public ArrayList<MovieModel> getmovieDataFromJson(String movieJsonStr)
                throws JSONException {

            final String list = "results";
            final String poster_path = "poster_path";
            final String overview = "overview";
            final String release_date = "release_date";
            final String vote_average = "vote_average";
            final String original_title = "original_title";
            final String movie_id = "id";
            JSONObject movieJson = null;
            String title;
            String view;
            String vote;
            String release;
            String poster;
            String mid;

            ArrayList<MovieModel> movieList = new ArrayList<>();

            try {
                movieJson = new JSONObject(movieJsonStr);
                JSONArray moviearray = movieJson.getJSONArray(list);
                for (int i = 0; i < moviearray.length(); i++) {
                    JSONObject movie_data = moviearray.getJSONObject(i);
                    mid = movie_data.getString(movie_id);
                    poster = movie_data.getString(poster_path);
                    view = movie_data.getString(overview);
                    release = movie_data.getString(release_date);
                    vote = movie_data.getString(vote_average);
                    title = movie_data.getString(original_title);
                    movieList.add(new MovieModel(mid, poster, title, view, vote, release));
                }
            } catch (JSONException e) {
                Log.e("Test", "JSONException: " + e.getMessage());

            }
            return movieList;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieModel> result) {

            if (result != null) {

                adapter = new MovieAdapter(getActivity(), result);
                gridView.setAdapter(adapter);
            }
        }
    }

    public interface Callback {
        public void setMovie(MovieModel mov);
    }
}