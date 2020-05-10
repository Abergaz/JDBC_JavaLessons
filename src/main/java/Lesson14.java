import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;


public class Lesson14 {
    private static String userName = "root";
    private static String password = "1111";
    private static String url = "jdbc:mysql://localhost:3306/test";


    public static void main(String[] args) {
        ResultSet resultSet = getData();
        try {
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1) + " " + resultSet.getString(2) + " " + resultSet.getDate(3));
            }

            //далее преобразуем rowSet опять в CachedRowSet и можно проводить все операции как и с ResultSet (чтение, перемещение, изменение, добавление)
            CachedRowSet cachedRowSet = (CachedRowSet) resultSet;
            //обязательно передать параметры соединения
            cachedRowSet.setUrl(url);
            cachedRowSet.setUsername(userName);
            cachedRowSet.setPassword(password);

            //можно также делать различные выборки из RowSet, без чтения БД   ???? или всетаки с чтением
            cachedRowSet.setCommand("Select * from books where id=?");
            cachedRowSet.setInt(1, 3);
            //Можно создавать страницы
            cachedRowSet.setPageSize(1);//по 1 строке на странице
            cachedRowSet.execute();
            //обход страниц
            int page = 1;
            do {
                //выводим данные с 1 страницы
                System.out.println("Страница: " + page);
                while (cachedRowSet.next()) {
                    System.out.println(resultSet.getInt(1) + " " + resultSet.getString(2) + " " + resultSet.getDate(3));
                }
                page++;
            } while (cachedRowSet.nextPage());//переходим на другую страницу

            //Модифицируем данные

            cachedRowSet.setTableName("Books");
            cachedRowSet.absolute(1);
            cachedRowSet.deleteRow(); //удалили 1 строку
            cachedRowSet.beforeFirst();
            while (cachedRowSet.next()) {
                System.out.println(resultSet.getInt(1) + " " + resultSet.getString(2) + " " + resultSet.getDate(3));
            }
            //Чтобы изменения передались в БД, параметром можно передать  DriverManager.getConnection(), тогда выше не надо устанавливать URL Login Password
            cachedRowSet.acceptChanges();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    static ResultSet getData() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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

            ResultSet resultSet = statement.executeQuery("select * from books");
            //для получения RowSet, он недержит постоянное соединение с БД в отличиии от ResultSet
            RowSetFactory rowSetFactory = RowSetProvider.newFactory();
            CachedRowSet cachedRowSet = rowSetFactory.createCachedRowSet();
            cachedRowSet.populate(resultSet);
            return cachedRowSet;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Connection close");
        }
        return null;
    }
}
