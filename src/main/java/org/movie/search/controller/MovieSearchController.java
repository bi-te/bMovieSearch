package org.movie.search.controller;

import org.movie.search.model.Movie;
import org.movie.search.services.MoviesSearcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class MovieSearchController {
    private final MoviesSearcherService moviesSearcherService;
    private final CacheManager cacheManager;


    @Autowired
    public MovieSearchController(MoviesSearcherService moviesSearcherService,
                                 CacheManager cacheManager){
        this.moviesSearcherService = moviesSearcherService;
        this.cacheManager = cacheManager;
    }

    @GetMapping(value = "/movie_search", params = {"title"})
    public List<Movie> searchMovieTitle(@RequestParam(value = "title", required = false) String[] titles,
                                        @RequestParam(value = "d", defaultValue = "false") boolean d){

        return moviesSearcherService.getMoviesByTitle(titles, d);
    }

    @GetMapping(value = "/movie_search", params = {"id"})
    public List<Movie> searchMovieId(@RequestParam("id") String[] ids,
                                     @RequestParam(value = "d", defaultValue = "false") boolean d) {

        return moviesSearcherService.getMoviesById(ids, d);
    }

    @Scheduled(fixedRate = 600000)
    public void evictCache(){
        cacheManager.getCacheNames().forEach(c -> cacheManager.getCache(c).clear());
    }

}
