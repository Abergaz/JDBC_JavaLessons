import java.sql.*;


public class Lesson12 {
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
                 */
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        ) {
            System.out.println("Connection open");
            //Запросы на изменение данных executeUpdate
            statement.executeUpdate("DROP TABLE iF EXISTS Books");
            statement.executeUpdate("CREATE TABLE IF NOT  EXISTS Books (id MEDIUMINT NOT NULL AUTO_INCREMENT, name VARCHAR(30) not null, dt DATE, PRIMARY KEY(id))");
            statement.executeUpdate("INSERT INTO books (name,dt) values ('Salamon Key','2020-05-09')");
            statement.executeUpdate("INSERT INTO books (name,dt) values ('Inferno','2020-05-09')");
            statement.executeUpdate("INSERT INTO books (name,dt) values ('Spartacus','2020-05-09')");

            ResultSet resultSet =statement.executeQuery("Select * from books");
            //идем вперед по записям
            System.out.println("идем вперед по записям");
            if (resultSet.next()){//встали на 1 стоку
                System.out.println(resultSet.getString("name"));
            }
            if (resultSet.next()){//встали на вторую чтроку
                System.out.println(resultSet.getString("name"));
            }
            //идём обратно по записям
            System.out.println("идём обратно по записям");
            if (resultSet.previous()){ //вернулись на 1 строку
                System.out.println(resultSet.getString("name"));
            }
            //идём относительно текущей
            System.out.println("идём относительно текущей");
            if (resultSet.relative(2)){//были на 1 + 2 стали на 3
                System.out.println(resultSet.getString("name"));
            }
            if (resultSet.relative(-2)){ //3-2 вернулись на 1
                System.out.println(resultSet.getString("name"));
            }
            //указываем абсолютной номер строки
            System.out.println("указываем абсолютной номер строки");
            if (resultSet.absolute(3)){ //встали на 3
                System.out.println(resultSet.getString("name"));
            }
            //получем 1 строку
            System.out.println("получем 1 строку");
            if (resultSet.first()){ //встали на 1
                System.out.println(resultSet.getString("name"));
            }
            //получем последнюю строку
            System.out.println("получем последнюю строку");
            if (resultSet.last()){ //встали на последнюю
                System.out.println(resultSet.getString("name"));
            }
            //встаем на самое начало перед 1 строкой
            System.out.println("встаем на самое начало перед 1 строкой");
            resultSet.beforeFirst();
            //встаем на самый конец за последней строкой
            System.out.println("встаем на самый конец за последней строкой");
            resultSet.afterLast();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Connection close");
    }
}
