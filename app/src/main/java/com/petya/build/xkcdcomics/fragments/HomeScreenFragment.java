package com.petya.build.xkcdcomics.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.petya.build.xkcdcomics.R;
import com.petya.build.xkcdcomics.api.ComicResponse;
import com.petya.build.xkcdcomics.job.NotifyNewComicsJob;

import java.util.Random;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.petya.build.xkcdcomics.Constants.IMAGE_SELECTED;
import static com.petya.build.xkcdcomics.Constants.NUMBER_COMICS_SELECTED;
import static com.petya.build.xkcdcomics.api.RetrofitRequest.createAPI;

/**
 * Created by Petya Marinova on 7/24/2018.
 */
public class HomeScreenFragment extends Fragment implements View.OnClickListener{
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    private static final String JOB_TAG = "NotifyNewComicsJob";
    private FirebaseJobDispatcher mDispatcher;

    public static HomeScreenFragment newInstance() {

        Bundle args = new Bundle();

        HomeScreenFragment fragment = new HomeScreenFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, v);
        editor.putInt(NUMBER_COMICS_SELECTED, 0).apply();
        editor.putString(IMAGE_SELECTED, null).apply();
        AdView mAdView = v.findViewById(R.id.adView);
        AdRequest request = new AdRequest.Builder()
                .addTestDevice(getResources().getString(R.string.banner_ad_unit_id))  // An example device ID
                .build();
        mAdView.loadAd(request);


        v.findViewById(R.id.btn_schedule).setOnClickListener(this);
        v.findViewById(R.id.btn_cancel).setOnClickListener(this);

        mDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getActivity()));
        return v;
    }

    @OnClick(R.id.current_btn)
    public void currentComic() {
        comicsRequest(createAPI().getCurrentComic());
    }

    @OnClick(R.id.random_btn)
    public void randomComix() {

        Random rand = new Random();
        int n = rand.nextInt(2022) + 1;
        comicsRequest(createAPI().getComicById(n));
    }
    @OnClick(R.id.fav_list)
    public void  listFavorites(){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_fragments, PersonalFragment.newInstance(), PersonalFragment.class.getSimpleName());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void comicsRequest(final Observable<ComicResponse> comixResponse) {

        comixResponse.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responses -> {
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container_fragments, DetailComicFragment.newInstance(responses), DetailComicFragment.class.getSimpleName());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                });


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_schedule: scheduleJob(); break;
            case R.id.btn_cancel: cancelJob(JOB_TAG); break;
        }
    }

    private void scheduleJob() {
        Job myJob = mDispatcher.newJobBuilder()
                .setService(NotifyNewComicsJob.class)
                .setTag(JOB_TAG)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(5, 30))
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setReplaceCurrent(false)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .build();
        mDispatcher.mustSchedule(myJob);
        Toast.makeText(getActivity(), "Job scheduled", Toast.LENGTH_LONG).show();
    }

    private void cancelJob(String jobTag) {
        if ("".equals(jobTag)) {
            mDispatcher.cancelAll();
        } else {
            mDispatcher.cancel(jobTag);
        }
        Toast.makeText(getActivity(), "Job canceled", Toast.LENGTH_LONG).show();
    }
}
