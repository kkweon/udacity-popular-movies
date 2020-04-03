package com.example.popularmovies.persistence;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.HashSet;
import java.util.List;

@Dao
public interface FavoriteMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(FavoriteMovie movie);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertMulti(HashSet<FavoriteMovie> movies);

    @Delete
    int delete(FavoriteMovie movie);

    @Query("DELETE FROM favorite_movie")
    int deleteAll();

    @Query("SELECT * FROM favorite_movie")
    List<FavoriteMovie> getMovies();
}
