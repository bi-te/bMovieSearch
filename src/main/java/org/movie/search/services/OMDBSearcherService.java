package org.movie.search.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.movie.search.model.Downloader;
import org.movie.search.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class OMDBSearcherService implements MoviesSearcherService {
    private final MovieSearchService omdbService;
    private final Logger logger;
    private final Downloader downloader;

    @Autowired
    public OMDBSearcherService(MovieSearchService omdbService, Downloader downloader){
        this.omdbService = omdbService;
        this.logger = LogManager.getLogger(OMDBSearcherService.class);
        this.downloader = downloader;
    }

    @Override
    public List<Movie> getMoviesByTitle(String[] titles) {
        List<Movie> movies = new LinkedList<>();
        List<CompletableFuture<Movie>> list = new LinkedList<>();

        for (String title : titles) {
            list.add(omdbService.getMovieByTitle(title));
        }

        return getMovies(movies, list);
    }

    @Override
    public List<Movie> getMoviesById(String[] ids) {
        List<Movie> movies = new LinkedList<>();
        List<CompletableFuture<Movie>> list = new LinkedList<>();

        for (String id : ids) {
            list.add(omdbService.getMovieById(id));
        }

        return getMovies(movies, list);
    }

    private List<Movie> getMovies(List<Movie> movies, List<CompletableFuture<Movie>> list) {
        list.forEach(m -> {
            try {
                movies.add(m.get());
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error occurred while adding movie --- ", e);
            }
        });

        return movies;
    }

}
