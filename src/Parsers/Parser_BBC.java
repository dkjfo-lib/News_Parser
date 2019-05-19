package Parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Parser_BBC extends AbstractParser {
    private final String targetURL = "https://www.bbc.com/news";
    private static final String TIME_CLASS_HOLDER = "gs-o-bullet__text date qa-status-date";
    private static final String News_Holder_CLASS = "gs-c-promo-body gel-1/2@xs gel-1/1@m gs-u-mt@m";

    private final boolean verbose_mode;

    public Parser_BBC(boolean verbose_mode) {
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
            Element link = el.select("a[href]").first();
            Element time = el.getElementsByClass(TIME_CLASS_HOLDER).first();
            if (link == null || time == null)
                continue;
            if (verbose_mode) {
                System.out.println(
                        "text : " + link.text() +
                                "\nlink : " + link.attr("abs:href") +
                                "\ntime : " + time.attr("datetime").substring(0, 10).replaceAll("-", ".") +
                                "\nsource : " + "BBC" + "\n\n");
            }
            newsDataList.add(new NewsData(
                    link.attr("abs:href"),
                    link.text(),
                    time.attr("datetime").substring(0, 10).replaceAll("-", "."),
                    "BBC"
            ));
        }

        NewsData[] BBC_News_Arr = new NewsData[newsDataList.size()];
        return newsDataList.toArray(BBC_News_Arr);
    }
}
