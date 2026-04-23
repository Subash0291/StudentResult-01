import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        String host = System.getenv("MYSQLHOST") != null ? System.getenv("MYSQLHOST") : "localhost";
        String port = System.getenv("MYSQLPORT") != null ? System.getenv("MYSQLPORT") : "3306";
        String db   = System.getenv("MYSQLDATABASE") != null ? System.getenv("MYSQLDATABASE") : "student_result";
        String user = System.getenv("MYSQLUSER") != null ? System.getenv("MYSQLUSER") : "root";
        String pass = System.getenv("MYSQLPASSWORD") != null ? System.getenv("MYSQLPASSWORD") : "awdr1357908642@A";

        String url = "jdbc:mysql://" + host + ":" + port + "/" + db + "?useSSL=false&allowPublicKeyRetrieval=true";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(url, user, pass);
    }
}
