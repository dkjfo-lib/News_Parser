package mySpace;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


//      https://www.reddit.com/r/spacex/wiki/launches/manifest
//      https://spaceflightnow.com/launch-schedule/

public class Main {

    static Map<String, String> sites = new HashMap<>();

    static String[] encodings = new String[] {"windows-1251","UTF-8"};

    static {
        sites.put("spaceX", "https://www.reddit.com/r/spacex/wiki/launches/manifest");
        sites.put("flightNow", "https://spaceflightnow.com/launch-schedule/");
    }

    public static void main(String[] args) {
        System.out.println(sites.get("spaceX"));
        try {
            FileWork.writeFile("RawHTML\\spaceX.html", (HTML_Getter.getHTML(sites.get("spaceX"))));
            FileWork.writeFile("ParsedHTML\\spaceX", SpaceX_parcer.parse("spaceX.html"));
        } catch (IOException e) {
            System.out.println("fuck_" + e);
        }
    }
}
