package org.movie.search.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.movie.search.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.convert.ConversionService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import java.util.concurrent.CompletableFuture;

@Service
public class OMDBService implements MovieSearchService{

    private final String apikey;
    private final RestOperations restTemplate;
    private final String omdb = "http://www.omdbapi.com/?";
    private final ConversionService conversionService;
    Logger logger;


    @Autowired
    OMDBService(@Value("${apikey}") String apikey, RestOperations restTemplate, ConversionService conversionService) {
        this.apikey = apikey;
        this.restTemplate = restTemplate;
        this.conversionService = conversionService;
        this.logger = LogManager.getLogger(OMDBService.class);
    }

    @Async
    @Cacheable(value = "movies_title", key = "#title")
    public CompletableFuture<Movie> getMovieByTitle(String title, boolean d) {
        logger.info("Making request to OMDB -  " + title);
        JSONObject response = new JSONObject(restTemplate.getForObject(omdb + "t=" + title +
                "&apikey=" + apikey, String.class));
        Movie movie = conversionService.convert(response, Movie.class);

        return CompletableFuture.completedFuture(movie);
    }

    @Async
    @Cacheable(value = "movies_id", key = "#id")
    public CompletableFuture<Movie> getMovieById(String id, boolean d) {
        logger.info("Making request to OMDB -  " + id);
        JSONObject response = new JSONObject(restTemplate.getForObject(omdb + "i=" + id +
                "&apikey=" + apikey, String.class));
        Movie movie = conversionService.convert(response, Movie.class);
        return CompletableFuture.completedFuture(movie);
    }


}
