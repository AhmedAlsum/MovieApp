package app.com.example.ahmed.popmovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends AppCompatActivity implements MovieFragment.Callback{

    boolean tab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if((findViewById(R.id.fragment))!=null){
            tab = true;
        }else{
            tab = false;
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void setMovie(MovieModel mov) {
        if(tab){
            DetailActivityFragment detailFragment = DetailActivityFragment.newInstance(mov);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,detailFragment).commit();

        }else{
            Intent intent = new Intent(MainActivity.this, DetailActivity.class)
                    .putExtra("mymovie", mov);
            startActivity(intent);
        }
    }
}
