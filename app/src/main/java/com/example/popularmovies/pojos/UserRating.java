package com.example.popularmovies.pojos;

import java.io.Serializable;
import lombok.Data;

@Data
public class UserRating implements Serializable {
    double score;

    public UserRating(double score) throws IllegalArgumentException {
        if (0 <= score && score < 11) {
            this.score = score;
            return;
        }

        throw new IllegalArgumentException(
                String.format("score must be between 0 and 10 (inclusive). But, it was %f", score));
    }

    @Override
    public String toString() {
        return String.format("%.1f/10", score);
    }
}
