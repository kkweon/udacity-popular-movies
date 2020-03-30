package com.example.popularmovies;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ApplicationViewModel extends ViewModel {
    private MutableLiveData<MovieService.FetchType> fetchTypeLiveData =
            new MutableLiveData<>(MovieService.FetchType.POPULAR_MOVIES);
    private MutableLiveData<Integer> currentPage = new MutableLiveData<Integer>(1);
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public LiveData<MovieService.FetchType> getFetchTypeLiveData() {
        return fetchTypeLiveData;
    }

    public void setMovieServiceFetchType(MovieService.FetchType t) {
        fetchTypeLiveData.setValue(t);
    }

    public LiveData<Integer> getCurrentPage() {
        return currentPage;
    }

    public void setPage(Integer newPage) {
        currentPage.setValue(newPage);
    }

    public void setLoading(boolean isLoading) {
        this.isLoading.setValue(isLoading);
    }

    public LiveData<Boolean> isLoading() {
        return this.isLoading;
    }
}
