package app.com.example.ahmed.popmovie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ahmed on 14/11/16.
 */

public class TrailerActivity extends AppCompatActivity {
    public ArrayAdapter TailerAdapter;
    ArrayList<TrailerModel> trList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trailer_activity);
        trList = getIntent().getParcelableArrayListExtra("trailerList");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);



        TailerAdapter adapter= new TailerAdapter();
        ListView lis = (ListView) findViewById(R.id.trailerlist);
        lis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TrailerModel t = (TrailerModel) parent.getItemAtPosition(position);
                Intent i = new Intent(Intent.ACTION_VIEW , Uri.parse("https://www.youtube.com/watch?v="+t.getTrailer_id()));
                startActivity(i);
            }
        });
        lis.setAdapter(adapter);
    }

    public class TailerAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return trList.size();
        }

        @Override
        public Object getItem(int position) {
            return trList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item_in_trailer, parent, false);
                holder = new ViewHolder();
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv = (TextView) convertView.findViewById(R.id.trailertxt);
            TrailerModel v = trList.get(position);

            holder.tv.setText(v.getTrailer_name());

            return convertView;
        }

        public class ViewHolder {
            TextView tv;


        }
    }
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
}