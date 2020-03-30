package com.example.popularmovies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.popularmovies.pojos.movievideos.MovieVideosResponseItem;
import java.util.List;

class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.ViewHolder> {
    private List<MovieVideosResponseItem> videosResponseItems;
    private OnPlayButtonClickListener listener;

    public MovieTrailerAdapter(
            List<MovieVideosResponseItem> videosResponseItems, OnPlayButtonClickListener listener) {
        this.videosResponseItems = videosResponseItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_movie_trailer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(videosResponseItems.get(position));
    }

    @Override
    public int getItemCount() {
        return videosResponseItems == null ? 0 : videosResponseItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton imageButtonToYouTube;
        TextView textViewTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageButtonToYouTube = itemView.findViewById(R.id.ImageButton_trailerPlayButton);
            textViewTitle = itemView.findViewById(R.id.TextView_trailerTitle);
        }

        public void bind(MovieVideosResponseItem movieVideosResponseItem) {
            imageButtonToYouTube.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onClick(movieVideosResponseItem);
                        }
                    });
            textViewTitle.setText(movieVideosResponseItem.name);
        }
    }

    public interface OnPlayButtonClickListener {
        void onClick(MovieVideosResponseItem key);
    }
}
