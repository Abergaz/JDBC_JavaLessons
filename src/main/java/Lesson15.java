import com.mysql.cj.jdbc.DatabaseMetaData;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;


public class Lesson15 {
    private static String userName = "root";
    private static String password = "1111";
    private static String url = "jdbc:mysql://localhost:3306/test";


    public static void main(String[] args) {
        try (
                Connection connection = DriverManager.getConnection(url, userName, password);
                /** Для того использовать прокручиваемые наборы реузьтатов и набры строк
                 * необходмо при создании Statement или PrepareStatement передать параметры
                 * 1-учитывать или нет изменения в таблицы, 2-можно или нет менять данные
                 * TYPE_SCROLL_SENSITIVE - чтобы читать и видеть изменения в БД
                 * CONCUR_UPDATABLE - чтобы вносить изменения
                 */
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

        ) {
            System.out.println("Connection open");
            //Запросы на изменение данных executeUpdate
            statement.executeUpdate("DROP TABLE iF EXISTS Books");
            statement.executeUpdate("CREATE TABLE IF NOT  EXISTS Books (id MEDIUMINT NOT NULL AUTO_INCREMENT, name VARCHAR(30) not null, dt DATE, PRIMARY KEY(id))");
            statement.executeUpdate("INSERT INTO books (name,dt) values ('Salamon Key','2020-05-09')");
            statement.executeUpdate("INSERT INTO books (name,dt) values ('Inferno','2020-05-09')");
            statement.executeUpdate("INSERT INTO books (name,dt) values ('Spartacus','2020-05-09')");

            //Получаем метаданные о БД
            DatabaseMetaData databaseMetaData = (DatabaseMetaData) connection.getMetaData();

            ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[]{"Table"});
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1) + " " +
                        resultSet.getString(2) + " " +
                        resultSet.getString(3) + " " +
                        resultSet.getString(4));
            }
            resultSet = statement.executeQuery("select * from books");
            //Получем метаданные о результате запроса
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            for (int i = 1; i < resultSetMetaData.getColumnCount(); i++) {
                System.out.println(resultSetMetaData.getColumnLabel(i)+" "+
                resultSetMetaData.getColumnType(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Connection close");
        }
    }
}
