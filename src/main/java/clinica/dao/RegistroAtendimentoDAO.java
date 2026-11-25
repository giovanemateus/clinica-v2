package clinica.dao;

import clinica.model.RegistroAtendimento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** PADRAO DAO para o registro de atendimento (lado 1:1 da consulta). */
public class RegistroAtendimentoDAO implements IRegistroAtendimentoDAO {

    @Override
    public void inserir(RegistroAtendimento r) throws SQLException {
        String sql = "INSERT INTO registro_atendimento (consulta_id, diagnostico, prescricao) VALUES (?, ?, ?)";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, r.getConsultaId());
            ps.setString(2, r.getDiagnostico());
            ps.setString(3, r.getPrescricao());
            ps.executeUpdate();
        }
    }

    @Override
    public RegistroAtendimento buscarPorConsulta(int consultaId) throws SQLException {
        String sql = "SELECT * FROM registro_atendimento WHERE consulta_id = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, consultaId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                RegistroAtendimento r = new RegistroAtendimento();
                r.setId(rs.getInt("id"));
                r.setConsultaId(rs.getInt("consulta_id"));
                r.setDiagnostico(rs.getString("diagnostico"));
                r.setPrescricao(rs.getString("prescricao"));
                r.setDataRegistro(rs.getString("data_registro"));
                return r;
            }
            return null;
        }
    }
}
