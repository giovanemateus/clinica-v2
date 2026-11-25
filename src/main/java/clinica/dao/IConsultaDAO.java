package clinica.dao;

import clinica.model.Consulta;
import java.sql.SQLException;
import java.util.List;

/**
 * DIP (Inversao de Dependencia): abstracao do DAO de consulta.
 * As classes de regra de negocio (services) dependem desta interface,
 * e nao da classe concreta ConsultaDAO.
 */
public interface IConsultaDAO {

    int inserir(Consulta c) throws SQLException;

    void atualizar(Consulta c) throws SQLException;

    void deletar(int id) throws SQLException;

    Consulta buscarPorId(int id) throws SQLException;

    List<Consulta> listarTodos() throws SQLException;

    List<Consulta> listarPorMedico(int medicoId) throws SQLException;

    List<String> listarHorariosOcupados(int medicoId, String data, int idIgnorar) throws SQLException;
}
