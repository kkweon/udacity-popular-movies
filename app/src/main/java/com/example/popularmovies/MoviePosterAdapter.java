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


/**
 * This adapter handles the movie poster grid on the main page.
 */
class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.ViewHolder> {
    private List<Movie> movies;
    private final MovieDetailClickListener clickListener;

    MoviePosterAdapter(MovieDetailClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflatedView =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_movie_poster, parent, /*attachToRoot=*/ false);
        return new ViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return this.movies == null ? 0 : this.movies.size();
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView moviePosterImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePosterImageView = itemView.findViewById(R.id.ImageView_moviePoster);
        }

        public void bind(int position) {
            Movie m = movies.get(position);
            Picasso.get().load(m.getThumbnail()).fit().into(moviePosterImageView);

            if (ApplicationService.getInstance().getFavoriteMovieIds().contains(m.getId())) {
                itemView.setBackground(
                        itemView.getResources().getDrawable(R.drawable.border_red, null));
            }

            itemView.setOnClickListener(v -> clickListener.onClick(m));
        }
    }

    public interface MovieDetailClickListener {
        void onClick(Movie m);
    }
}
