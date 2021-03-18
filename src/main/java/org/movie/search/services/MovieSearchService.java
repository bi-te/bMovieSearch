package org.movie.search.services;


import org.movie.search.model.Movie;

import java.util.concurrent.CompletableFuture;

public interface MovieSearchService {
    CompletableFuture<Movie> getMovieByTitle(String title);

    CompletableFuture<Movie> getMovieById(String id);

}
