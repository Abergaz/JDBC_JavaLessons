import java.sql.Connection;
import java.sql.DriverManager;


public class Lesson5 {
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
            )
        {
            System.out.println("Connection open");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
