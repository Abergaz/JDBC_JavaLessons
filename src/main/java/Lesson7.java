import java.sql.*;


public class Lesson7 {
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
            statement.executeUpdate("DROP TABLE  IF EXISTS Users");
            statement.executeUpdate("CREATE TABLE IF NOT  EXISTS Users (id MEDIUMINT NOT NULL AUTO_INCREMENT, name VARCHAR(30) not null, password VARCHAR(30) not null, PRIMARY KEY(id))");
            statement.executeUpdate("INSERT INTO Users (name,password) VALUES ('max','123')");
            statement.executeUpdate("INSERT INTO Users (name,password) VALUES ('serg','321')");

            //Для получения данных используем executeQuery
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Users");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3));
            }
            System.out.println("PreparedStatement защищают нас от SQL injeсtion, т.к. используются параметры а не конкатенация строк");
            String userId = "1";
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from Users where id=? and name=?");
            preparedStatement.setString(1,userId);
            preparedStatement.setString(2,"max");
            preparedStatement.execute();
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3));
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Connection close");
    }
}
