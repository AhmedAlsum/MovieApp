package app.com.example.ahmed.popmovie;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ahmed on 31/10/16.
 */

public class MovieAdapter extends BaseAdapter {
    Context context;
    ArrayList<MovieModel> movie;
    public MovieAdapter(Context context, ArrayList<MovieModel> movie) {
        this.context = context;
        this.movie = movie;

    }

    public MovieAdapter(FragmentActivity activity) {

    }

    @Override
    public int getCount() {

        return movie.size();
    }
    @Override
    public long getItemId(int position) {

        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);



        View v = inflater.inflate(R.layout.list_item_movie, parent, false);
          ImageView iv= (ImageView) v.findViewById(R.id.imageView_film);
            v.setTag(iv);

            MovieModel m = getItem(position);


           Picasso.with(context)
                    .load("http://image.tmdb.org/t/p/w185" + m.getPoster())
                    .into(iv);

        return v;
    }
    @Override
    public MovieModel getItem(int i) {

        return movie.get(i);
    }
}
