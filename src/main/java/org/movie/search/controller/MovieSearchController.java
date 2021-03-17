package org.movie.search.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.movie.search.model.Downloader;
import org.movie.search.model.Movie;
import org.movie.search.services.MovieSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@RestController
public class MovieSearchController {
    private final MovieSearchService omdbService;
    private final Downloader downloader;
    private final CacheManager cacheManager;
    private final Logger logger;


    @Autowired
    public MovieSearchController(MovieSearchService omdbService, Downloader downloader,
                                 CacheManager cacheManager){
        this.omdbService = omdbService;
        this.downloader = downloader;
        this.cacheManager = cacheManager;
        this.logger = LogManager.getLogger(MovieSearchController.class);
    }

    @GetMapping(value = "/movie_search", params = {"title"})
    public List<Movie> searchMovieTitle(@RequestParam(value = "title", required = false) String[] titles,
                                        @RequestParam(value = "d", defaultValue = "false") boolean d){

        List<Movie> movies = new LinkedList<>();
        List<CompletableFuture<Movie>> list = new LinkedList<>();

        for(String title: titles){
            list.add(omdbService.getMovieByTitle(title, d));
        }

        list.forEach(m -> {
            try {
                movies.add(m.get());
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error occurred while adding movie --- ",e);
            }
        });

        if(d){
            try {
                logger.info("downloading file...");
                downloader.download(movies);
            } catch (IOException e) {
                logger.error("Error occurred while downloading result --- ",e);
            }
        }

        return movies;
    }

    @GetMapping(value = "/movie_search", params = {"id"})
    public List<Movie> searchMovieId(@RequestParam("id") String[] ids,
                                     @RequestParam(value = "d", defaultValue = "false") boolean d) {
        List<Movie> movies = new LinkedList<>();
        List<CompletableFuture<Movie>> list = new LinkedList<>();

        for(String id: ids){
            list.add(omdbService.getMovieById(id, d));
        }

        list.forEach(m -> {
            try {
                movies.add(m.get());
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error occurred while adding movie --- ",e);
            }
        });

        if(d){
            try {
                logger.info("downloading file...");
                downloader.download(movies);
            } catch (IOException e) {
                logger.error("Error occurred while downloading result --- ",e);
            }
        }

        return movies;
    }

    @Scheduled(fixedRate = 600000)
    public void evictCache(){
        cacheManager.getCacheNames().forEach(c -> cacheManager.getCache(c).clear());
    }

}
