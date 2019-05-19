package Server;

import Parsers.NewsData;
import SQL.SQL_Adapter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.sql.SQLException;

import static java.lang.System.out;

public class HTML_Builder {

    //___API__

    public static File populateDoc(File canvasFile) {
        try {
            String htmlDoc;
            try (BufferedReader br = new BufferedReader(new FileReader(canvasFile))) {
                StringBuilder builder = new StringBuilder();
                String st;
                while ((st = br.readLine()) != null)
                    builder.append(st);
                htmlDoc = builder.toString();
            }
            htmlDoc = populateDoc(htmlDoc, SQL_Adapter.getAllContent());
            File resultFile = new File("Resources\\result.html");
            try (BufferedWriter br = new BufferedWriter(new FileWriter(resultFile))) {
                br.write(htmlDoc);
                br.flush();
            }
            return resultFile;
        } catch (IOException | ClassNotFoundException | SQLException ioe) {
            MyWindow.instance.writeLog("Error : can not build page : \n\t" + ioe.toString());
            return null;
        }
    }


    //___Inner_Members__

    private static String populateDoc(String Doc, NewsData[] data) {
        Document doc = Jsoup.parse(Doc);
        return populateDoc(doc, data).toString();
    }

    private static Document populateDoc(Document doc, NewsData[] data) {
        Element table = doc.getElementById("table");
        doc.getElementById("table").append((getDataTable(data)));
        return doc;
    }

    private static String getDataTable(NewsData[] data) {
        StringBuilder builder = new StringBuilder();
        builder.append("<tr>");
        builder.append("<th>NEWS</th>");
        builder.append("</tr>");
        for (int i = data.length-1; i > data.length-100 && i >= 0; --i) {
            builder.append("<tr>");
            addCell(builder, data[i]);
            builder.append("</tr>");
        }
        return builder.toString();
    }

    private static void addCell(StringBuilder builder, NewsData data) {
        builder.append("<td class=\"newsContent\">");
        builder.append("<a href=\"");
        builder.append(data.link);
        builder.append("\">");
        builder.append(data.article);
        builder.append("</a>");
        builder.append("<p>");
        builder.append(data.sourceName);
        builder.append("</p>");
        builder.append("<p>");
        builder.append(data.date);
        builder.append("</p>");
        builder.append("</td>");
    }
}
