package com.example.popularmovies;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView moviePosters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviePosters = findViewById(R.id.recycler_view_movie_posters);
        moviePosters.setLayoutManager(new GridLayoutManager(this, 3));
        moviePosters.setAdapter(new MovieDetailAdapter());
    }
}
