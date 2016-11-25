package app.com.example.ahmed.popmovie;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

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
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    Context context;
    ToggleButton toggle;
    MovieModel MovieModel;
    private TextView title;
    private  TextView vote;
    private TextView overview;
    private TextView release;
    private ImageView poster;
    ArrayList movie;

    public DetailActivityFragment() {
    }

    public static DetailActivityFragment newInstance(MovieModel MovieModel) {
        DetailActivityFragment frg = new DetailActivityFragment();
        Bundle args = new Bundle();
        args.putParcelable("MovieModel", MovieModel);
        frg.setArguments(args);
        return frg;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View rootview = inflater.inflate(R.layout.fragment_detail, container, false);
         title = (TextView) rootview.findViewById(R.id.title);
         vote = (TextView)rootview.findViewById(R.id.vote_avreage);
         release= (TextView)rootview.findViewById(R.id.Release_data);
         poster= (ImageView)rootview.findViewById(R.id.imageView_detail);
         overview= (TextView)rootview.findViewById(R.id.View);
         Button ReviewB = (Button) rootview.findViewById(R.id.button_review);
         Button TrailerB= (Button) rootview.findViewById(R.id.button_trailer);
         toggle = (ToggleButton) rootview.findViewById(R.id.myToggleButton);

        ReviewB.setOnClickListener(this);
        TrailerB.setOnClickListener(this);
        toggle.setOnCheckedChangeListener(this);
        return rootview;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
            MovieModel = getArguments().getParcelable("MovieModel");


        title.setText(MovieModel.getTitle());
        vote.setText(MovieModel.getVote());
        overview.setText(MovieModel.getView());
        release.setText(MovieModel.getRelease());

        Picasso.with(context)
                .load("http://image.tmdb.org/t/p/w185" + MovieModel.getPoster())
                .into(poster);
        Log.i("pos", poster.toString());
        toggle.setChecked(getMovieIDs().contains(MovieModel.getID()));
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.button_review:
                fetchReview();
                break;
            case R.id.button_trailer:
                fetchTrailer();
                break;

        }
    }

    public boolean InsertData (MovieModel movie) {
        boolean CREATED=false;
        ContentValues values = new ContentValues();

        values.put(MovieDBHelper.COL_ID, movie.getID());
        values.put(MovieDBHelper.COL_DATE, movie.getRelease());
        values.put(MovieDBHelper.COL_POSTER, movie.getPoster());
        values.put(MovieDBHelper.COL_TITLE, movie.getTitle());
        values.put(MovieDBHelper.COL_VOTE, movie.getVote());
        values.put(MovieDBHelper.COL_OVERVIEW, movie.getView());



        MovieDBHelper  dbHelper = new MovieDBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        CREATED = db.insert(MovieDBHelper.TABLE_NAME, null, values) > 0;
        db.close();

        return CREATED;
    }

    public boolean DeleteData (MovieModel movie) {

        boolean Deleted=false;
        MovieDBHelper  dbHelper = new MovieDBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Deleted = db.delete(MovieDBHelper.TABLE_NAME, "ID = ?", new String[] { movie.getID() }) > 0;
        db.close();

        return Deleted;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
            // The toggle is enabled
            InsertData(MovieModel);

        } else {
            // The toggle is disable
            DeleteData(MovieModel);

        }

    }

    private ArrayList<String> getMovieIDs(){
        ArrayList<String> iDsList =new ArrayList<>();
        MovieDBHelper helper =new MovieDBHelper(getActivity());
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.query(MovieDBHelper.TABLE_NAME , new String[]
                        {MovieDBHelper.COL_ID},
                null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                String movie_id = c.getString(c.getColumnIndex(MovieDBHelper.COL_ID));



                iDsList.add(movie_id);

            } while (c.moveToNext());
        }

        return iDsList;
    }


    public class FetchTrailer extends AsyncTask<Void, Void, ArrayList<TrailerModel>> {
        private final String LOG_TAG = FetchTrailer.class.getSimpleName();
        @Override
        protected ArrayList doInBackground(Void... params) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            String TailerJson = null;
            final String BaseURL = ("http://api.themoviedb.org/3/movie/");
            Uri BuildUri = Uri.parse(BaseURL).buildUpon()
                    .appendPath(MovieModel.getID())
                    .appendPath("videos")
                    .appendQueryParameter("api_key", "5649c8a1382653f1cd95d5ef97604364")
                    .build();
            URL url = null;
            try {
                url = new URL(BuildUri.toString());
                Log.i("this is url ", BuildUri.toString());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line + "/n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                TailerJson = buffer.toString();
                Log.i("Trailer json", TailerJson);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (final IOException e) {
                    }
                }
            }
            try {
                return getIDdatafromJSON(TailerJson);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        public ArrayList<TrailerModel> getIDdatafromJSON(String TailerJson)
                throws JSONException {
            final String list = "results";
            String ID = "id";
            JSONObject movieJson = null;
            String[] resultstrs = null;
            movie = new ArrayList<>();
            try {
                movieJson = new JSONObject(TailerJson);
                JSONArray moviearray = movieJson.getJSONArray(list);
                resultstrs = new String[moviearray.length()];
                for (int i = 0; i < moviearray.length(); i++) {
                    JSONObject movie_data = moviearray.getJSONObject(i);
                    ID = movie_data.getString("key");
                    String name = movie_data.getString("name");
                    Log.i("ID is : ", ID);
                    movie.add(new TrailerModel(ID,name));
                }
            } catch (JSONException e) {
                Log.i("movie", movie.toString());
                {
                }
            }
            return movie;
        }
        @Override
        protected void onPostExecute(ArrayList<TrailerModel> res) {

            super.onPostExecute(res);
            Intent i = new Intent(getActivity(), TrailerActivity.class);
            i.putExtra("trailerList", res);
            startActivity(i);

        }
    }
    class FetchReview extends AsyncTask<Void, Void, ArrayList<ReviewModel>> {
        @Override
        protected ArrayList<ReviewModel> doInBackground(Void... params) {
            HttpURLConnection URLCon = null;
            BufferedReader buffered = null;
            String RevJson = null;
            final String BaseURl = ("https://api.themoviedb.org/3/movie/");
            Uri BuildUri = Uri.parse(BaseURl).buildUpon()
                    .appendPath(MovieModel.getID())
                    .appendPath("reviews")
                    .appendQueryParameter("api_key", "5649c8a1382653f1cd95d5ef97604364")
                    .build();

            try {
                URL url = new URL(BuildUri.toString());
                URLCon = (HttpURLConnection) url.openConnection();
                URLCon.setRequestMethod("GET");
                URLCon.connect();
                InputStream inputStream = URLCon.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                buffered = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = buffered.readLine()) != null) {
                    buffer.append(line + "/n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                RevJson = buffer.toString();
                Log.i("revjson", RevJson);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (URLCon != null) {
                    URLCon.disconnect();
                }
                if (buffered != null) {
                    try {
                        buffered.close();
                    } catch (final IOException e) {
                    }
                }
            }
            try {
                return GetViewfromJSon(RevJson);
            } catch (JSONException e) {

                e.printStackTrace();
            }
            return null;
        }
        public ArrayList<ReviewModel> GetViewfromJSon(String ViewJSONSTR) throws JSONException {
            final String list = "results";
            String ID = "id";
            JSONObject movieJson = null;
            String[] resultstrs = null;
            ArrayList<ReviewModel> movierev = new ArrayList<>();
            try {
                movieJson = new JSONObject(ViewJSONSTR);
                JSONArray moviearray = movieJson.getJSONArray(list);
                for (int i = 0; i < moviearray.length(); i++) {
                    JSONObject movie_data = moviearray.getJSONObject(i);
                    String auther = movie_data.getString("author");
                    String content = movie_data.getString("content");
                    Log.i("auther", auther);
                    movierev.add(new ReviewModel(auther, content));
                }
            } catch (JSONException e) {
                Log.i("movieresult", resultstrs.toString());
                {
                }
            }
            return movierev;
        }
        @Override
        protected void onPostExecute(ArrayList<ReviewModel> rev) {
            super.onPostExecute(rev);
            Log.i("rev", rev.toString());
            Intent i = new Intent(getActivity(), ReviewActivity.class);
            i.putExtra("revList", rev);
            startActivity(i);
            Log.i("onPostExecute", "Startactivity!!!!");
        }
    }
    public void fetchReview(){
        new FetchReview().execute();
    }
    public void fetchTrailer(){
        FetchTrailer ft = new FetchTrailer();
        ft.execute();
    }
}