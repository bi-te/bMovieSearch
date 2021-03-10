package org.movie.search.services;

import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.movie.search.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.convert.ConversionService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;
import java.util.concurrent.CompletableFuture;

@Service
public class OMDBService {
    private final RestTemplate restTemplate;
    private final String apikey;
    private final String omdb = "http://www.omdbapi.com/?";
    private final ConversionService conversionService;
    private final Logger logger;


    @Autowired
    OMDBService(RestTemplate restTemplate, Properties properties, ConversionService conversionService, Logger logger) {
        this.restTemplate = restTemplate;
        this.apikey = properties.getProperty("apikey");
        this.conversionService = conversionService;
        this.logger = logger;
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
