package mySpace;

import org.jsoup.*;
import org.jsoup.helper.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.io.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class SpaceX_parcer {

    private static char sepSymbol = ',';
    private static String[] columns = {"date", "rocket", "LS", "orbit", "PM", "payload", "customer"};


    static String parse(String filepath) throws IOException {

        Document doc = Jsoup.parse(FileWork.readFile(filepath));

        // selects first table from class "md wiki"
        Element content = doc.getElementsByClass("md wiki").select("table").first();
        Elements tableCells = content.select("td");
        tableCells.removeIf(el -> el.text().isEmpty());

        //return parseByArrays(tableCells);
        return parseBySB(tableCells);
    }

    private static String parseByArrays(Elements tableCells) {
        // gowther data (divide by 8 coz original table has 8 columns, not 7)
        String[][] tableData = new String[tableCells.size() / 8][];
        String[] oneLaunchData = new String[7];
        int j = 0;
        int i = 0;
        for (Element link : tableCells) {
            if (i == 7) {
                i = 0;
                tableData[j] = oneLaunchData.clone();
                j++;
                //System.out.println();
                continue;
            }
            if (!link.text().isEmpty()) {
                oneLaunchData[i++] = link.text();
                //System.out.println(columns[i - 1] + " : " + link.text());
            }
        }
        StringBuilder sb = new StringBuilder();
        String sepString = Character.toString(sepSymbol);
        for (String[] line : tableData) {
            sb.append(String.join(sepString, line));
            sb.append('\n');
        }
        return sb.toString();
    }

    private static String parseBySB(Elements tableCells) {
        int i = 0;
        StringBuilder sb = new StringBuilder();
        for (Element link : tableCells) {
            if (i == 7) {
                i = 0;
                sb.append('\n');
                continue;
            }
            if (!link.text().isEmpty()) {
                i++;
                sb.append(link.text());
                sb.append(sepSymbol);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        try {
            long n = 2000;
            long sum = 0;
            for (int i = 0; i < n; i++) {
                long t1 = System.currentTimeMillis();
                parse("spaceX.html");
                long t2 = System.currentTimeMillis();
                sum += t2 - t1;
            }
            System.out.println("delta time = " + (sum/n));
        } catch (IOException e) {
            System.out.println("fuck_" + e);
        }
    }
}
