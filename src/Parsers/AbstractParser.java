package Parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class AbstractParser {

    public abstract NewsData[] parsePage() throws IOException;
    /**
     * Downloads html content of the page on the target url
     * @param URL
     * @return html doc in string form
     */
    static String getPage(String URL) throws IOException {

        BufferedReader rd = null;
        HttpURLConnection myConn = null;
        StringBuilder result = null;

        try {
            // creates a proper URL request
            java.net.URL myURL = new URL(URL);
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

            // frees resources
            rd.close();
            myConn.disconnect();

            return result.toString();
        } finally {
            // ensures all resources are free
            if (myConn != null)
                myConn.disconnect();
        }
    }
}
