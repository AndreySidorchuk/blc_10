package ru.tinkoff.qa.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnectionCreator {
    //host: jdbc:h2:mem:myDb
    //user: sa
    //password: sa
    public static Connection createConnection() throws SQLException {
        String host = "jdbc:h2:mem:myDb";
        String user = "sa";
        String password = "sa";
        return DriverManager.getConnection(host, user, password);
    }
}
