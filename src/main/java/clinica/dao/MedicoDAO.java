package clinica.dao;

import clinica.model.Medico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** PADRAO DAO: toda a parte de SQL da tabela medico fica aqui. */
public class MedicoDAO implements IMedicoDAO {

    @Override
    public void inserir(Medico m) throws SQLException {
        String sql = "INSERT INTO medico (nome, crm, especialidade, telefone) VALUES (?, ?, ?, ?)";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getNome());
            ps.setString(2, m.getCrm());
            ps.setString(3, m.getEspecialidade());
            ps.setString(4, m.getTelefone());
            ps.executeUpdate();
        }
    }

    @Override
    public void atualizar(Medico m) throws SQLException {
        String sql = "UPDATE medico SET nome = ?, crm = ?, especialidade = ?, telefone = ? WHERE id = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getNome());
            ps.setString(2, m.getCrm());
            ps.setString(3, m.getEspecialidade());
            ps.setString(4, m.getTelefone());
            ps.setInt(5, m.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void deletar(int id) throws SQLException {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM medico WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Medico buscarPorId(int id) throws SQLException {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM medico WHERE id = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return montar(rs);
            }
            return null;
        }
    }

    @Override
    public List<Medico> listarTodos() throws SQLException {
        List<Medico> lista = new ArrayList<>();
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM medico ORDER BY nome")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(montar(rs));
            }
        }
        return lista;
    }

    private Medico montar(ResultSet rs) throws SQLException {
        Medico m = new Medico();
        m.setId(rs.getInt("id"));
        m.setNome(rs.getString("nome"));
        m.setCrm(rs.getString("crm"));
        m.setEspecialidade(rs.getString("especialidade"));
        m.setTelefone(rs.getString("telefone"));
        return m;
    }
}
