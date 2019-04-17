package mySpace;

import java.io.*;
import java.net.*;

public class HTML_Getter {

    /**
     * Downloads html content of the page on the target url
     * @param URL
     * @return html doc in string form
     */
    static String getHTML(String URL) {

        BufferedReader rd = null;
        HttpURLConnection myConn = null;
        StringBuilder result = null;

        try {
            // creates a proper URL request
            URL myURL = new URL(URL);
            myConn = (HttpURLConnection) myURL.openConnection();
            myConn.setDoOutput(true);
            myConn.setRequestMethod("GET");
            myConn.setRequestProperty("User-Agent", "dat ist custom bot to get some info");
            myConn.setConnectTimeout(5000);
            myConn.setReadTimeout(5000);

            // sends "GET" request / gets input stream
            rd = new BufferedReader(new InputStreamReader(myConn.getInputStream()));
            // creates output string
            String line;
            result = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                result.append(line);
                result.append('\n');
            }
            rd.close();
            myConn.disconnect();
            return result.toString();
        } catch (IOException e) {
            System.out.println("Could not connect to server " + e.getMessage());
        } finally {
            // ensures all resources are free
            if (myConn != null)
                myConn.disconnect();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getHTML("https://www.reddit.com/r/spacex/wiki/launches/manifest"));
    }
}
