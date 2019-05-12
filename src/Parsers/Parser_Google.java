package Parsers;

import SQL.SQL_Adapter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static java.lang.System.err;

public class Parser_Google extends AbstractParser {

    private static final String targetURL = "https://news.google.com/?hl=ru&gl=RU&ceid=RU%3Aru";
    private static final String TIME_CLASS_HOLDER = "WW6dff uQIVzc Sksgp";
    private static final String SOURCE_HOLDER_CLASS = "wEwyrc AVN2gc uQIVzc Sksgp";
    private static final String News_Holder_CLASS = " MQsxIb xTewfe tXImLc R7GTQ keNKEd keNKEd  dIehj EjqUne";

    private final boolean verbose_mode;

    public Parser_Google(boolean verbose_mode) {
        this.verbose_mode = verbose_mode;
    }

    @Override
    public NewsData[] parsePage() throws IOException {
        List<NewsData> newsDataList = new LinkedList<>();
        Document doc = Jsoup.connect(targetURL).get();

        Elements news = doc.getElementsByClass(News_Holder_CLASS);
        if (verbose_mode) {
            System.out.println("Found " + (news.size()) + " links: ");
        }
        for (Element el : news) {
            Element linkHolder = el.getElementsByTag("h4").first();
            if (linkHolder == null)
                continue;
            Element link = linkHolder.select("a[href]").first();
            Element source = el.getElementsByClass(SOURCE_HOLDER_CLASS).first();
            Element time = el.getElementsByClass(TIME_CLASS_HOLDER).first();
            if (link == null || source == null || time == null)
                continue;
            if (verbose_mode) {
                System.out.println(
                        "text : " + link.text() +
                                "\nlink : " + link.attr("abs:href") +
                                "\ntime : " + time.attr("datetime").substring(0, 10).replaceAll("-", ".") +
                                "\nsource : " + source.text() + "\n\n");
            }
            newsDataList.add(new NewsData(
                    link.attr("abs:href"),
                    link.text(),
                    time.attr("datetime").substring(0, 10).replaceAll("-", "."),
                    source.text()
            ));
        }

        NewsData[] BBC_News_Arr = new NewsData[newsDataList.size()];
        return newsDataList.toArray(BBC_News_Arr);
    }


    static public void main(String[] args) {
        Parser_Google unit01 = new Parser_Google(true);
        try {
            NewsData[] BBC_News = unit01.parsePage();


            try {
                SQL_Adapter.WriteToTargetTable(BBC_News);
            } catch (SQLException | ClassNotFoundException ex) {
                err.println("Can Not Write to database: \n" + ex);
            }

        } catch (IOException ioe) {
            err.println(ioe.getMessage() + "\n");
            ioe.printStackTrace();
        }
    }
}

