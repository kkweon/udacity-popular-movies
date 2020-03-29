package com.example.popularmovies;

import android.content.Context;
import android.util.DisplayMetrics;

public class Utility {
    public static int calculateNoOfColumns(Context context, float columnWidthInPixel) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthPixel = displayMetrics.widthPixels;
        int noOfColumns =
                (int)
                        (screenWidthPixel / columnWidthInPixel
                                + 0.5); // +0.5 for correct rounding to int.
        return noOfColumns;
    }
}
