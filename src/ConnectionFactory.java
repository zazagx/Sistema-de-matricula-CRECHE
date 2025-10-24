import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String URL = "jdbc:mysql://localhost:3306/Creche";
    private static final String USER = "root";
    private static final String PASSWORD = "isaac1662006";

    public Connection recuperarConexao() {
        try {
            return DriverManager.getConnection(URL, USER,PASSWORD);

        } catch (SQLException e) {
           throw new RuntimeException("Erro ao conectar com o bando de dados", e);
        }
    }
}
