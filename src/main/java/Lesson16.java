import com.mysql.cj.jdbc.DatabaseMetaData;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;


public class Lesson16 {
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

            //перед началом транзации необходимо установить авто коммит в false
            connection.setAutoCommit(false);

            //Запросы на изменение данных executeUpdate
            statement.executeUpdate("DROP TABLE iF EXISTS Books");
            statement.executeUpdate("CREATE TABLE IF NOT  EXISTS Books (id MEDIUMINT NOT NULL AUTO_INCREMENT, name VARCHAR(30) not null, dt DATE, PRIMARY KEY(id))");
            statement.executeUpdate("INSERT INTO books (name,dt) values ('Salamon Key','2020-05-09')");
            //создаем точку сохранения
            Savepoint savepoint = connection.setSavepoint();
            statement.executeUpdate("INSERT INTO books (name,dt) values ('Inferno','2020-05-09')");
            statement.executeUpdate("INSERT INTO books (name,dt) values ('Spartacus','2020-05-09')");

            //после оканчания закоммитить измегения или откатиить обратно
            connection.commit();
            //connection.rollback(); //Операции изменения или создания таблиц не откатываются, только операц ии ихменения данных
            //connection.rollback(savepoint); //возврат на точку останова, посе надо вызвать commit
            //connection.releaseSavepoint(savepoint);//удаляем точку востановления.




        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Connection close");
        }
    }
}
