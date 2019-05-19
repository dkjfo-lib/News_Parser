package Parsers;

import SQL.SQL_Adapter;
import Server.MyWindow;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

public class ParsersTimer implements Runnable {

    private final MyWindow window;
    Date nextUpdateDate;
    Date lastUpdateDate;
    private int delayMilSec = 20 * 1000 * 60 ;
    public boolean run = false;

    public ParsersTimer(MyWindow window) {
        this.window = window;
    }

    private void out(String message) {
        window.writeLog(message);
    }

    public void setDelay(int newDelay) {
        delayMilSec = newDelay * 1000 * 60 ;
        if (run){
            lastUpdateDate = new Date();
            nextUpdateDate = (new Date());
            nextUpdateDate.setTime(lastUpdateDate.getTime() + delayMilSec);
            out("Next Parser Launch at " + nextUpdateDate);
        }
    }
    public int getDelay() {
        return delayMilSec/ 60000;
    }

    public void run() {
        run = true;
        out("Timer started");
        AbstractParser[] parsers = new AbstractParser[]{
                new Parser_Google(false),
                new Parser_BBC(false),
                new Parser_TASS(false)
        };
        while (run) {
            out("Parsers started");
            boolean error = false;
            for (AbstractParser parser : parsers) {
                try {
                    SQL_Adapter.WriteToTargetTable(parser.parsePage());
                } catch (IOException | ClassNotFoundException | SQLException ioe) {
                    out("Parser failed to provide data : \n\t" + ioe.toString());
                    error = true;
                }
            }
            if (!error) {
                out("Parsers successfully finished");
            }
            lastUpdateDate = new Date();
            nextUpdateDate = (new Date());
            nextUpdateDate.setTime(lastUpdateDate.getTime() + delayMilSec);
            out("Next Parser Launch at " + nextUpdateDate);
            while (nextUpdateDate.after(new Date()) && run) {
                try {
                    Thread.currentThread().sleep(2000);
                } catch (InterruptedException e) {
                    out("Error in timer thread : " + e.toString());
                }
            }
        }
        out("Timer stop");
    }
}
