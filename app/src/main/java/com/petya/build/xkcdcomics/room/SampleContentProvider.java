package com.petya.build.xkcdcomics.room;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import static com.petya.build.xkcdcomics.room.ComixContract.ComixEntry.*;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.petya.build.xkcdcomics.api.ComicResponse;

import java.util.ArrayList;

/**
 * Created by Petya Marinova on 7/25/2018.
 */
public class SampleContentProvider extends ContentProvider {

    /** The authority of this content provider. */
    public static final String AUTHORITY = "com.petya.build.xkcdcomics";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    /** The URI for the Comix table. */
    public static final Uri URI_COMIX = Uri.parse(
            "content://" + AUTHORITY + "/" + TABLE_NAME);
    public static final Uri URI_TRANSCRIPT = Uri.parse(
            "content://" + AUTHORITY + "/" + "TranscriptTable");

    /** The match code for some items in the Cheese table. */
    private static final int CODE_FAV_DIR = 1;

    /** The match code for an item in the Cheese table. */
    private static final int CODE_FAV_ITEM = 2;

    /** The URI matcher. */
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, TABLE_NAME, CODE_FAV_DIR);
        MATCHER.addURI(AUTHORITY, TABLE_NAME + "/*", CODE_FAV_ITEM);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int code = MATCHER.match(uri);
        if (code == CODE_FAV_DIR || code == CODE_FAV_ITEM) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }
            ComixDao comicResponseDao = SampleDatabase.getInstance(context).comixDao();
            final Cursor cursor;
            if (code == CODE_FAV_DIR) {
                cursor = comicResponseDao.selectAll();
            }
            else {
                cursor = comicResponseDao.selectById(ContentUris.parseId(uri));
            }
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case CODE_FAV_DIR:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + TABLE_NAME;
            case CODE_FAV_ITEM:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (MATCHER.match(uri)) {
            case CODE_FAV_DIR:
                final Context context = getContext();
                if (context == null) {
                    return null;
                }
                final long id = SampleDatabase.getInstance(context).comixDao()
                        .insert(ComicResponse.fromContentValues(values));
                context.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            case CODE_FAV_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case CODE_FAV_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case CODE_FAV_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final int count = SampleDatabase.getInstance(context).comixDao()
                        .deleteById(ContentUris.parseId(uri));
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case CODE_FAV_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case CODE_FAV_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final ComicResponse comicResponse = ComicResponse.fromContentValues(values);
                comicResponse.uid = ContentUris.parseId(uri);
                final int count = SampleDatabase.getInstance(context).comixDao()
                        .update(comicResponse);
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(
            @NonNull ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final Context context = getContext();
        if (context == null) {
            return new ContentProviderResult[0];
        }
        final SampleDatabase database = SampleDatabase.getInstance(context);
        database.beginTransaction();
        try {
            final ContentProviderResult[] result = super.applyBatch(operations);
            database.setTransactionSuccessful();
            return result;
        } finally {
            database.endTransaction();
        }
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] valuesArray) {
        switch (MATCHER.match(uri)) {
            case CODE_FAV_DIR:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final SampleDatabase database = SampleDatabase.getInstance(context);
                final ComicResponse[] ComicResponseS = new ComicResponse[valuesArray.length];
                for (int i = 0; i < valuesArray.length; i++) {
                    ComicResponseS[i] = ComicResponse.fromContentValues(valuesArray[i]);
                }
                return database.comixDao().insertAll(ComicResponseS).length;
            case CODE_FAV_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

}