package com.petya.build.xkcdcomics.room;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.petya.build.xkcdcomics.api.ComicResponse;

import java.util.List;

/**
 * Created by Petya Marinova on 8/1/2018.
 */
public class ComicsViewModel extends AndroidViewModel {

    private ComixRepository mRepository;

    private LiveData<List<ComicResponse>> mAllComics;

    public ComicsViewModel(Application application) {
        super(application);
        mRepository = new ComixRepository(application);
        mAllComics = mRepository.getAll();
    }

   public LiveData<List<ComicResponse>> getAll() {
        return mAllComics;
    }
}