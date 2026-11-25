package clinica.dao;

import clinica.model.Paciente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** PADRAO DAO: toda a parte de SQL da tabela paciente fica aqui. */
public class PacienteDAO implements IPacienteDAO {

    @Override
    public void inserir(Paciente p) throws SQLException {
        String sql = "INSERT INTO paciente (nome, cpf, telefone, email) VALUES (?, ?, ?, ?)";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getCpf());
            ps.setString(3, p.getTelefone());
            ps.setString(4, p.getEmail());
            ps.executeUpdate();
        }
    }

    @Override
    public void atualizar(Paciente p) throws SQLException {
        String sql = "UPDATE paciente SET nome = ?, cpf = ?, telefone = ?, email = ? WHERE id = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getCpf());
            ps.setString(3, p.getTelefone());
            ps.setString(4, p.getEmail());
            ps.setInt(5, p.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void deletar(int id) throws SQLException {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM paciente WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Paciente buscarPorId(int id) throws SQLException {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM paciente WHERE id = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return montar(rs);
            }
            return null;
        }
    }

    @Override
    public List<Paciente> listarTodos() throws SQLException {
        List<Paciente> lista = new ArrayList<>();
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM paciente ORDER BY nome")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(montar(rs));
            }
        }
        return lista;
    }

    private Paciente montar(ResultSet rs) throws SQLException {
        Paciente p = new Paciente();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setCpf(rs.getString("cpf"));
        p.setTelefone(rs.getString("telefone"));
        p.setEmail(rs.getString("email"));
        return p;
    }
}
