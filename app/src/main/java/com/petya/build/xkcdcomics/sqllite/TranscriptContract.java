package com.petya.build.xkcdcomics.sqllite;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Petya Marinova on 7/29/2018.
 */
public class TranscriptContract {

    public static final String DB_NAME = "com.petya.build.xkcdcomics.sqllite.transcriptTable";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "transcriptTable";
    public static final String AUTHORITY = "com.petya.build.xkcdcomics.sqllite";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE);
    public static final int LIST_COMICS = 1;
    public static final int ITEM_COMICS_ = 2;
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/petya.transcriptDB/" + TABLE;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/petya/transcriptDB" + TABLE;

    public class Columns {

        public static final String _ID = BaseColumns._ID;
        public static final String TEXT = "text";

    }
}
