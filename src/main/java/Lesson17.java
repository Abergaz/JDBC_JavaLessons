import com.mysql.cj.jdbc.DatabaseMetaData;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;


public class Lesson17 {
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

            /** перед тем как использовать Batch (пакет/серию sql запросов
             * необходимо установить setAutoCommit=false */
            connection.setAutoCommit(false);

            //Запросы добавляем в Batch для выполнения одним потоком
            statement.addBatch("DROP TABLE iF EXISTS Books");
            statement.addBatch("CREATE TABLE IF NOT  EXISTS Books (id MEDIUMINT NOT NULL AUTO_INCREMENT, name VARCHAR(30) not null, dt DATE, PRIMARY KEY(id))");
            statement.addBatch("INSERT INTO books (name,dt) values ('Salamon Key','2020-05-09')");
            statement.addBatch("INSERT INTO books (name,dt) values ('Inferno','2020-05-09')");
            statement.addBatch("INSERT INTO books (name,dt) values ('Spartacus','2020-05-09')");

            //запускаем поток на выполнение, возвращает int массив  с резуьтаттами выполения запросов
            if (statement.executeBatch().length==5){
                connection.commit();//если 5 резуьтатотов то commit
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Connection close");
        }
    }
}
