package org.movie.search.model;

import java.io.IOException;
import java.util.List;

public interface Downloader {
    void download(List<Movie> movies) throws IOException;
}
