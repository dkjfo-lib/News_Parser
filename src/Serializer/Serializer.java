package Serializer;

import Parsers.NewsData;

import java.io.*;

public class Serializer {

    static final String filename = "NewsData";

    public static boolean serializeNewsData(NewsData[] objects){
        try
        {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(objects);

            out.close();
            file.close();
            return true;
        }catch (IOException ioe){
            System.err.println("exception during serialization : " + ioe);
        }
        return false;
    }


    public static NewsData[] deserializeNewsData(){
        NewsData[] arr = null;
        try
        {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            arr = (NewsData[])in.readObject();

            in.close();
            file.close();

        }catch (IOException | ClassNotFoundException ex){
            System.err.println("exception during deserialization : " + ex);
        }
        return arr;
    }
}
