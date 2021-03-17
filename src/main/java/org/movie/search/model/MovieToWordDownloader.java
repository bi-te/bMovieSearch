package org.movie.search.model;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Component
public class MovieToWordDownloader implements Downloader {
    @Value("${downloadFolder}")
    private String downloadFolder;

    public void download(List<Movie> movies) throws IOException {
        XWPFDocument document = new XWPFDocument();
        movies.forEach(m -> {
            XWPFParagraph movie = document.createParagraph();
            XWPFRun run =  movie.createRun();
            run.setText(movieToWord(m));
            run.setFontSize(14);
            run.setFontFamily("TimesNewRoman");
        });

        File word;
        if(downloadFolder.equals("default")){
            word = new File(System.getProperty("user.home") + "/Downloads/" + "MovieSearch.docx");
        } else {
            word = new File(downloadFolder + "MovieSearch.docx");
        }
        try (OutputStream outputStream = new FileOutputStream(word)){
            document.write(outputStream);
        }

        document.close();

    }

    private String movieToWord(Movie movie){
        return "Title='" + movie.getTitle() + "' " +
                "Year=" + movie.getYear() + "' " +
                "Genre='" + movie.getGenre() + "' " +
                "Plot='" + movie.getPlot() + "' ";
    }
}