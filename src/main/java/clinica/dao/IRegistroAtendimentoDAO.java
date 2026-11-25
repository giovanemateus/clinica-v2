package clinica.dao;

import clinica.model.RegistroAtendimento;
import java.sql.SQLException;

/** DIP: abstracao do DAO do registro de atendimento (lado 1:1 da consulta). */
public interface IRegistroAtendimentoDAO {

    void inserir(RegistroAtendimento r) throws SQLException;

    RegistroAtendimento buscarPorConsulta(int consultaId) throws SQLException;
}
