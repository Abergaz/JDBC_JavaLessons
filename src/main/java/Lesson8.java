import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.sql.*;


public class Lesson8 {
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
            statement.executeUpdate("CREATE TABLE IF NOT  EXISTS Books (id MEDIUMINT NOT NULL AUTO_INCREMENT, name VARCHAR(30) not null, img BLOB, PRIMARY KEY(id))");

            //читаем картинку и записываем ее в BLOB
            BufferedImage bufferedImage = ImageIO.read(new File("smile.jpg"));
            Blob blob = connection.createBlob();
            try (OutputStream outputStream = blob.setBinaryStream(1)){
                ImageIO.write(bufferedImage,"jpg",outputStream);
            }

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Books (name,img) VALUES (?,?)");
            preparedStatement.setString(1,"inferno img");
            preparedStatement.setBlob(2,blob);
            //execute используется если не нужно возвращать результуты
            preparedStatement.execute();

            //Читаем картинку из БД
            ResultSet resultSet = statement.executeQuery("SELECT * from books");
            while (resultSet.next()){
                blob = resultSet.getBlob("img");
                bufferedImage = ImageIO.read(blob.getBinaryStream());
                File outputFile = new File("saved.png");
                ImageIO.write(bufferedImage,"jpg",outputFile);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Connection close");
    }
}
