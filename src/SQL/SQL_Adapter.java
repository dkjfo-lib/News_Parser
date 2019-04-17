package SQL;

import Parsers.NewsData;

import java.io.IOException;
import java.sql.*;

public class SQL_Adapter {

    private static final boolean VERBOSE_MODE = true;

    private static final String USER_NAME = "root";
    private static final String PASSWORD = "1234";

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String SQL_TIME_ZONE_PROBLEM_SOLVE = "?verifyServerCertificate=false&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";


    private static final String DB_root_URL = "jdbc:mysql://localhost:3306" + SQL_TIME_ZONE_PROBLEM_SOLVE;
    private static final String DB_name = "NewsData";
    private static final String target_DB_URL = "jdbc:mysql://localhost:3306/" + DB_name + SQL_TIME_ZONE_PROBLEM_SOLVE;
    private static final String table_name = "News_test03";

    private static boolean initialized = false;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Init();
    }

    private static void Init() throws ClassNotFoundException, SQLException {
        Class.forName(JDBC_DRIVER);
        // create database if needed
        CreateDataBaseIfNeeded();
        out("");
        // create table in new(old) database if needed
        CreateTableIfNeeded();
        initialized = true;
    }

    private static void CreateDataBaseIfNeeded() throws SQLException {
        CreateDataBaseIfNeeded(DB_root_URL, DB_name);
    }

    private static void CreateDataBaseIfNeeded(String DB_root_URL, String DB_Name) throws SQLException {
        out("Connecting to database root...");
        try (Connection conn = DriverManager.getConnection(DB_root_URL, USER_NAME, PASSWORD)) {
            out("Creating database \"" + DB_Name + "\"...");
            Statement statement = conn.createStatement();

            String sql = "create database if not exists " + DB_Name;
            statement.executeUpdate(sql);
            out("Database \"" + DB_Name + "\" created successfully...");
        }
    }

    private static void CreateTableIfNeeded() throws SQLException {
        CreateTableIfNeeded(target_DB_URL, table_name);
    }

    private static void CreateTableIfNeeded(String DB_URL, String table_Name) throws SQLException {
        out("Connecting to target database...");
        try (Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD)) {
            out("Creating table \"" + table_Name + "\"...");
            Statement statement = conn.createStatement();

            // TODO link may be longer than 255char
            String sql = "create table if not exists " + table_Name +
                    "(link char (255)not null," +
                    "article text not null," +
                    "primary key (link))";
            statement.executeUpdate(sql);
            out("table \"" + table_Name + "\" created successfully...");
        }
    }

    public static void WriteToTargetTable(NewsData[] data) throws SQLException, ClassNotFoundException {
        WriteToTargetTable(target_DB_URL, table_name, data);
    }

    private static void WriteToTargetTable(String DB_URL, String table_Name, NewsData[] data) throws SQLException, ClassNotFoundException {
        if (!initialized) {
            Init();
        }
        out("Connecting to target database...");
        try (Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD)) {
            out("Inserting records into the table\"" + table_Name + "\"...");

            for (int i = 0; i < data.length; ++i) {
                String sql = "insert into " + table_Name +
                        " set link = ? , article = ?"; //where id = ?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, data[i].link);
                statement.setString(2, data[i].article);
//                statement.setInt(3, i+1);
                try {
                    statement.executeUpdate();
                    out(statement.toString());
                }catch (Exception ignore){}
            }
            System.out.println("All records were inserted into the table\"" + table_Name + "\" successfully...");
        }
    }

//    static String prepareStringToDataBase(String string) {
//        return string.replaceAll(CRITICAL_CHARACTERS, "");
//    }

    static void out(String message) {
        if (VERBOSE_MODE)
            System.out.println(message);
    }
}
