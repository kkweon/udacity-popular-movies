package com.example.popularmovies;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_view_movie_posters);

        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);
    }
}
