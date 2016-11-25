package app.com.example.ahmed.popmovie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ahmed on 10/11/16.
 */


public class MovieDBHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "MovieHelper.db";
    public static final String TABLE_NAME = "movie_dataBase";
    public static final String COL_ID = "ID";
    public static final String COL_TITLE = "Title";
    public static final String COL_VOTE = "Vote";
    public static final String COL_POSTER = "Poster";
    public static final String COL_OVERVIEW = "overview";
    public static final String COL_DATE = "Release";


    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        String SQL = "CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT , "
                + COL_ID + " INTEGER , "
                + COL_DATE + " String , "
                + COL_POSTER + " String , "
                + COL_TITLE + " String , "
                + COL_OVERVIEW +" String , "
                + COL_VOTE + " INTEGER)";

        db.execSQL(SQL);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

