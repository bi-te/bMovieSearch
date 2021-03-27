package org.movie.search.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.movie.search.model.Downloader;
import org.movie.search.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.convert.ConversionService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.CompletableFuture;

@Service
public class OMDBService implements MovieSearchService {

    private final String apikey;
    private final RestOperations restTemplate;
    private final String omdb;
    private final ConversionService conversionService;
    private final Logger logger;
    private final Downloader downloader;


    @Autowired
    OMDBService(@Value("${apikey}") String apikey, RestOperations restTemplate, ConversionService conversionService,
                @Value("${omdb}") String omdb, Downloader downloader) {
        this.apikey = apikey;
        this.restTemplate = restTemplate;
        this.conversionService = conversionService;
        this.logger = LogManager.getLogger(OMDBService.class);
        this.omdb = omdb;
        this.downloader = downloader;
    }

    @Override
    @Async
    @Cacheable(value = "movies_title", key = "#title")
    public CompletableFuture<Movie> getMovieByTitle(String title) {
        logger.info("Making request to OMDB -  " + title);
        String uri = UriComponentsBuilder.fromUriString(omdb).queryParam("t", title)
                .queryParam("apikey", apikey).toUriString();

        JSONObject response = new JSONObject(restTemplate.getForObject(uri , String.class));

        Movie movie = conversionService.convert(response, Movie.class);

        return CompletableFuture.completedFuture(movie);
    }

    @Override
    @Async
    @Cacheable(value = "movies_id", key = "#id")
    public CompletableFuture<Movie> getMovieById(String id) {
        logger.info("Making request to OMDB -  " + id);
        String uri = UriComponentsBuilder.fromUriString(omdb).queryParam("i", id)
                .queryParam("apikey", apikey).toUriString();

        JSONObject response = new JSONObject(restTemplate.getForObject(uri, String.class));

        Movie movie = conversionService.convert(response, Movie.class);
        return CompletableFuture.completedFuture(movie);
    }
}

