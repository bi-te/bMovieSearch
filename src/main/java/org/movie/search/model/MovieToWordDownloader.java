package org.movie.search.model;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.List;

@Component
public class MovieToWordDownloader implements Downloader {
    private final String template;
    private final String downloadFolder;
    private final ConversionService conversionService;

    @Autowired
    public MovieToWordDownloader(@Value("${template}") String template, @Value("${downloadFolder}") String downloadFolder,
                                 ConversionService conversionService) {
        this.template = template;
        this.downloadFolder = downloadFolder;
        this.conversionService = conversionService;
    }

    public void download(List<Movie> movies) throws IOException {
        XWPFDocument document = new XWPFDocument();

        for (Movie m: movies){
            XWPFDocument movie = conversionService.convert(m, XWPFDocument.class);
            movie.getParagraphs().forEach(xwpfParagraph ->
                    document.createParagraph().createRun().setText(xwpfParagraph.getText()));
            movie.close();
            document.createParagraph();
        }


        File word;
        if (downloadFolder.equals("default")) {
            word = new File(System.getProperty("user.home") + "/Downloads/" + "MovieSearch.docx");
        } else {
            word = new File(downloadFolder + "MovieSearch.docx");
        }
        try (OutputStream outputStream = new FileOutputStream(word)) {
            document.write(outputStream);
        }

        document.close();

    }

    private String movieToWord(Movie movie) {
        return "Title='" + movie.getTitle() + "' " +
                "Year=" + movie.getYear() + "' " +
                "Genre='" + movie.getGenre() + "' " +
                "Plot='" + movie.getPlot() + "' " +
                "imdbId= " + movie.getId();
    }
}