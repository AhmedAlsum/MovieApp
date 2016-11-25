package app.com.example.ahmed.popmovie;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ahmed on 31/10/16.
 */

public class MovieModel implements Parcelable {
    private String poster;
    private String title;
    private String overview;
    private String vote;
    private String release;
    private String ID;
    private int Data;
    ArrayList<ReviewModel> movreview;
    ArrayList<TrailerModel> movtrailer;

    public ArrayList<ReviewModel> getMovreview() {
        return movreview;
    }

    public void setMovreview(ArrayList<ReviewModel> movreview) {
        this.movreview = movreview;
    }

    public ArrayList<TrailerModel> getMovtrailer() {
        return movtrailer;
    }

    public void setMovtrailer(ArrayList<TrailerModel> movtrailer) {
        this.movtrailer = movtrailer;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getData() {
        return Data;
    }

    public void setData(int data) {
        Data = data;
    }

    public int describeContents() {
        return 0;
    }


    public void writeToParcel(Parcel out, int flags) {

        out.writeString(ID);
        out.writeString(poster);
        out.writeString(title);
        out.writeString(overview);
        out.writeString(vote);
        out.writeString(release);


    }

    private void readFromParcel(Parcel in) {

        ID = in.readString();
        poster = in.readString();
        title = in.readString();
        overview = in.readString();
        vote = in.readString();
        release = in.readString();

    }

    public static final Parcelable.Creator<MovieModel> CREATOR
            = new Parcelable.Creator<MovieModel>() {
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    public MovieModel(Parcel in) {
        ID = in.readString();

        poster = in.readString();
        title = in.readString();
        overview = in.readString();
        vote = in.readString();
        release = in.readString();
    }


    public String getView() {
        return overview;
    }

    public String getPoster() {
        return poster;
    }

    public String getVote() {
        return vote;
    }

    public String getRelease() {
        return release;
    }

    public String getTitle() {
        return title;
    }

    public MovieModel() {
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setView(String view) {
        this.overview = view;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getmData() {
        return Data;
    }

    public void setmData(int mData) {
        this.Data = mData;
    }

    public String getID() {
        return ID;
    }


    public MovieModel(String ID, String poster, String title, String view, String vote, String release) {
        this.release = release;
        this.overview = view;
        this.vote = vote;
        this.title = title;
        this.poster = poster;
        this.ID = ID;
    }


}
