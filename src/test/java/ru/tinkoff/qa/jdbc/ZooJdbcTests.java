package ru.tinkoff.qa.jdbc;

import org.junit.jupiter.api.*;
import ru.tinkoff.qa.BeforeCreator;

import java.sql.*;

public class ZooJdbcTests {

    private static Connection connection;

    @BeforeAll
    static void init() {
        BeforeCreator.createData();
    }

    @BeforeEach
    public void beforeEach() throws SQLException {
        connection = JdbcConnectionCreator.createConnection();
    }

    @AfterEach
    public void afterEach() throws SQLException {
        connection.close();
    }

    /**
     * В таблице public.animal ровно 10 записей
     */
    @Test
    public void countRowAnimal() throws SQLException {
        String query = "SELECT count(*) AS kol FROM animal";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        Assertions.assertEquals(10, resultSet.getInt("kol"));
    }

    /**
     * В таблицу public.animal нельзя добавить строку с индексом от 1 до 10 включительно
     */
    @Test
    public void insertIndexAnimal() throws SQLException {
        for (int i = 1; i <= 10; i++) {
            String queryInsert = "INSERT INTO animal (id,\"name\",age,\"type\",sex,place) VALUES ((?),'Ряба',3,2,1,2)";
            PreparedStatement statement = connection.prepareStatement(queryInsert);
            statement.setInt(1, i);

            SQLException exception = Assertions.assertThrows(SQLException.class, () -> {
                statement.executeUpdate();
            });
            Assertions.assertTrue(exception.getMessage().contains("Нарушение уникального индекса или первичного ключа"));
        }
    }

    /**
     * В таблицу public.workman нельзя добавить строку с name = null
     */
    @Test
    public void insertNullToWorkman() throws SQLException {
        String queryInsert = "INSERT INTO workman (id,\"name\",age,\"position\") VALUES (33,null,33,33)";
        Statement statement = connection.createStatement();

        SQLException exception = Assertions.assertThrows(SQLException.class, () -> {
            statement.executeUpdate(queryInsert);
        });
        Assertions.assertTrue(exception.getMessage().contains("Значение NULL не разрешено для поля"));
    }

    /**
     * Если в таблицу public.places добавить еще одну строку, то в ней будет 6 строк
     */
    @Test
    public void insertPlacesCountRow() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO places (id,\"row\",place_num,\"name\") VALUES (55,55,55,'Орехово')");
        String query = "SELECT COUNT(*) FROM places";
        try {
            ResultSet resultSetSelect = statement.executeQuery(query);
            resultSetSelect.next();
            Assertions.assertEquals(6, resultSetSelect.getInt(1));
        } finally {
            statement.execute("DELETE FROM places WHERE id=55");
        }
    }

    /**
     * В таблице public.zoo всего три записи с name 'Центральный', 'Северный', 'Западный'
     */
    @Test
    public void countRowZoo() throws SQLException {
        String query = "SELECT * FROM zoo";
        String queryCount = "SELECT count(*) AS kol FROM zoo";
        Statement statement = connection.createStatement();
        ResultSet resultSetSelect = statement.executeQuery(query);
        resultSetSelect.next();
        Assertions.assertEquals("Центральный", resultSetSelect.getString("name"));
        resultSetSelect.next();
        Assertions.assertEquals("Северный", resultSetSelect.getString("name"));
        resultSetSelect.next();
        Assertions.assertEquals("Западный", resultSetSelect.getString("name"));
        ResultSet resultSet = statement.executeQuery(queryCount);
        resultSet.next();
        Assertions.assertEquals(3, resultSet.getInt("kol"));
    }
}
