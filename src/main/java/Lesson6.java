import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class Lesson6 {
    public static void main(String[] args) {
        String userName = "root";
        String password = "1111";
        String url ="jdbc:mysql://localhost:3306/test";


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        try(
                Connection connection = DriverManager.getConnection(url,userName,password);
                Statement statement = connection.createStatement();
            )
        {
            System.out.println("Connection open");
            //Запросы на изменение данных executeUpdate
            statement.executeUpdate("DROP TABLE  Books");
            statement.executeUpdate("CREATE TABLE IF NOT  EXISTS Books (id MEDIUMINT NOT NULL AUTO_INCREMENT, name VARCHAR(30) not null, PRIMARY KEY(id))");
            statement.executeUpdate("INSERT INTO Books (name) VALUES ('Inferno')");
            statement.executeUpdate("INSERT INTO Books (name) VALUES ('Salomon key')");

            //Для получения данных используем executeQuery
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Books");
            while(resultSet.next()){
                System.out.println(resultSet.getInt(1)+" "+resultSet.getString(2));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Connection close");
    }
}
