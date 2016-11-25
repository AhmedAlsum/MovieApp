package app.com.example.ahmed.popmovie;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ahmed on 14/11/16.
 */

public class TrailerModel implements Parcelable{
    private String trailer_id;
    private String trailer_name;

    public TrailerModel(String trailer_id, String trailer_name) {
        this.trailer_id = trailer_id;
        this.trailer_name = trailer_name;
    }

    public String getTrailer_id() {
        return trailer_id;
    }

    public void setTrailer_id(String trailer_id) {
        this.trailer_id = trailer_id;
    }

    public String getTrailer_name() {
        return trailer_name;
    }

    public void setTrailer_name(String trailer_name) {
        this.trailer_name = trailer_name;
    }

    protected TrailerModel(Parcel in) {
        trailer_id = in.readString();
        trailer_name = in.readString();
    }

    public static final Creator<TrailerModel> CREATOR = new Creator<TrailerModel>() {
        @Override
        public TrailerModel createFromParcel(Parcel in) {
            return new TrailerModel(in);
        }

        @Override
        public TrailerModel[] newArray(int size) {
            return new TrailerModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(trailer_id);
        parcel.writeString(trailer_name);
    }
}
