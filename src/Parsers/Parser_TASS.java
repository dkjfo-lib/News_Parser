package Parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Parser_TASS extends AbstractParser {
    private static final String targetURL = "http://tass.com/world";
    private static final String TIME_CLASS_HOLDER = "b-news-item__date";
    private static final String News_Holder_CLASS = "b-news-item__text";

    private final boolean verbose_mode;

    public Parser_TASS(boolean verbose_mode) {
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
            Element article = el.getElementsByTag("a").first();
            Element link = article.select("a[href]").first();
            Element time = el.getElementsByClass(TIME_CLASS_HOLDER).first();
            if (link == null || time == null)
                continue;
            if (verbose_mode) {
                System.out.println(
                        "text : " + link.text() +
                                "\nlink : " + link.attr("abs:href") +
                                "\ntime : " + time.text() +
                                "\nsource : " + "TASS" + "\n\n");
            }
            newsDataList.add(new NewsData(
                    link.attr("abs:href"),
                    link.text(),
                    time.text(),
                    "TASS"
            ));
        }

        NewsData[] BBC_News_Arr = new NewsData[newsDataList.size()];
        return newsDataList.toArray(BBC_News_Arr);
    }
}

