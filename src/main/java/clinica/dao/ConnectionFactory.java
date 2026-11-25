package clinica.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Fabrica de conexoes com o MySQL.
 * Senha padrao vazia (MySQL do XAMPP do laboratorio). Em outra maquina
 * a senha pode ser passada com -Ddb.senha=... ou alterada aqui.
 * Nos testes do DAO a URL e trocada para o banco em memoria H2 (-Ddb.url).
 */
public class ConnectionFactory {

    private static final String URL_MYSQL = "jdbc:mysql://localhost:3306/clinica_db"
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo";

    public static Connection getConnection() throws SQLException {
        String url = System.getProperty("db.url", URL_MYSQL);
        String usuario = System.getProperty("db.usuario", "root");
        String senha = System.getProperty("db.senha", "");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver do MySQL nao encontrado.");
        }
        return DriverManager.getConnection(url, usuario, senha);
    }
}
