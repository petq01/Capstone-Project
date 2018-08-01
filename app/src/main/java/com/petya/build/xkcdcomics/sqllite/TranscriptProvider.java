package com.petya.build.xkcdcomics.sqllite;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by Petya Marinova on 7/29/2018.
 */
public class TranscriptProvider extends ContentProvider {

    private SQLiteDatabase db;
    private TranscriptDBHelper transcriptDBHelper;
    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(TranscriptContract.AUTHORITY, TranscriptContract.TABLE, TranscriptContract.LIST_COMICS);
        uriMatcher.addURI(TranscriptContract.AUTHORITY, TranscriptContract.TABLE + "/#", TranscriptContract.ITEM_COMICS_);
    }

    @Override
    public boolean onCreate() {

        transcriptDBHelper = new TranscriptDBHelper(getContext());
        db = transcriptDBHelper.getWritableDatabase();

        if (db == null) {

            return false;

        } else if (db.isReadOnly()) {

            db.close();
            db = null;
            return false;
        }

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TranscriptContract.TABLE);

        switch (uriMatcher.match(uri)) {
            case TranscriptContract.LIST_COMICS:
                break;

            case TranscriptContract.ITEM_COMICS_:
                qb.appendWhere(TranscriptContract.Columns._ID + " = " + uri.getLastPathSegment());
                break;

            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }

        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, null);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {

        switch (uriMatcher.match(uri)) {
            case TranscriptContract.LIST_COMICS:
                return TranscriptContract.CONTENT_TYPE;

            case TranscriptContract.ITEM_COMICS_:
                return TranscriptContract.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        if (uriMatcher.match(uri) != TranscriptContract.LIST_COMICS) {
            throw new IllegalArgumentException("Invalid URI: " + uri);
        }

        long id = db.insert(TranscriptContract.TABLE, null, contentValues);

        if (id > 0) {
            return ContentUris.withAppendedId(uri, id);
        }
        throw new SQLException("Error inserting into table: " + TranscriptContract.TABLE);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int deleted = 0;

        switch (uriMatcher.match(uri)) {
            case TranscriptContract.LIST_COMICS:
                db.delete(TranscriptContract.TABLE, selection, selectionArgs);
                break;

            case TranscriptContract.ITEM_COMICS_:
                String where = TranscriptContract.Columns._ID + " = " + uri.getLastPathSegment();
                if (!selection.isEmpty()) {
                    where += " AND " + selection;
                }

                deleted = db.delete(TranscriptContract.TABLE, where, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }

        return deleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {

        int updated = 0;

        switch (uriMatcher.match(uri)) {
            case TranscriptContract.LIST_COMICS:
                db.update(TranscriptContract.TABLE, contentValues, s, strings);
                break;

            case TranscriptContract.ITEM_COMICS_:
                String where = TranscriptContract.Columns._ID + " = " + uri.getLastPathSegment();
                if (!s.isEmpty()) {
                    where += " AND " + s;
                }
                updated = db.update(TranscriptContract.TABLE, contentValues, where, strings);
                break;

            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }

        return updated;
    }
}