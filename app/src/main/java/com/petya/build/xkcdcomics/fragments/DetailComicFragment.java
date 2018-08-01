package com.petya.build.xkcdcomics.fragments;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.petya.build.xkcdcomics.Constants;
import com.petya.build.xkcdcomics.R;
import com.petya.build.xkcdcomics.api.ComicResponse;
import com.petya.build.xkcdcomics.sqllite.TranscriptModel;
import com.petya.build.xkcdcomics.sqllite.TranscriptContract;
import com.petya.build.xkcdcomics.room.SampleContentProvider;
import com.petya.build.xkcdcomics.room.SampleDatabase;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;

import static com.petya.build.xkcdcomics.Constants.IMAGE_SELECTED;
import static com.petya.build.xkcdcomics.Constants.KEY_COMIC_RESPONSE;
import static com.petya.build.xkcdcomics.Constants.NUMBER_COMICS_SELECTED;
import static com.petya.build.xkcdcomics.Constants.SHAREDPREF;
import static com.petya.build.xkcdcomics.api.RetrofitRequest.createAPI;
import static com.petya.build.xkcdcomics.room.ComixContract.ComixEntry.*;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Petya Marinova on 7/24/2018.
 */
public class DetailComicFragment extends Fragment {
    @BindView(R.id.title_comix)
    TextView titleComix;
    @BindView(R.id.tv_day)
    TextView tvDay;
    @BindView(R.id.tv_transcript)
    TextView tvTranscript;
    @BindView(R.id.iv_comix)
    ImageView ivComic;
    @BindView(R.id.fav)
    ImageView btnFav;
    @BindView(R.id.unfav)
    ImageView btnUnFav;
    @BindView(R.id.addWidget)
    ImageView addWidget;
    ComicResponse comixBundle;

    SharedPreferences.Editor editor;
    SharedPreferences pref;
    private static Integer number = 0;
    private static String image;
    private int lastComicNumber = 0;
    private int detailId;
    private String transcriptSave;
    private ComicResponse comicResponseSave;

    SharedPreferences mSharedPreferences;

    public static DetailComicFragment newInstance(ComicResponse comicResponse) {
        Bundle args = new Bundle();
        DetailComicFragment fragment = new DetailComicFragment();
        args.putParcelable(KEY_COMIC_RESPONSE, comicResponse);
        args.putString(IMAGE_SELECTED, image);
        args.putInt(NUMBER_COMICS_SELECTED, number);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        comixBundle = new ComicResponse();
        pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail_comix, container, false);
        ButterKnife.bind(this, v);

