import java.sql.*;


public class Lesson9 {
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

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO books (name,dt) values (?,?)");
            preparedStatement.setString(1,"Inferno new");
            preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
            preparedStatement.execute();
            System.out.println(preparedStatement);
            preparedStatement.clearParameters();
            System.out.println(preparedStatement);

            //тоже самое но через Statment и executeUpdate без использования параметров в запросе
            statement.executeUpdate("INSERT INTO books (name,dt) values ('Salamon Key','2020-05-09')");

            //тоже самое но с SQL sequence {d: }
            statement.executeUpdate("INSERT INTO books (name,dt) values ('Salamon Key 2',{d:'2020-05-09'})");

            //получаем дату из БД
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books");
            while (resultSet.next()){
                System.out.println(resultSet.getInt(1)+" "+resultSet.getString(2)+" "+resultSet.getDate(3));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Connection close");
    }
}
