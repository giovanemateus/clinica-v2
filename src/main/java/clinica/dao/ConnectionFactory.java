package clinica.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Fabrica de conexoes com o MySQL.
 * Senha padrao vazia (MySQL do XAMPP do laboratorio). Em outra maquina
 * a senha pode ser passada com -Ddb.senha=... ou alterada aqui.
 */
public class ConnectionFactory {

    private static final String URL = "jdbc:mysql://localhost:3306/clinica_db"
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo";
    private static final String USUARIO = "root";
    private static final String SENHA = System.getProperty("db.senha", "");

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver do MySQL nao encontrado.");
        }
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
