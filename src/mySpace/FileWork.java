package mySpace;

import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;

import java.io.*;

public class FileWork {

    private static String defLocation = "";

    static void writeFile(String fileName, String content) throws IOException {
        String out = new String(content.getBytes(),"UTF-8");
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(content);

        writer.close();
    }

    static String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        StringBuilder sb = new StringBuilder();
        String st;
        while ((st = br.readLine()) != null)
            sb.append(st);
        return sb.toString();
    }

    public static void main(String[] args){
        try{
            String res = readFile("spaceX.xml");
            System.out.println(res);
            writeFile("test\\test_encod.xml",res);
        }
        catch (IOException e){
            System.out.println("could not read from file: " + e);
        }
    }
}
