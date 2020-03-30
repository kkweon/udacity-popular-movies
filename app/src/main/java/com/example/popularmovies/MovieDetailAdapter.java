package com.example.popularmovies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.popularmovies.pojos.Movie;
import com.squareup.picasso.Picasso;
import java.util.List;

class MovieDetailAdapter extends RecyclerView.Adapter<MovieDetailAdapter.MovieDetailViewHolder> {
    private List<Movie> movies;
    private final MovieDetailClickListener clickListener;

    MovieDetailAdapter(MovieDetailClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MovieDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflatedView =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_movie_poster, parent, /*attachToRoot=*/ false);
        return new MovieDetailViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieDetailViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return this.movies == null ? 0 : this.movies.size();
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public class MovieDetailViewHolder extends RecyclerView.ViewHolder {
        private ImageView moviePosterImageView;

        public MovieDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePosterImageView = itemView.findViewById(R.id.ImageView_moviePoster);
        }

        public void bind(int position) {
            Movie m = movies.get(position);
            Picasso.get().load(m.getThumbnail()).fit().into(moviePosterImageView);

            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickListener.onClick(m);
                        }
                    });
        }
    }

    public interface MovieDetailClickListener {
        void onClick(Movie m);
    }
}
