package org.movie.search.services;

import org.movie.search.model.Movie;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import java.util.concurrent.CompletableFuture;

@Service
public class OMDBService {



    @Async
    public CompletableFuture<Movie> getMovieByTitle(String title, boolean d) {
        return null;
    }

    @Async
    public CompletableFuture<Movie> getMovieById(String id, boolean d) {
        return null;
    }


}
