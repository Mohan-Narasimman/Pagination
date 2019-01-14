package com.mohann.samplepagination.adapter;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.mohann.samplepagination.R;
import com.mohann.samplepagination.model.Movie;


public class ItemAdapter extends PagedListAdapter<Movie, ItemAdapter.ItemViewHolder> {

    private Context mCtx;
    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w500";


    public ItemAdapter(Context mCtx) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.item_list, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Movie item = getItem(position);

        if (item != null) {
            holder.movie_title.setText(item.getOriginalTitle());
            holder.movie_desc.setText(item.getOverview());
            holder.movie_year.setText(item.getReleaseDate().substring(0, 4)  // we want the year only
                    + " | "
                    + item.getOriginalLanguage().toUpperCase());
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            Glide
                    .with(mCtx)
                    .load(BASE_URL_IMG + item.getPosterPath()).apply(options)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.movie_progress.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.movie_progress.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.movie_poster);
        } else {
            Toast.makeText(mCtx, "Item is null", Toast.LENGTH_LONG).show();
        }
    }

    private static DiffUtil.ItemCallback<Movie> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Movie>() {
                @Override
                public boolean areItemsTheSame(Movie oldItem, Movie newItem) {
                    return oldItem.getGenreIds() == newItem.getGenreIds();
                }

                @Override
                public boolean areContentsTheSame(Movie oldItem, Movie newItem) {
                    return oldItem.equals(newItem);
                }
            };

    class ItemViewHolder extends RecyclerView.ViewHolder {

        ProgressBar movie_progress;
        ImageView movie_poster;
        TextView movie_year;
        TextView movie_title;
        TextView movie_desc;

        public ItemViewHolder(View itemView) {
            super(itemView);
            movie_progress = itemView.findViewById(R.id.movie_progress);
            movie_poster = itemView.findViewById(R.id.movie_poster);
            movie_year = itemView.findViewById(R.id.movie_year);
            movie_title = itemView.findViewById(R.id.movie_title);
            movie_desc = itemView.findViewById(R.id.movie_desc);
        }
    }
}