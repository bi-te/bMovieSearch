package org.movie.search.model;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class MovieToWordDownloader implements Downloader {
    private final String downloadFolder;
    private final ConversionService conversionService;

    @Autowired
    public MovieToWordDownloader(@Value("${downloadFolder}") String downloadFolder,
                                 ConversionService conversionService) {
        this.downloadFolder = downloadFolder;
        this.conversionService = conversionService;
    }

    public XWPFDocument moviesToDownload(List<Movie> movies) throws IOException  {
        XWPFDocument document = new XWPFDocument();
        for (Movie m: movies){
            XWPFDocument movie = conversionService.convert(m, XWPFDocument.class);
            movie.getParagraphs().forEach(xwpfParagraph ->
                    document.createParagraph().createRun().setText(xwpfParagraph.getText()));
            movie.close();
            document.createParagraph();
        }

        return document;
    }

    private String movieToWord(Movie movie) {
        return "Title='" + movie.getTitle() + "' " +
                "Year=" + movie.getYear() + "' " +
                "Genre='" + movie.getGenre() + "' " +
                "Plot='" + movie.getPlot() + "' " +
                "imdbId= " + movie.getId();
    }
}