package clinica.dao;

import clinica.model.Paciente;
import java.sql.SQLException;
import java.util.List;

/** DIP: abstracao do DAO de paciente. */
public interface IPacienteDAO {

    void inserir(Paciente p) throws SQLException;

    void atualizar(Paciente p) throws SQLException;

    void deletar(int id) throws SQLException;

    Paciente buscarPorId(int id) throws SQLException;

    List<Paciente> listarTodos() throws SQLException;
}
