package Parsers;

import java.io.Serializable;

public class NewsData implements Serializable {
    public String link;
    public String article;

    public NewsData(String link, String article) {
        this.link = link;
        this.article = article;
    }

    @Override
    public String toString() {
        return "NewsData{" +
                "link='" + link + '\'' +
                ", article='" + article + '\'' +
                '}';
    }
}
