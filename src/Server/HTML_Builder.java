package Server;

import Parsers.NewsData;
import SQL.SQL_Adapter;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
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
            File resultFile = new File("WebHTML\\result.html");
            try (BufferedWriter br = new BufferedWriter(new FileWriter(resultFile))) {
                br.write(htmlDoc);
                br.flush();
            }
            return resultFile;
        } catch (IOException | ClassNotFoundException | SQLException err) {
            return null;
        }
    }


    //___Inner_Members__

    private static String populateDoc(String documentContent) {
        Document doc = Jsoup.parse(documentContent);
        try {
            return populateDoc(doc, SQL_Adapter.getAllContent()).toString();
        } catch (ClassNotFoundException | SQLException err) {
            return null;
        }
    }

    private static String populateDoc(String Doc, NewsData[] data) {
        Document doc = Jsoup.parse(Doc);
        return populateDoc(doc, data).toString();
    }

    private static Document populateDoc(Document doc, NewsData[] data) {
        Element table = doc.getElementById("table");
        out.println( "DEBUG_____________" + table);
        doc.getElementById("table").append((getDataTable(data)));
        return doc;
    }

    private static String getDataTable(NewsData[] data) {
        StringBuilder builder = new StringBuilder();
        builder.append("<tr>");
        builder.append("<th>article</th>");
        builder.append("<th>link</th>");
        builder.append("</tr>");
        for (int i = 0; i < data.length; ++i) {
            builder.append("<tr>");
            addCell(builder, data[i].article);
            addCell(builder, data[i].link);
            builder.append("</tr>");
        }
        return builder.toString();
    }

    private static void addCell(StringBuilder builder, String content) {
        builder.append("<td>");
        builder.append(content);
        builder.append("</td>");
    }


    private final static File WEB_ROOT = new File("WebHTML");//WebHTML
    private final static String DEFAULT_FILE = "open.html";

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        NewsData[] tableData = SQL_Adapter.getAllContent();


        StringBuilder doc = new StringBuilder();
        {
            File file = new File(WEB_ROOT, DEFAULT_FILE);
            BufferedReader br = new BufferedReader(new FileReader(file));

            String st;
            while ((st = br.readLine()) != null)
                doc.append(st);
        }
        String htmlDoc = doc.toString();

        out.println(htmlDoc);
        htmlDoc = populateDoc(htmlDoc, tableData);
        out.println(htmlDoc);
    }

}
