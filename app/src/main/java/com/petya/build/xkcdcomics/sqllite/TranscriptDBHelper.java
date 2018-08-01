package com.petya.build.xkcdcomics.sqllite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Petya Marinova on 7/29/2018.
 */
public class TranscriptDBHelper extends SQLiteOpenHelper {

    public TranscriptDBHelper(Context context) {

        super(context, TranscriptContract.DB_NAME, null, TranscriptContract.DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {

        String sqlQuery = "CREATE TABLE " + TranscriptContract.TABLE + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TranscriptContract.Columns.TEXT + " TEXT " + ")";

        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TranscriptContract.TABLE);
        onCreate(sqlDB);
    }
}