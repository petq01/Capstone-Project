package com.petya.build.xkcdcomics.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;
import static com.petya.build.xkcdcomics.room.ComixContract.ComixEntry.*;

import com.petya.build.xkcdcomics.api.ComicResponse;

import java.util.List;

/**
 * Created by Petya Marinova on 7/25/2018.
 */
@Dao
public interface ComixDao {

    /**
     * Inserts a ComicResponse into the table.
     *
     * @param ComicResponse A new ComicResponse.
     * @return The row ID of the newly inserted ComicResponse.
     */
    @Insert
    long insert(ComicResponse ComicResponse);

    /**
     * Inserts multiple ComicResponses into the database
     *
     * @param ComicResponses An array of new ComicResponses.
     * @return The row IDs of the newly inserted ComicResponses.
     */
    @Insert
    long[] insertAll(ComicResponse[] ComicResponses);

    /**
     * Select all ComicResponses.
     *
     * @return A {@link Cursor} of all the ComicResponses in the table.
     */
    @Query("SELECT * FROM " + TABLE_NAME)
    Cursor selectAll();

    /**
     * Select a ComicResponse by the ID.
     *
     * @param id The row ID.
     * @return A {@link Cursor} of the selected ComicResponse.
     */
    @Query("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = :id")
    Cursor selectById(long id);
    @Query("SELECT * FROM " + TABLE_NAME + "  WHERE " + COLUMN_TITLE + " = :title LIMIT 1")
    ComicResponse findByTitle(String  title);
    @Query("SELECT * FROM " + TABLE_NAME + "  WHERE " + COLUMN_NUM + " = :num LIMIT 1")
    ComicResponse findByNum(Integer  num);

    /**
     * Delete a ComicResponse by the ID.
     *
     * @param id The row ID.
     * @return A number of ComicResponses deleted. This should always be {@code 1}.
     */
    @Query("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = :id")
    int deleteById(long id);

    @Delete
    void delete(ComicResponse ComicResponse);
    @Query("SELECT * FROM " + TABLE_NAME )
    LiveData<List<ComicResponse>> getAll();
    /**
     * Update the ComicResponse. The ComicResponse is identified by the row ID.
     *
     * @param ComicResponse The ComicResponse to update.
     * @return A number of ComicResponses updated. This should always be {@code 1}.
     */
    @Update
    int update(ComicResponse ComicResponse);
}
