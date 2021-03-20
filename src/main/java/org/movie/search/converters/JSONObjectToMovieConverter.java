package org.movie.search.converters;

import org.json.JSONObject;
import org.movie.search.model.Movie;
import org.springframework.core.convert.converter.Converter;


public class JSONObjectToMovieConverter implements Converter<JSONObject, Movie> {

    @Override
    public Movie convert(JSONObject response) {
        Movie movie;
        if (response.getString("Response").equals("True")){
            movie = new Movie(response.getString("Title"),
                    response.getString("Year"),
                    response.getString("Genre"),
                    response.getString("Plot"),
                    response.getString("imdbID"));
        } else {
            movie = new Movie(response.getString("Error"));
        }

        return movie;
    }
}