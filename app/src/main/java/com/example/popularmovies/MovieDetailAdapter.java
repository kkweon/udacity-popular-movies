package com.example.popularmovies;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

class MovieDetailAdapter extends RecyclerView.Adapter<MovieDetailAdapter.MovieDetailViewHolder> {
    @NonNull
    @Override
    public MovieDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflatedView =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.movie_detail, parent, /*attachToRoot=*/ false);
        return new MovieDetailViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieDetailViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MovieDetailViewHolder extends RecyclerView.ViewHolder {
        public ImageView moviePosterImageView;

        public MovieDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePosterImageView = itemView.findViewById(R.id.image_view_movie_detail);
        }

        public void bind(int position) {
            Log.v(
                    MovieDetailViewHolder.class.getSimpleName(),
                    String.format("position=%d", position));
            Picasso.get().load("https://i.imgur.com/DvpvklR.png").fit().into(moviePosterImageView);
        }
    }
}
