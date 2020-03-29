package com.example.popularmovies;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.MoviePosterViewHolder> {
    @NonNull
    @Override
    public MoviePosterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        boolean attachToRoot = false;
        View inflatedView =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.movie_detail, parent, attachToRoot);
        return new MoviePosterViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviePosterViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MoviePosterViewHolder extends RecyclerView.ViewHolder {
        private ImageView moviePosterImageView;

        public MoviePosterViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePosterImageView = itemView.findViewById(R.id.image_view_movie_detail);
        }

        public void bind(int position) {
            Log.v(
                    MoviePosterViewHolder.class.getSimpleName(),
                    String.format("position=%d", position));
            Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(moviePosterImageView);
        }
    }
}
