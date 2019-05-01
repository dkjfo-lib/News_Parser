package Parsers;

import SQL.SQL_Adapter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.sql.SQLException;

import static Serializer.Serializer.deserializeNewsData;
import static Serializer.Serializer.serializeNewsData;
import static java.lang.System.err;

public class Parser_00 extends AbstractParser {

    private final boolean verbose_mode;

    private final String targetURL = "https://www.bbc.com/news";
    private final String targetHTMLElementClass_layer_0 = "gs-o-media__body";

    public Parser_00(boolean verbose_mode) {
        this.verbose_mode = verbose_mode;
    }


    /**
     * target element <div id = "u1276955557987094">...</div>
     * but it never was caught...
     * @return
     */
    @Override
    NewsData[] parsePage() throws IOException {

        Document doc = Jsoup.connect(targetURL).get();

//        String HTMLDoc = getPage(targetURL);
//        Document doc = Jsoup.parse(HTMLDoc);

        Elements content = doc.getElementsByClass(targetHTMLElementClass_layer_0);

        NewsData[] BBC_News = new NewsData[10];

        int counter = 0;
        System.out.println("Found " + content.size() + " top news: ");
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
//                String firstParagraph; // not gonna work coz no paragraph layout

                innerArticle = innerNewsPageContent.select("h1.story-body__h1").text();

                article = innerArticle;
            }

            BBC_News[counter - 5] = new NewsData(link, article);

            if (verbose_mode) {
                System.out.println(String.valueOf(counter - 5) + ". " + BBC_News[counter - 5]);
                System.out.println();
            }

            counter++;
        }

        return BBC_News;
    }


    static public void main(String[] args) {
        Parser_00 unit00 = new Parser_00(false);
        try {
            NewsData[] BBC_News = unit00.parsePage();
            serializeNewsData(BBC_News);
            NewsData[] somedata = deserializeNewsData();

            try {
                SQL_Adapter.WriteToTargetTable(BBC_News);
            } catch (SQLException | ClassNotFoundException ex) {
                err.println("Can Not Write to database: \n" + ex);
            }

//            for (int i = 0; i < BBC_News.length; ++i) {
//                System.out.println("News " + i + " : ");
//                System.out.println(somedata[i]);
//                System.out.println(BBC_News[i]);
//                System.out.println();
//            }
        } catch (IOException ioe) {
            err.println(ioe.getMessage() + "\n");
            ioe.printStackTrace();
        }
    }
}
