package clinica.dao;

import clinica.model.Consulta;
import clinica.model.ConsultaBuilder;
import clinica.model.Medico;
import clinica.model.Paciente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** PADRAO DAO: toda a parte de SQL da tabela consulta fica aqui. */
public class ConsultaDAO implements IConsultaDAO {

    // SELECT com JOIN para trazer junto o nome do paciente e do medico
    private static final String SELECT =
            "SELECT c.*, p.nome AS paciente_nome, m.nome AS medico_nome "
            + "FROM consulta c "
            + "JOIN paciente p ON p.id = c.paciente_id "
            + "JOIN medico m ON m.id = c.medico_id ";

    /** Insere e devolve o id gerado pelo banco (usado na resposta da API). */
    @Override
    public int inserir(Consulta c) throws SQLException {
        String sql = "INSERT INTO consulta (paciente_id, medico_id, data, hora, especialidade, tipo_atendimento, "
                + "status, valor, forma_pagamento, observacoes, exames, valor_total) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencher(ps, c);
            ps.executeUpdate();
            ResultSet chave = ps.getGeneratedKeys();
            chave.next();
            return chave.getInt(1);
        }
    }

    @Override
    public void atualizar(Consulta c) throws SQLException {
        String sql = "UPDATE consulta SET paciente_id = ?, medico_id = ?, data = ?, hora = ?, especialidade = ?, "
                + "tipo_atendimento = ?, status = ?, valor = ?, forma_pagamento = ?, observacoes = ?, "
                + "exames = ?, valor_total = ? WHERE id = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            preencher(ps, c);
            ps.setInt(13, c.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void deletar(int id) throws SQLException {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM consulta WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Consulta buscarPorId(int id) throws SQLException {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT + "WHERE c.id = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return montar(rs);
            }
            return null;
        }
    }

    @Override
    public List<Consulta> listarTodos() throws SQLException {
        List<Consulta> lista = new ArrayList<>();
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT + "ORDER BY c.data, c.hora")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(montar(rs));
            }
        }
        return lista;
    }

    /** Relacionamento 1:N -> todas as consultas de um medico. */
    @Override
    public List<Consulta> listarPorMedico(int medicoId) throws SQLException {
        List<Consulta> lista = new ArrayList<>();
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT + "WHERE c.medico_id = ? ORDER BY c.data, c.hora")) {
            ps.setInt(1, medicoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(montar(rs));
            }
        }
        return lista;
    }

    /**
     * Usado na automacao: devolve os horarios ja ocupados do medico em um dia.
     * Consultas canceladas nao ocupam horario. idIgnorar serve para a edicao
     * (a propria consulta nao conta como conflito).
     */
    @Override
    public List<String> listarHorariosOcupados(int medicoId, String data, int idIgnorar) throws SQLException {
        List<String> horarios = new ArrayList<>();
        String sql = "SELECT hora FROM consulta "
                + "WHERE medico_id = ? AND data = ? AND status <> 'Cancelada' AND id <> ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, medicoId);
            ps.setString(2, data);
            ps.setInt(3, idIgnorar);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                horarios.add(rs.getString("hora").substring(0, 5)); // "08:00:00" -> "08:00"
            }
        }
        return horarios;
    }

    private void preencher(PreparedStatement ps, Consulta c) throws SQLException {
        ps.setInt(1, c.getPaciente().getId());
        ps.setInt(2, c.getMedico().getId());
        ps.setString(3, c.getData());
        ps.setString(4, c.getHora());
        ps.setString(5, c.getEspecialidade());
        ps.setString(6, c.getTipoAtendimento());
        ps.setString(7, c.getStatus());
        ps.setDouble(8, c.getValor());
        ps.setString(9, c.getFormaPagamento());
        ps.setString(10, c.getObservacoes());
        ps.setString(11, c.getExames());
        ps.setDouble(12, c.getValorTotal());
    }

    /** Monta a Consulta usando o Builder. */
    private Consulta montar(ResultSet rs) throws SQLException {
        Paciente paciente = new Paciente();
        paciente.setId(rs.getInt("paciente_id"));
        paciente.setNome(rs.getString("paciente_nome"));

        Medico medico = new Medico();
        medico.setId(rs.getInt("medico_id"));
        medico.setNome(rs.getString("medico_nome"));

        return new ConsultaBuilder()
                .comId(rs.getInt("id"))
                .comPaciente(paciente)
                .comMedico(medico)
                .naData(rs.getString("data"))
                .noHorario(rs.getString("hora").substring(0, 5))
                .comEspecialidade(rs.getString("especialidade"))
                .comTipoAtendimento(rs.getString("tipo_atendimento"))
                .comStatus(rs.getString("status"))
                .comValor(rs.getDouble("valor"))
                .comFormaPagamento(rs.getString("forma_pagamento"))
                .comObservacoes(rs.getString("observacoes"))
                .criadaEm(rs.getString("data_criacao"))
                .comExames(rs.getString("exames"))
                .comValorTotal(rs.getDouble("valor_total"))
                .constroi();
    }
}
