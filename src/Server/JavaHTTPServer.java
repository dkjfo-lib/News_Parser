package Server;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.StringTokenizer;

/// Each client handled in a separate thread
public class JavaHTTPServer implements Runnable {

    // directory constants
    final static File WEB_ROOT = new File("WebHTML");//WebHTML
    final static String DEFAULT_FILE = "open.html";
    final static String FILE_NOT_FOUND = "404.html";
    final static String METHOD_NOT_SUPPORTED = "NotSupport.html";
    // PORT
    static final int PORT = 8080;

    // more info mode
    static final boolean verbose = true;

    // Client enter point
    private Socket connect;

    public JavaHTTPServer(Socket connect) {
        this.connect = connect;
    }

    public static void main(String[] args) {
        try {
//            InetSocketAddress currAdress = new InetSocketAddress("92.242.59.6", PORT);
            InetSocketAddress currAdress = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(),PORT);
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(currAdress);

            System.out.println("JavaHTTPServer started.");
            System.out.println("Listen for connection on HOST : " + serverSocket.getLocalSocketAddress() + "...");
            System.out.println("Listen for connection on PORT : " + PORT + "...");
            System.out.println();

            // deal in `https://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html` style
            while (true) {
                Socket newClientRequest = serverSocket.accept();
                JavaHTTPServer myServer = new JavaHTTPServer(newClientRequest);
                if (verbose) {
                    System.out.println("Connection opened with " + newClientRequest.getInetAddress().getHostName() + ". (" + new Date() + ")");
                }
                // create new thread for the connection
                Thread thread = new Thread(myServer);
                thread.start();
            }

        } catch (IOException e) {
            System.err.println("JavaHTTPServer Connection error : " + e.getMessage());
        }
    }

    @Override
    public void run() {
        // manage connection with the particular client
        BufferedReader in = null;
        PrintWriter out = null;
        BufferedOutputStream dataOut = null;
        String fileRequested = null;

        try {

            // we read characters from the client via input stream on the socket
            in = new BufferedReader(new InputStreamReader(connect.getInputStream(), StandardCharsets.UTF_8));

            // we get characters output stream to client (for headers)
            out = new PrintWriter(connect.getOutputStream());
            // get binary output stream to client (for requested data)
            dataOut = new BufferedOutputStream(connect.getOutputStream());

            // get first line of the request from the client
            String input = in.readLine();
            // we parse the request with a string tokenizer
            System.out.println("in stream : " + input);
            StringTokenizer parse = new StringTokenizer(input);
            String method = parse.nextToken().toUpperCase();    //
            //we get file request
            fileRequested = parse.nextToken().toLowerCase();

            // we support only GET and HEAD methods, we check
            if (!method.equals("GET") && !method.equals("HEAD")) {
                if (verbose) {
                    System.out.println("501 Not Implemented : " + method + " method");
                }
                // return `METHOD_NOT_SUPPORTED` page
                File file = new File(WEB_ROOT, METHOD_NOT_SUPPORTED);
                int fileLength = (int) file.length();
                String conectMimeType = "text/html";
                // read content to return to client
                byte[] fileData = readFileData(file, fileLength);

                // we send HTTP Headers with data to client
                out.print("HTTP/1.1 501 Not Implemented");
                out.print("Server: Java HTTP Server from Michale : 1.0");
                out.print("Date: " + new Date());
                out.print("Content-type: " + conectMimeType);
                out.print("Content-length: " + fileLength);
                out.println();  // very important blank line between header and content
                out.println();  // very important blank line between header and content
                out.flush();
                //file
                dataOut.write(fileData, 0, fileLength);
                dataOut.flush();

            } else {
                // GET and HEAD methods
                if (fileRequested.endsWith("/")) {
                    fileRequested += DEFAULT_FILE;
                }

                File file = new File(WEB_ROOT, fileRequested);
                int fileLength = (int) file.length();
                String content = getContentType(fileRequested);

                // GET method
                if (method.equals("GET")) {
                    byte[] fileData = readFileData(file, fileLength);

                    // we send HTTP Headers with data to client
                    out.print("HTTP/1.1 200 OK");
                    out.print("Server: Java HTTP Server from Michale : 1.0");
                    out.print("Date: " + new Date());
                    out.print("Content-type: " + content);
                    out.print("Content-length: " + fileLength);
                    out.println();  // very important blank line between header and content
                    out.println();  // very important blank line between header and content
                    out.flush();
                    //file
                    dataOut.write(fileData, 0, fileLength);
                    dataOut.flush();

                    if (verbose) {
                        System.out.println("File " + fileRequested + " of type " + content + " returned");
                        System.out.println("File length : " + fileLength + " bytes");
                    }
                }
            }

        } catch (FileNotFoundException fnfe) {
            try {
                fileNotFound(out, dataOut, fileRequested);
            } catch (IOException ioe) {
                System.err.println("Error with file not found exception : " + ioe.getMessage());
            }
        } catch (IOException ioe) {
            System.err.println("Server error : " + ioe.getMessage());
        } finally {
            try {
                in.close();
                out.close();
                dataOut.close();
                connect.close();
            } catch (IOException ioe) {
                System.err.println("Error closing stream : " + ioe.getMessage());
            }

            if (verbose) {
                System.out.println("Connection closed\n");
            }
        }

    }

    private byte[] readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];

        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null)
                fileIn.close();
        }

        return fileData;
    }

    // return supported MIME types
    private String getContentType(String fileRequested) {
        if (fileRequested.endsWith(".html") || fileRequested.endsWith(".htm"))
            return "text/html";
        else
            return "text/plain";
    }

    // return
    private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
        File file = new File(WEB_ROOT, FILE_NOT_FOUND);
        int fileLength = (int) file.length();
        String content = "text/html";
        byte[] fileData = readFileData(file, fileLength);

        // we send HTTP Headers with data to client
        out.print("HTTP/1.1 404 File Not Found");
        out.print("Server: Java HTTP Server from Michale : 1.0");
        out.print("Date: " + new Date());
        out.print("Content-type: " + content);
        out.print("Content-length: " + fileLength);
        out.println();  // very important blank line between header and content
        out.println();  // very important blank line between header and content
        out.flush();
        //file
        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();

        if (verbose) {
            System.out.println("File " + fileRequested + " not found");
        }
    }

}
