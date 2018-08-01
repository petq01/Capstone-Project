package com.petya.build.xkcdcomics.fragments.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.petya.build.xkcdcomics.R;
import com.petya.build.xkcdcomics.api.ComicResponse;
import com.petya.build.xkcdcomics.fragments.ComixImageFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Petya Marinova on 7/26/2018.
 */
public class FavAdapter  extends RecyclerView.Adapter<FavAdapter.ViewHolder> {

    private List<ComicResponse> comicResponses;
    private Context context;
    private FragmentManager fragmentManager;

    public FavAdapter(List<ComicResponse> comicResponses, Context context, FragmentManager fragmentManager) {
        this.comicResponses = comicResponses;
        this.context = context;
        this.fragmentManager=fragmentManager;
    }
   public void setComic(List<ComicResponse> comicResponses){
        this.comicResponses = comicResponses;
        notifyDataSetChanged();
    }
    @Override
    public FavAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comix_response_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ComicResponse comicResponse = comicResponses.get(position);
        holder.originalTitle.setText(comicResponse.getTitle());
        holder.day.setText(comicResponse.getDay()+"/"+comicResponse.getMonth()+"/"+comicResponse.getYear());
        holder.transcript.setText(comicResponse.getTranscript());
        Picasso.with(context).load(comicResponse.getImg()).placeholder(R.drawable.ic_none).error(R.drawable.ic_error).into(holder.imageView);
        holder.imageView.setOnClickListener(v -> {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragments, ComixImageFragment.newInstance(comicResponse.getImg()), ComixImageFragment.class.getSimpleName());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
        });
    }
    @Override
    public int getItemCount() {
        return comicResponses.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.title_comix_item)
        TextView originalTitle;
        @BindView(R.id.tv_day_item)
        TextView day;
        @BindView(R.id.tv_transcript_item)
        TextView transcript;
        @BindView(R.id.iv_comix_item)
        ImageView imageView;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

    }
}
