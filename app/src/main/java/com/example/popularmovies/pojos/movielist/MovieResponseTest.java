package com.example.popularmovies.pojos.movielist;

import static org.junit.Assert.*;

import com.example.popularmovies.pojos.movielist.MovieResponse;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.Test;

public class MovieResponseTest {

    @Test
    public void testParseJsonToMovieResponse() throws IOException {
        File mockJsonFile =
                new File(
                        getClass()
                                .getClassLoader()
                                .getResource("example_movie_response.json")
                                .getFile());
        String mockJson =
                new String(Files.readAllBytes(Paths.get(mockJsonFile.getPath())), "UTF-8");

        Gson gson = new Gson();
        MovieResponse resp = gson.fromJson(mockJson, MovieResponse.class);

        Assert.assertEquals(1, resp.getPage());
        Assert.assertEquals("Dilwale Dulhania Le Jayenge", resp.getResults().get(0).getTitle());
        Assert.assertEquals(
                new GregorianCalendar(1995, Calendar.OCTOBER, 20).getTime(),
                resp.getResults().get(0).getReleaseDate());
    }
}
