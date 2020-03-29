package com.example.popularmovies;

import lombok.Data;

@Data
class UserRating {
    double score;

    public UserRating(double score) throws IllegalArgumentException {
        if (0 <= score && score < 11) {
            this.score = score;
            return;
        }

        throw new IllegalArgumentException(
                String.format("score must be between 0 and 10 (inclusive). But, it was %f", score));
    }
}
