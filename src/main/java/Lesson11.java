import java.sql.*;


public class Lesson11 {
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

            //для вызова хранимых процедур используем CallableStatement и prepareCall
            CallableStatement callableStatement = connection.prepareCall("{CALL getCount()}");
            boolean hasResult = callableStatement.execute();
            //используем цикл для перебора результатов
            while (hasResult){
                ResultSet resultSet = callableStatement.getResultSet();
                while (resultSet.next()){
                    System.out.println("count: "+resultSet.getInt(1));
                }
                //переходим к слудущему реущьтату из множества результатов работы хоанимой процедуры
                hasResult=callableStatement.getMoreResults();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Connection close");
    }
}
