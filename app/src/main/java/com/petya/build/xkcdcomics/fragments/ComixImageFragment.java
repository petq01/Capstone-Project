package com.petya.build.xkcdcomics.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.petya.build.xkcdcomics.R;
import com.petya.build.xkcdcomics.TouchImageView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Petya Marinova on 7/24/2018.
 */
public class ComixImageFragment extends Fragment {
    @BindView(R.id.iv_full_scrn)
    TouchImageView fullScreen;
    private static final String KEY_IMAGE = "IMAGE";

    public static ComixImageFragment newInstance(String img) {

        Bundle args = new Bundle();
        ComixImageFragment fragment = new ComixImageFragment();
        args.putString(KEY_IMAGE, img);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_comix_image, container, false);
        ButterKnife.bind(this, v);

        Bundle bundle = getArguments();
        if (bundle != null) {

            String imgBundle = bundle.getString(KEY_IMAGE);
            Picasso.with(getActivity()).load(imgBundle).placeholder(R.drawable.ic_none).error(R.drawable.ic_error).into(fullScreen);
        }
        return v;
    }

}