        mSharedPreferences = getActivity().getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE);

        Bundle bundle = getArguments();
        if (bundle != null) {
            comixBundle = bundle.getParcelable(KEY_COMIC_RESPONSE);
            if (comicResponseSave == null) {
                comicResponseSave = comixBundle;
            }
            if (transcriptSave == null) {
                if (comixBundle.getTranscript().isEmpty()) {
                    transcriptSave = comixBundle.getAlt();
                } else {
                    transcriptSave = comixBundle.getTranscript();
                }

            }

            lastComix(createAPI().getCurrentComic());
            if (pref.getInt(NUMBER_COMICS_SELECTED, 1) != 0) {
                number = pref.getInt(NUMBER_COMICS_SELECTED, 1);
                image = pref.getString(IMAGE_SELECTED, "");
                if (number > 0 && number <= lastComicNumber) {
                    comicsRequest(createAPI().getComicById(number));
                    editor.putInt(NUMBER_COMICS_SELECTED, number).apply();
                    editor.putString(IMAGE_SELECTED, image).apply();
                }
            } else {
                number = comixBundle.getNum();
                image = comixBundle.getImg();
                checkMovieFavorite(comicResponseSave.getTitle());
                editor.putInt(NUMBER_COMICS_SELECTED, number).apply();
                editor.putString(IMAGE_SELECTED, image).apply();
                titleComix.setText(String.format("%s   #%s", comixBundle.getTitle(), comixBundle.getNum()));
                Picasso.with(getActivity()).load(comixBundle.getImg()).placeholder(R.drawable.ic_none).error(R.drawable.ic_error).into(ivComic);
                tvDay.setText(String.format("%s/%s/%s", comixBundle.getDay(), comixBundle.getMonth(), comixBundle.getYear()));
                if (comixBundle.getTranscript().isEmpty()) {
                    tvTranscript.setText(comixBundle.getAlt());
                } else {
                    tvTranscript.setText(comixBundle.getTranscript());
                }

            }


        }
        return v;
    }

    @OnClick(R.id.addWidget)
    public void addToWidget() {
        String transcript = comicResponseSave.getTranscript();

        mSharedPreferences.edit().putString(Constants.PREF_TITLE, comicResponseSave.getTitle()).apply();


        Uri uri1 = TranscriptContract.CONTENT_URI;
        Cursor cursor = getActivity().getContentResolver().query(uri1, null, null, null, null);


        if (cursor != null) {

            //Delete the existing data
            while (cursor.moveToNext()) {
                Uri uri2 = TranscriptContract.CONTENT_URI;
                getActivity().getContentResolver().delete(uri2,
                        TranscriptContract.Columns._ID + "=?",
                        new String[]{cursor.getString(0)});

            }

            //Insert into database
            ContentValues values = new ContentValues();

//            for (Ingredient ingredient : ingredients) {
            values.clear();
            if (transcript.isEmpty()) {
                transcript = comicResponseSave.getTitle();
            }
            values.put(TranscriptContract.Columns.TEXT, transcript);


            Uri uri = TranscriptContract.CONTENT_URI;
            getActivity().getContentResolver().insert(uri, values);
        }


        int[] ids = AppWidgetManager.getInstance(getActivity())
                .getAppWidgetIds(new ComponentName(getActivity(), AppWidgetProvider.class));
        AppWidgetProvider ingredientWidget = new AppWidgetProvider();
        ingredientWidget.onUpdate(getActivity(), AppWidgetManager.getInstance(getActivity()), ids);
        Context context = getActivity().getApplicationContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, AppWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ing_widget_list);
        Toast.makeText(getActivity(), "successfully added", Toast.LENGTH_LONG).show();

    }

    public void addWidget() {
        TranscriptModel transcriptModel = new TranscriptModel();
        if (!transcriptSave.isEmpty()) {
            transcriptModel.setText(transcriptSave);
        } else {
            transcriptModel.setText(comicResponseSave.getTitle());
        }


        mSharedPreferences.edit().putString(Constants.PREF_TITLE, comicResponseSave.getTitle()).apply();


        Uri uri1 = TranscriptContract.CONTENT_URI;
        Cursor cursor = getActivity().getContentResolver().query(uri1, null, null, null, null);


        if (cursor != null) {

            //Delete the existing data
            while (cursor.moveToNext()) {
                Uri uri2 = TranscriptContract.CONTENT_URI;
                getActivity().getContentResolver().delete(uri2,
                        TranscriptContract.Columns._ID + "=?",
                        new String[]{cursor.getString(0)});

            }

            //Insert into database
            ContentValues values = new ContentValues();

            values.clear();

            values.put(TranscriptContract.Columns.TEXT, transcriptModel.getText());


            Uri uri = TranscriptContract.CONTENT_URI;
            getActivity().getContentResolver().insert(uri, values);
        }

        int[] ids = AppWidgetManager.getInstance(getActivity())
                .getAppWidgetIds(new ComponentName(getActivity(), AppWidgetProvider.class));
        AppWidgetProvider ingredientWidget = new AppWidgetProvider();
        ingredientWidget.onUpdate(getActivity(), AppWidgetManager.getInstance(getActivity()), ids);
        Context context = getActivity().getApplicationContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, AppWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ing_widget_list);
        Toast.makeText(getActivity(), "successfully added", Toast.LENGTH_LONG).show();
    }

    public void checkMovieFavorite(String title) {
        AsyncTask.execute(() -> {


            if (SampleDatabase.getInstance(getActivity()).comixDao().findByTitle(title) != null) {


                getActivity().runOnUiThread(() -> {
                    btnUnFav.setVisibility(View.VISIBLE);
                    btnFav.setVisibility(View.GONE);
                });
            } else {
                getActivity().runOnUiThread(() -> {
                    btnUnFav.setVisibility(View.GONE);
                    btnFav.setVisibility(View.VISIBLE);

                });
            }
        });

    }

    @OnClick(R.id.unfav)
    public void removeFav() {
        AsyncTask.execute(() -> {

            ComicResponse comicResponse = SampleDatabase.getInstance(getActivity()).comixDao().findByTitle(comixBundle.getTitle());
            SampleDatabase.getInstance(getActivity()).comixDao().delete(comicResponse);
            checkMovieFavorite(comicResponseSave.getTitle());
        });
    }

    @OnClick(R.id.fav)
    public void addFav() {

        AsyncTask.execute(() -> {

            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_TITLE, comicResponseSave.getTitle());
            contentValues.put(COLUMN_MONTH, comicResponseSave.getMonth());
            contentValues.put(COLUMN_NUM, comicResponseSave.getNum());
            contentValues.put(COLUMN_LINK, comicResponseSave.getLink());
            contentValues.put(COLUMN_YEAR, comicResponseSave.getYear());
            contentValues.put(COLUMN_NEWS, comicResponseSave.getNews());
            contentValues.put(COLUMN_SAFE_TITLE, comicResponseSave.getSafeTitle());
            contentValues.put(COLUMN_TRANSCRIPT, comicResponseSave.getTranscript());
            contentValues.put(COLUMN_ALT, comicResponseSave.getAlt());
            contentValues.put(COLUMN_IMG, comicResponseSave.getImg());
            contentValues.put(COLUMN_DAY, comicResponseSave.getDay());
            getActivity().getContentResolver().insert(SampleContentProvider.URI_COMIX, contentValues);
            checkMovieFavorite(comicResponseSave.getTitle());


        });

    }

    @OnClick(R.id.iv_comix)
    public void clickNewScreen() {
        if (image == null) {
            image = comicResponseSave.getImg();
        }
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_fragments, ComixImageFragment.newInstance(image), ComixImageFragment.class.getSimpleName());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @OnClick(R.id.prev_btn)
    public void previous() {
        if (number > 0 && number <= lastComicNumber) {

            number--;
            comicsRequest(createAPI().getComicById(number));
        }
    }

    @OnClick(R.id.next_btn)
    public void next() {
        if (number > 0 && number < lastComicNumber) {

            number++;
            comicsRequest(createAPI().getComicById(number));
        }
    }

    public void lastComix(final Observable<ComicResponse> comixResponse) {

        comixResponse.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responses -> {
                    lastComicNumber = responses.getNum();

                });


    }

    public void comicsRequest(final Observable<ComicResponse> comixResponse) {

        comixResponse.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responses -> {
                            number = responses.getNum();
                            image = responses.getImg();
//                            titleSave = responses.getTitle();
                            if (responses.getTranscript().isEmpty()) {
                                transcriptSave = responses.getAlt();
                            } else {
                                transcriptSave = responses.getTranscript();
                            }

                            checkMovieFavorite(responses.getTitle());
                            titleComix.setText(String.format("%s   #%s", responses.getTitle(), responses.getNum()));
                            Picasso.with(DetailComicFragment.this.getActivity()).load(responses.getImg()).placeholder(R.drawable.ic_none).error(R.drawable.ic_error).into(ivComic);
                            tvDay.setText(String.format("%s/%s/%s", responses.getDay(), responses.getMonth(), responses.getYear()));
                            tvTranscript.setText(responses.getTranscript());
                            comixBundle = new ComicResponse();
                            comixBundle.setTitle(responses.getTitle());
                            comixBundle.setImg(responses.getImg());
                            comixBundle.setDay(responses.getDay());
                            comixBundle.setMonth(responses.getMonth());
                            comixBundle.setYear(responses.getYear());
                            comixBundle.setTranscript(responses.getTranscript());
                            comicResponseSave = responses;
                            editor.putInt(NUMBER_COMICS_SELECTED, number).apply();
                            editor.putString(IMAGE_SELECTED, image).apply();

                        },
                        throwable -> {
                            checkMovieFavorite(comicResponseSave.getTitle());
                            titleComix.setText(String.format("%s   #%s", comixBundle.getTitle(), comixBundle.getNum()));
                            Picasso.with(getActivity()).load(comixBundle.getImg()).placeholder(R.drawable.ic_none).error(R.drawable.ic_error).into(ivComic);
                            tvDay.setText(String.format("%s/%s/%s", comixBundle.getDay(), comixBundle.getMonth(), comixBundle.getYear()));
                            tvTranscript.setText(comixBundle.getTranscript());
                        });


    }

}
