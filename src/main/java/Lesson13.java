import java.sql.*;


public class Lesson13 {
    public static void main(String[] args) {
        String userName = "root";
        String password = "1111";
        String url = "jdbc:mysql://localhost:3306/test";


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
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1) + " " + resultSet.getString(2) + " " + resultSet.getDate(3));
            }
            System.out.println("--------------------------");
            //встаем на  последнюю строку
            resultSet.last();
            //обновляем данные строки таблицы
            resultSet.updateString("name", "Spartacus 2");
            //применяем обновления
            resultSet.updateRow();

            //добавление записи
            resultSet.moveToInsertRow(); //встаем на новую запись
            resultSet.updateString("name", "Insert new row");
            resultSet.updateDate("dt", new Date(System.currentTimeMillis()));
            resultSet.insertRow(); //добавляем её

            //Удаляем 2 строку
            resultSet.absolute(2);
            resultSet.deleteRow();


            resultSet.beforeFirst();
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1) + " " + resultSet.getString(2) + " " + resultSet.getDate(3));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Connection close");
    }
}
