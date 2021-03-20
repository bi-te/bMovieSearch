package org.movie.search.model;

import java.util.Objects;


public class Movie {
    private String title;
    private String year;
    private String genre;
    private String plot;
    private String id;

    public Movie() {
    }

    public Movie(String title){
        this.title = title;
        this.year = "0";
        this.genre="N/A";
        this.plot = "N/A";
    }

    public Movie(String title, String year, String genre, String plot, String id) {
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.plot = plot;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", year=" + year +
                ", genre='" + genre + '\'' +
                ", plot='" + plot + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        return year == movie.year && title.equals(movie.title) && genre.equals(movie.genre) && plot.equals(movie.plot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, year, genre, plot);
    }
}