package com.petya.build.xkcdcomics.job;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.petya.build.xkcdcomics.api.ComicResponse;

import java.text.DateFormat;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.petya.build.xkcdcomics.Constants.NEW_COMIX_ARRIVED;
import static com.petya.build.xkcdcomics.api.RetrofitRequest.createAPI;


/**
 * Created by Petya Marinova on 7/30/2018.
 */
public class NotifyNewComicsJob extends JobService {
    private static final String TAG = "NotifyNewComicsJob";
    SharedPreferences.Editor editor;
    SharedPreferences pref;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        Log.d(TAG, currentDateTimeString);
        pref = getApplication().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        AsyncTask.execute(() -> comicsRequest(createAPI().getCurrentComic()));

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "Job cancelled!");
        return false;
    }


    public void comicsRequest(final Observable<ComicResponse> comixResponse) {

        comixResponse.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responses -> {
                    if (!responses.getNum().equals(pref.getInt(NEW_COMIX_ARRIVED, 1))) {
                        if(responses.getTranscript().isEmpty()){
                            NotificationUtils.remindUserNewComixArrived(this, responses.getTitle(), responses.getAlt());
                        }else{
                            NotificationUtils.remindUserNewComixArrived(this, responses.getTitle(), responses.getTranscript());
                        }

                        editor.putInt(NEW_COMIX_ARRIVED, responses.getNum()).apply();
                    }
                }, throwable -> {
                    System.out.println(throwable);
                });


    }


}