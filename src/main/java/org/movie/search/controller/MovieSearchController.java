package org.movie.search.controller;

import org.movie.search.model.Movie;
import org.movie.search.model.MovieToWordDownloader;
import org.movie.search.services.OMDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;


@RestController
public class MovieSearchController {
    OMDBService omdbService;
    Executor executor;
    MovieToWordDownloader downloader;

    @Autowired
    public MovieSearchController(OMDBService omdbService, Executor executor, MovieToWordDownloader downloader){
        this.omdbService = omdbService;
        this.executor = executor;
        this.downloader = downloader;
    }

    @GetMapping(value = "/movie_search", params = {"title"})
    public List<Movie> searchMovieTitle(@RequestParam(value = "title", required = false) String[] titles,
                                        @RequestParam(value = "d", defaultValue = "false") boolean d) throws IOException {
        List<Movie> movies = new LinkedList<>();
        List<CompletableFuture<Movie>> list = new LinkedList<>();

        for(String title: titles){
            list.add(omdbService.getMovieByTitle(title, d));
        }

        list.forEach(m -> {
            try {
                movies.add(m.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        if(d){
            downloader.download(movies);
        }

        return movies;
    }

    @GetMapping(value = "/movie_search", params = {"id"})
    public List<Movie> searchMovieId(@RequestParam("id") String[] ids,
                                     @RequestParam(value = "d", defaultValue = "false") boolean d) throws IOException {
        List<Movie> movies = new LinkedList<>();
        List<CompletableFuture<Movie>> list = new LinkedList<>();

        for(String id: ids){
            list.add(omdbService.getMovieById(id, d));
        }

        list.forEach(m -> {
            try {
                movies.add(m.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        if(d){
            downloader.download(movies);
        }

        return movies;
    }

}
