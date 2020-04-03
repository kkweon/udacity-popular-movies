package com.example.popularmovies.pojos.movielist;

import static org.junit.Assert.*;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
                new String(Files.readAllBytes(Paths.get(mockJsonFile.getPath())), StandardCharsets.UTF_8);

        Gson gson = new Gson();
        MovieResponse resp = gson.fromJson(mockJson, MovieResponse.class);

        assertEquals(1, resp.getPage());
        assertEquals("Dilwale Dulhania Le Jayenge", resp.getResults().get(0).getTitle());
        assertEquals(
                new GregorianCalendar(1995, Calendar.OCTOBER, 20).getTime(),
                resp.getResults().get(0).getReleaseDate());
    }
}
