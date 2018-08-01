package com.petya.build.xkcdcomics.room;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

import com.petya.build.xkcdcomics.api.ComicResponse;

/**
 * Created by Petya Marinova on 7/25/2018.
 */
public class ComixContract {
    public static final String PROVIDER_AUTHORITY = "com.petya.build.xkcdcomics";

    public static class ComixEntry implements BaseColumns {
        public static final String TABLE_NAME = "ComixTable";
        public static final String DIR_BASE_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PROVIDER_AUTHORITY + "/" + TABLE_NAME;
        public static final String ITEM_BASE_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + PROVIDER_AUTHORITY + "/" + TABLE_NAME;


        public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_AUTHORITY + "/" + TABLE_NAME);
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_MONTH = "month";
        public static final String COLUMN_NUM = "num";
        public static final String COLUMN_LINK = "link";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_NEWS = "news";
        public static final String COLUMN_SAFE_TITLE = "safeTitle";
        public static final String COLUMN_TRANSCRIPT = "transcript";
        public static final String COLUMN_ALT = "alt";
        public static final String COLUMN_IMG = "img";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DAY = "day";


        public static ContentValues resolveComics(ComicResponse movie) {
            ContentValues values = new ContentValues();

            values.put(COLUMN_MONTH, movie.getMonth());
            values.put(COLUMN_NUM, movie.getNum());
            values.put(COLUMN_LINK, movie.getLink());
            values.put(COLUMN_YEAR, movie.getYear());
            values.put(COLUMN_NEWS, movie.getNews());

            values.put(COLUMN_SAFE_TITLE, movie.getSafeTitle());
            values.put(COLUMN_TRANSCRIPT, movie.getTranscript());
            values.put(COLUMN_ALT, movie.getAlt());
            values.put(COLUMN_IMG, movie.getImg());
            values.put(COLUMN_TITLE, movie.getTitle());
            values.put(COLUMN_DAY, movie.getDay());

            return values;
        }

    }
}
