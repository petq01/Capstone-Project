package com.petya.build.xkcdcomics.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.petya.build.xkcdcomics.R;
import com.petya.build.xkcdcomics.api.ComicResponse;
import com.petya.build.xkcdcomics.fragments.adapters.DividerItemDecoration;
import com.petya.build.xkcdcomics.fragments.adapters.FavAdapter;
import com.petya.build.xkcdcomics.room.ComicsViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Petya Marinova on 7/26/2018.
 */
public class PersonalFragment extends Fragment {
    @BindView(R.id.rv_personal)
    RecyclerView recyclerView;
    @BindView(R.id.no_favorites)
    TextView textView;
    public static int index = -1;
    public static int top = -1;
    LinearLayoutManager mLayoutManager;
    private ComicsViewModel mComicsViewModel;

    public static PersonalFragment newInstance() {

        return new PersonalFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComicsViewModel = ViewModelProviders.of(this).get(ComicsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_personal_collection, container, false);
        ButterKnife.bind(this, v);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), getResources().getDrawable(R.drawable.bannerdivider), LinearLayoutManager.VERTICAL));
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        index = mLayoutManager.findFirstVisibleItemPosition();
        View v = recyclerView.getChildAt(0);
        top = (v == null) ? 0 : (v.getTop() - recyclerView.getPaddingTop());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (index != -1) {
            mLayoutManager.scrollToPositionWithOffset(index, top);
        }
        AsyncTask.execute(() -> {
            mComicsViewModel.getAll().observe(this, new Observer<List<ComicResponse>>() {
                @Override
                public void onChanged(@Nullable final List<ComicResponse> comicResponses) {
                    if (comicResponses.isEmpty()) {
                        getActivity().runOnUiThread(() -> {
                            textView.setVisibility(View.VISIBLE);
                            textView.setText(R.string.like_some);
                        });
                    } else {
                        getActivity().runOnUiThread(() -> {
                            textView.setVisibility(View.INVISIBLE);
                            FavAdapter favAdapter = new FavAdapter(comicResponses, getActivity(), getFragmentManager());
                            favAdapter.setComic(comicResponses);
                            favAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(favAdapter);
                        });
                    }


                }
            });
//            List<ComicResponse> comicResponses = SampleDatabase.getInstance(getActivity()).comixDao().getAll();
//            if(comicResponses.isEmpty()){
//                getActivity().runOnUiThread(() -> {
//                    textView.setVisibility(View.VISIBLE);
//                    textView.setText(R.string.like_some);
//                });
//            }else{
//                getActivity().runOnUiThread(() -> {
//                textView.setVisibility(View.INVISIBLE);
//                FavAdapter favAdapter = new FavAdapter(comicResponses, getActivity(),getFragmentManager());
//                favAdapter.notifyDataSetChanged();
//                    recyclerView.setAdapter(favAdapter);
//                });
//            }

        });
    }
}

