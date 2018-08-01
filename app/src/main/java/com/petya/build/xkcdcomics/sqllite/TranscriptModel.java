package com.petya.build.xkcdcomics.sqllite;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Petya Marinova on 7/29/2018.
 */
public class TranscriptModel implements Parcelable {

    @SerializedName("text")
    @Expose
    private String text;
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
    }
    public static final Creator<TranscriptModel> CREATOR = new Creator<TranscriptModel>() {
        @Override
        public TranscriptModel createFromParcel(Parcel in) {
            TranscriptModel movie = new TranscriptModel();
            movie.setText(in.readString());
            return movie;
        }

        @Override
        public TranscriptModel[] newArray(int size) {
            return new TranscriptModel[size];
        }
    };

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Transcript{" +
                "transcript=" + text +
                '}';
    }


}