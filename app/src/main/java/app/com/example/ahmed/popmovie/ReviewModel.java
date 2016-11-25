package app.com.example.ahmed.popmovie;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ahmed on 14/11/16.
 */

public class ReviewModel implements Parcelable{
    private String outher;
    private String content;

    public ReviewModel(){}

    public ReviewModel(String outer, String content) {
        this.outher = outer;
        this.content = content;
    }

    public String getOuther() {
        return outher;

    }

    public void setOuther(String outer) {
        this.outher = outer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    protected ReviewModel(Parcel in) {
        outher = in.readString();
        content = in.readString();
    }

    public static final Creator<ReviewModel> CREATOR = new Creator<ReviewModel>() {
        @Override
        public ReviewModel createFromParcel(Parcel in) {
            return new ReviewModel(in);
        }

        @Override
        public ReviewModel[] newArray(int size) {
            return new ReviewModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(outher);
        parcel.writeString(content);
    }
}
