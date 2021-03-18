package org.movie.search.services;

import org.movie.search.model.Movie;

import java.util.List;

public interface MoviesSearcherService {
    List<Movie> getMoviesByTitle(String[] titles, boolean b);

    List<Movie> getMoviesById(String[] ids, boolean b);

}
