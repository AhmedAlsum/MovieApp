package app.com.example.ahmed.popmovie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ahmed on 14/11/16.
 */


public class ReviewActivity extends AppCompatActivity {
    public ArrayAdapter<String> ReviewAdapter;
    private ListView listView;
    ArrayList<ReviewModel> Revlist;
    @Override
    //back button
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_activity);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent i =  getIntent();
        Revlist = i.getParcelableArrayListExtra("reviewlist");


        listView= (ListView) findViewById(R.id.reviewlist);
        ReviewAdapter adapter= new ReviewAdapter();
        listView.setAdapter(adapter);

    }
    public class ReviewAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return Revlist.size();
        }

        @Override
        public Object getItem(int position) {
            return Revlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder= null;
            if (convertView==null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item_in_review, parent, false);
                holder = new ViewHolder();
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder)convertView.getTag();
            }
            holder.tv= (TextView)convertView.findViewById(R.id.contentxt);
            holder.Tev= (TextView)convertView.findViewById(R.id.outhertxt);
            ReviewModel v= (ReviewModel) getItem(position);
            holder.tv.setText(v.getContent());
            holder.Tev.setText(v.getOuther());
            return convertView;
        }


    }

    public class ViewHolder{
        TextView tv;
        TextView Tev;

    }





}
