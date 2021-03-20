package org.movie.search.converters;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.movie.search.model.Movie;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MovieToWordConverter implements Converter<Movie, XWPFDocument> {
    private String template = "MovieSearchTemplate.docx";
    private Map<String, Function<Movie, String>> map = Map.of(
            "$TITLE$", (Movie::getTitle),
            "$YEAR$", (Movie::getYear),
            "$GENRE$", (Movie::getGenre),
            "$PLOT$", (Movie::getPlot),
            "$IMDBID$", (Movie::getId)
    );
    private List<String> list = List.of("$TITLE$", "$YEAR$", "$GENRE$", "$PLOT$", "$IMDBID$");

    @Override
    public XWPFDocument convert(Movie movie) {
        try (InputStream is = new FileInputStream(ResourceUtils.getFile("classpath:" + template))) {
            XWPFDocument template = new XWPFDocument(is);
            int c = 0;
            for (XWPFParagraph paragraph : template.getParagraphs()) {

                XWPFRun run = paragraph.getRuns().get(0);
                String key = list.get(c);
                run.setText(run.text().replace(key,  map.get(key).apply(movie)), 0);
                c++;
            }
            return template;
        } catch (IOException e) {
            XWPFDocument document = new XWPFDocument();
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run =  paragraph.createRun();
            run.setText(movieToWord(movie));
            run.setFontSize(14);
            run.setFontFamily("TimesNewRoman");
            return document;
        }

    }

    private String movieToWord(Movie movie){
        return "Title='" + movie.getTitle() + "' " +
                "Year=" + movie.getYear() + "' " +
                "Genre='" + movie.getGenre() + "' " +
                "Plot='" + movie.getPlot() + "' " +
                "imdbId= " + movie.getId();
    }
}
