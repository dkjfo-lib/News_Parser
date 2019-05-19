package SQL;

import Parsers.NewsData;
import Server.MyWindow;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import com.mysql.jdbc.*;

public class SQL_Adapter {
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "1234";

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String SQL_TIME_ZONE = "?verifyServerCertificate=false&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String SQL_TIME_ZONE2 = "?useSSL=false&allowPublicKeyRetrieval=true&verifyServerCertificate=false&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    private static final String DB_root_URL = "jdbc:mysql://localhost:3306" + SQL_TIME_ZONE2;
    private static final String DB_name = "NewsData";
    private static final String target_DB_URL = "jdbc:mysql://localhost:3306/" + DB_name + SQL_TIME_ZONE2;
    private static final String table_name = "News_test04";

    private static boolean initialized = false;
    private static final boolean VERBOSE_MODE = false;

    private static void Init() throws ClassNotFoundException, SQLException {
//        Class.forName(JDBC_DRIVER);
        // create database if needed
        CreateDataBaseIfNeeded();
        // create table in new(old) database if needed
        CreateTableIfNeeded();
        initialized = true;
    }

    private static void CreateDataBaseIfNeeded() throws SQLException {
        out("Connecting to database root...");
        try (Connection conn = DriverManager.getConnection(DB_root_URL, USER_NAME, PASSWORD)) {
            out("Creating database \"" + DB_name + "\"...");
            Statement statement = conn.createStatement();

            String sql = "create database if not exists " + DB_name;
            statement.executeUpdate(sql);
            out("Database \"" + DB_name + "\" created successfully...");
        }
    }

    private static void CreateTableIfNeeded() throws SQLException {
        out("Connecting to target database...");
        try (Connection conn = DriverManager.getConnection(target_DB_URL, USER_NAME, PASSWORD)) {
            out("Creating table \"" + table_name + "\"...");
            Statement statement = conn.createStatement();

            // TODO link may be longer than 255char
            String sql = "create table if not exists " + table_name +
                    "(link char (255)not null," +
                    "articleText text not null," +
                    "timeText text," +
                    "sourceName text," +
                    "primary key (link))";
            statement.executeUpdate(sql);
            out("table \"" + table_name + "\" created successfully...");
        }
    }

    public static void WriteToTargetTable(NewsData[] data) throws SQLException, ClassNotFoundException {
        if (!initialized) {
            Init();
        }
        out("Connecting to target database...");
        try (Connection conn = DriverManager.getConnection(target_DB_URL, USER_NAME, PASSWORD)) {
            out("Inserting records into the table\"" + table_name + "\"...");

            for (int i = 0; i < data.length; ++i) {
                String sql = "insert into " + table_name +
                        " set link = ? , articleText = ?, timeText = ?, sourceName = ?"; //where id = ?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, data[i].link);
                statement.setString(2, data[i].article);
                statement.setString(3, data[i].date);
                statement.setString(4, data[i].sourceName);
                try {
                    statement.executeUpdate();
                    out(statement.toString());
                } catch (Exception ignore) {
                }
            }
            out("All records were inserted into the table\"" + table_name + "\" successfully...");
        }
    }

    //__________READER________________

    public static NewsData[] getAllContent() throws SQLException, ClassNotFoundException {
        if (!initialized) {
            Init();
        }
        out("Connecting to target database...");
        List<NewsData> data = new LinkedList<>();
        try (
                Connection conn = DriverManager.getConnection(target_DB_URL, USER_NAME, PASSWORD);
                Statement statement = conn.createStatement()) {
            out("getting content from the table\"" + table_name + "\"...");

            ResultSet resultSet = statement.executeQuery("SELECT * from newsdata.news_test04");
            while (resultSet.next()) {
                data.add(
                        new NewsData(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getString(4)
                        ));
            }
            out("Content was received from the table\"" + table_name + "\" successfully...");
        }
        NewsData[] dataType = new NewsData[data.size()];
        return data.toArray(dataType);
    }

    static void out(String message) {
        if (VERBOSE_MODE)
            MyWindow.instance.writeLog(message);
    }
}
