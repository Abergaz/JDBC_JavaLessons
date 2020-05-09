import java.sql.*;


public class Lesson10 {
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
                Statement statement = connection.createStatement();
        ) {
            System.out.println("Connection open");
            //Запросы на изменение данных executeUpdate
            statement.executeUpdate("DROP TABLE iF EXISTS Books");
            statement.executeUpdate("CREATE TABLE IF NOT  EXISTS Books (id MEDIUMINT NOT NULL AUTO_INCREMENT, name VARCHAR(30) not null, dt DATE, PRIMARY KEY(id))");
            statement.executeUpdate("INSERT INTO books (name,dt) values ('Salamon Key','2020-05-09')");
            statement.executeUpdate("INSERT INTO books (name,dt) values ('Inferno','2020-05-09')");

            //класс для рабботы с хранимыми процедурами CallableStatement
            CallableStatement callableStatement = connection.prepareCall("{CALL booksCount(?)}");
            //т.к. ф-ия возвращает параметр его надо зарегистрировать
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.execute();
            //получаем возвращаемый параметр
            int returnParam = callableStatement.getInt(1);
            System.out.println("Return param = " + returnParam);//верет кол-во строк в тааблице как описано в хранимой поцедуре booksCount

            //передаем параметр в хранимую процедуру но ничего не возвращает она
            callableStatement = connection.prepareCall("{CALL getBooks(?)}");
            callableStatement.setInt(1, 1);
            boolean res = callableStatement.execute();
            //получаем результат выполнения хранимой процедуры
            if (res) {
                ResultSet resultSet = callableStatement.getResultSet();
                while (resultSet.next()) {
                    System.out.println(resultSet.getInt("id") + " " + resultSet.getString("name"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Connection close");
    }
}
