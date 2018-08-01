
package com.petya.build.xkcdcomics.api;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static com.petya.build.xkcdcomics.room.ComixContract.ComixEntry.*;

@Entity(tableName = TABLE_NAME)
public class ComicResponse implements Parcelable {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    public long uid;
    @SerializedName("month")
    @Expose
    @ColumnInfo(name = COLUMN_MONTH)
    private String month;
    @SerializedName("num")
    @Expose
    @ColumnInfo(name = COLUMN_NUM)
    private Integer num;
    @SerializedName("link")
    @Expose
    @ColumnInfo(name = COLUMN_LINK)
    private String link;
    @SerializedName("year")
    @Expose
    @ColumnInfo(name = COLUMN_YEAR)
    private String year;
    @SerializedName("news")
    @Expose
    @ColumnInfo(name = COLUMN_NEWS)
    private String news;
    @SerializedName("safe_title")
    @Expose
    @ColumnInfo(name = COLUMN_SAFE_TITLE)
    private String safeTitle;
    @SerializedName("transcript")
    @Expose
    @ColumnInfo(name = COLUMN_TRANSCRIPT)
    private String transcript;
    @SerializedName("alt")
    @Expose
    @ColumnInfo(name = COLUMN_ALT)
    private String alt;
    @SerializedName("img")
    @Expose
    @ColumnInfo(name = COLUMN_IMG)
    private String img;
    @SerializedName("title")
    @Expose
    @ColumnInfo(name = COLUMN_TITLE)
    private String title;
    @SerializedName("day")
    @Expose
    @ColumnInfo(name = COLUMN_DAY)
    private String day;

    public static ComicResponse fromContentValues(ContentValues values) {
        final ComicResponse comicResponse = new ComicResponse();
        if (values.containsKey(COLUMN_ID)) {
            comicResponse.uid = values.getAsLong(COLUMN_ID);
        }
        if (values.containsKey(COLUMN_MONTH)) {
            comicResponse.month = values.getAsString(COLUMN_MONTH);
        }
        if (values.containsKey(COLUMN_NUM)) {
            comicResponse.num = values.getAsInteger(COLUMN_NUM);
        }
        if (values.containsKey(COLUMN_LINK)) {
            comicResponse.link = values.getAsString(COLUMN_LINK);
        }
        if (values.containsKey(COLUMN_YEAR)) {
            comicResponse.year = values.getAsString(COLUMN_YEAR);
        }
        if (values.containsKey(COLUMN_NEWS)) {
            comicResponse.news = values.getAsString(COLUMN_NEWS);
        }

        if (values.containsKey(COLUMN_SAFE_TITLE)) {
            comicResponse.safeTitle = values.getAsString(COLUMN_SAFE_TITLE);
        }
        if (values.containsKey(COLUMN_TRANSCRIPT)) {
            comicResponse.transcript = values.getAsString(COLUMN_TRANSCRIPT);
        }
        if (values.containsKey(COLUMN_ALT)) {
            comicResponse.alt = values.getAsString(COLUMN_ALT);
        }
        if (values.containsKey(COLUMN_IMG)) {
            comicResponse.img = values.getAsString(COLUMN_IMG);
        }
        if (values.containsKey(COLUMN_TITLE)) {
            comicResponse.title = values.getAsString(COLUMN_TITLE);
        }
        if (values.containsKey(COLUMN_DAY)) {
            comicResponse.day = values.getAsString(COLUMN_DAY);
        }
        return comicResponse;
    }

    protected ComicResponse(Parcel in) {
        month = in.readString();
        num = in.readInt();
        link = in.readString();
        year = in.readString();

        news = in.readString();
        safeTitle = in.readString();
        transcript = in.readString();

        alt = in.readString();
        img = in.readString();
        title = in.readString();
        day = in.readString();
    }

    public ComicResponse() {
    }

    public static final Creator<ComicResponse> CREATOR = new Creator<ComicResponse>() {
        @Override
        public ComicResponse createFromParcel(Parcel in) {
            ComicResponse comicResponse = new ComicResponse();
            comicResponse.setMonth(in.readString());
            comicResponse.setNum(in.readInt());
            comicResponse.setLink(in.readString());
            comicResponse.setYear(in.readString());
            comicResponse.setNews(in.readString());
            comicResponse.setSafeTitle(in.readString());
            comicResponse.setTranscript(in.readString());
            comicResponse.setAlt(in.readString());
            comicResponse.setImg(in.readString());
            comicResponse.setTitle(in.readString());
            comicResponse.setDay(in.readString());
            return comicResponse;
        }

        @Override
        public ComicResponse[] newArray(int size) {
            return new ComicResponse[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(month);
        dest.writeInt(num);
        dest.writeString(link);
        dest.writeString(year);
        dest.writeString(news);
        dest.writeString(safeTitle);
        dest.writeString(transcript);
        dest.writeString(alt);
        dest.writeString(img);
        dest.writeString(title);
        dest.writeString(day);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public String getSafeTitle() {
        return safeTitle;
    }

    public void setSafeTitle(String safeTitle) {
        this.safeTitle = safeTitle;
    }

    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

}
