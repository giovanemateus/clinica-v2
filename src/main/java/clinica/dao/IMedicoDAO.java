package clinica.dao;

import clinica.model.Medico;
import java.sql.SQLException;
import java.util.List;

/** DIP: abstracao do DAO de medico. */
public interface IMedicoDAO {

    void inserir(Medico m) throws SQLException;

    void atualizar(Medico m) throws SQLException;

    void deletar(int id) throws SQLException;

    Medico buscarPorId(int id) throws SQLException;

    List<Medico> listarTodos() throws SQLException;
}
