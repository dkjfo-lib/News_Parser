package Parsers;

import java.io.Serializable;
import java.util.Date;

public class NewsData implements Serializable {
    public String link;
    public String article;
    public String picture;
    public String date;
    public String sourceName;

    public NewsData(String link, String article) {
        this.link = link;
        this.article = article;
    }

    public NewsData(String link, String article, String date, String sourceName) {
        this.link = link;
        this.article = article;
        this.date = date;
        this.sourceName = sourceName;
    }

    @Override
    public String toString() {
        return "NewsData{" +
                "link='" + link + '\'' +
                ", article='" + article + '\'' +
                '}';
    }
}
