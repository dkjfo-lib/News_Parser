package Parsers;

import SQL.SQL_Adapter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.sql.SQLException;
import static java.lang.System.err;

public class Parser_BBC extends AbstractParser {
    private final String targetURL = "https://www.bbc.com/news";
    private final String targetHTMLElementClass_layer_0 = "gs-o-media__body";

    private final boolean verbose_mode;

    public Parser_BBC(boolean verbose_mode) {
        this.verbose_mode = verbose_mode;
    }


    /**
     * target element <div id = "u1276955557987094">...</div>
     * but it never was caught...
     * @return
     */
    @Override
    public NewsData[] parsePage() throws IOException {

        Document doc = Jsoup.connect(targetURL).get();

        Elements content = doc.getElementsByClass(targetHTMLElementClass_layer_0);

        NewsData[] BBC_News = new NewsData[10];

        int counter = 0;
        for (Element oneNewsUnit : content) {
            if (counter < 5) {
                counter++;
                continue;
            }

            String article = oneNewsUnit.text();
            String link = oneNewsUnit.select("a[href]").attr("href"); // "http://example.com/"

            link = targetURL + link.substring(5);

            // inner page parsing
            {
                Document innerNewsPage = Jsoup.connect(link).get();
                Element innerNewsPageContent = innerNewsPage.select("div#page").select("div.story-body").first();

                String innerArticle;

                innerArticle = innerNewsPageContent.select("h1.story-body__h1").text();

                article = innerArticle;
            }

            BBC_News[counter - 5] = new NewsData(link, article, "", "BBC");

            if (verbose_mode) {
                System.out.println("Found " + content.size() + " top news: ");
                System.out.println(String.valueOf(counter - 5) + ". " + BBC_News[counter - 5]);
                System.out.println();
            }

            counter++;
        }

        return BBC_News;
    }
}
