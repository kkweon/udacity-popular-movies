package com.example.popularmovies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.popularmovies.pojos.moviereviews.MovieReviewsResponseItem;
import java.util.List;


/**
 * On the movie details page,
 * we show reviews by random people. This adapter is responsible for the view.
 */
public class MovieDetailsReviewsAdapter
        extends RecyclerView.Adapter<MovieDetailsReviewsAdapter.ViewHolder> {

    private List<MovieReviewsResponseItem> reviews;

    public MovieDetailsReviewsAdapter(List<MovieReviewsResponseItem> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_movie_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        return reviews == null ? 0 : reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mAuthor;
        private TextView mContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mAuthor = itemView.findViewById(R.id.TextView_reviewAuthor);
            mContent = itemView.findViewById(R.id.TextView_reviewContent);
        }

        public void bind(MovieReviewsResponseItem item) {
            mAuthor.setText(item.getAuthor());
            mContent.setText(item.getContent());
        }
    }
}
