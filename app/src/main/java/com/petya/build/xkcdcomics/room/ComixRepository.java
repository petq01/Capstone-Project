package com.petya.build.xkcdcomics.room;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.petya.build.xkcdcomics.api.ComicResponse;

import java.util.List;

/**
 * Created by Petya Marinova on 8/1/2018.
 */
public class ComixRepository {
    private ComixDao comixDao;
    private LiveData<List<ComicResponse>> listComicResponses;
    public ComixRepository(Application application) {
        SampleDatabase db = SampleDatabase.getInstance(application);
        comixDao = db.comixDao();
        listComicResponses = comixDao.getAll();
    }
  public   LiveData<List<ComicResponse>> getAll() {
        return listComicResponses;
    }
}
