package org.movie.search.model;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;
import java.util.List;

public interface Downloader {
    XWPFDocument moviesToDownload(List<Movie> movies) throws IOException;
}
