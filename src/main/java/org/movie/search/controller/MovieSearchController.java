package org.movie.search.controller;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.movie.search.model.Downloader;
import org.movie.search.model.Movie;
import org.movie.search.services.MoviesSearcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


@RestController
public class MovieSearchController {
    private final MoviesSearcherService moviesSearcherService;
    private final CacheManager cacheManager;
    private final Downloader downloader;


    @Autowired
    public MovieSearchController(MoviesSearcherService moviesSearcherService,
                                 CacheManager cacheManager, Downloader downloader) {
        this.moviesSearcherService = moviesSearcherService;
        this.cacheManager = cacheManager;
        this.downloader = downloader;
    }

    @GetMapping(value = "/movie_search", params = {"title"})
    public List<Movie> searchMovieTitle(@RequestParam(value = "title", required = false) String[] titles) {

        return moviesSearcherService.getMoviesByTitle(titles);
    }

    @GetMapping(value = "/movie_search", params = {"id"})
    public List<Movie> searchMovieId(@RequestParam("id") String[] ids) {

        return moviesSearcherService.getMoviesById(ids);
    }

    @GetMapping(value = "/movie_search/download", params = {"title"})
    public ResponseEntity<byte[]> downloadMovieTitle(@RequestParam(value = "title", required = false) String[] titles) throws IOException {

        XWPFDocument document = downloader.moviesToDownload(moviesSearcherService.getMoviesByTitle(titles));
        byte[] doc;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            document.write(byteArrayOutputStream);
            doc = byteArrayOutputStream.toByteArray();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "MovieSearch.docx");

        return new ResponseEntity<>(doc, headers, HttpStatus.OK);
    }

    @GetMapping(value = "/movie_search/download", params = {"id"})
    public ResponseEntity<byte[]> downloadMovieId(@RequestParam("id") String[] ids) throws IOException {

        XWPFDocument document = downloader.moviesToDownload(moviesSearcherService.getMoviesById(ids));
        byte[] doc;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            document.write(byteArrayOutputStream);
            doc = byteArrayOutputStream.toByteArray();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "MovieSearch");

        return new ResponseEntity<>(doc, headers, HttpStatus.OK);
    }

    @Scheduled(fixedRate = 600000)
    public void evictCache() {
        cacheManager.getCacheNames().forEach(c -> cacheManager.getCache(c).clear());
    }

}
