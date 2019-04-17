/*
package mySpace;

import javax.naming.InvalidNameException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class U_PARSER {

    static Map<String, Integer> mounths = new HashMap<>();

    static {
        mounths.put("Jan", 1);
        mounths.put("Feb", 2);
        mounths.put("Mar", 3);
        mounths.put("Apr", 4);
        mounths.put("May", 5);
        mounths.put("Jun", 6);
        mounths.put("Jul", 7);
        mounths.put("Aug", 8);
        mounths.put("Sep", 9);
        mounths.put("Oct", 10);
        mounths.put("Nov", 11);
        mounths.put("Dec", 12);
    }

    private static long getDate(String date, String dateFormat){
        SimpleDateFormat f = new SimpleDateFormat(dateFormat);
        try {
            Date d = f.parse(date);
            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    static long StringToDate(String date) throws InvalidNameException {
        if (date.matches("\\d{4}"))
            return getDate(date, "yyyy");
        if (date.matches("\\d{4} \\W{3}")){

            return getDate(date, "yyyy mm");
        }
        int[] dates = new int[5];
        String[] parts = date.split(" ");
        "dd-MMM-yyyy"
        return dates;
    }

    public static void main(String[] args) {
        System.out.println(StringToDate("2018 TBD"));
        System.out.println(StringToDate("2018 Dec 4 [18:38]"));
        System.out.println(StringToDate("2018 Dec 18 [14:10]"));
        System.out.println(StringToDate("2019 Jan"));
        System.out.println(StringToDate("2019 Q1"));
        System.out.println(StringToDate("2019 H2"));
        System.out.println(StringToDate("2021"));
        System.out.println(StringToDate("TBA"));
    }
}
//*/
