package clinica.dao;

import clinica.model.Consulta;
import clinica.model.ConsultaBuilder;
import clinica.model.Medico;
import clinica.model.Paciente;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Testes do ConsultaDAO usando o banco H2 em memoria (modo MySQL),
 * assim o teste nao mexe no banco clinica_db de verdade.
 */
class ConsultaDAOTest {

    IConsultaDAO consultaDAO = new ConsultaDAO();

    @BeforeAll
    static void usarBancoEmMemoria() {
        System.setProperty("db.url", "jdbc:h2:mem:clinica_teste;MODE=MySQL;DB_CLOSE_DELAY=-1");
        System.setProperty("db.usuario", "sa");
    }

    @AfterAll
    static void voltarBancoPadrao() {
        System.clearProperty("db.url");
        System.clearProperty("db.usuario");
    }

    @BeforeEach
    void criarTabelas() throws Exception {
        try (InputStream arquivo = getClass().getResourceAsStream("/banco-teste.sql");
             Connection con = ConnectionFactory.getConnection();
             Statement st = con.createStatement()) {
            String script = new String(arquivo.readAllBytes(), StandardCharsets.UTF_8);
            for (String comando : script.split(";")) {
                if (!comando.isBlank()) {
                    st.execute(comando);
                }
            }
        }
    }

    @Test
    @DisplayName("Deve inserir a consulta e encontrar pelo id gerado")
    void inserirEBuscarPorId() throws Exception {
        //Arrange
        Consulta consulta = novaConsulta(1, "2026-12-01", "09:00", "Agendada");

        //Act
        int id = consultaDAO.inserir(consulta);
        Consulta encontrada = consultaDAO.buscarPorId(id);

        //Assert
        assertNotNull(encontrada);
        assertEquals("09:00", encontrada.getHora());
        assertEquals("Hemograma", encontrada.getExames());
        assertEquals(345.0, encontrada.getValorTotal());
    }

    @Test
    @DisplayName("Deve devolver null quando o id nao existe")
    void buscarIdInexistente() throws Exception {
        //Arrange
        int idInexistente = 999;

        //Act
        Consulta encontrada = consultaDAO.buscarPorId(idInexistente);

        //Assert
        assertNull(encontrada);
    }

    @Test
    @DisplayName("Horarios ocupados nao devem contar consulta cancelada")
    void horariosOcupadosSemCancelada() throws Exception {
        //Arrange
        consultaDAO.inserir(novaConsulta(1, "2026-12-01", "08:00", "Agendada"));
        consultaDAO.inserir(novaConsulta(1, "2026-12-01", "08:30", "Cancelada"));

        //Act
        List<String> ocupados = consultaDAO.listarHorariosOcupados(1, "2026-12-01", 0);

        //Assert
        assertEquals(List.of("08:00"), ocupados);
    }

    @Test
    @DisplayName("Relacionamento 1:N: deve listar so as consultas do medico")
    void listarPorMedico() throws Exception {
        //Arrange
        consultaDAO.inserir(novaConsulta(1, "2026-12-01", "08:00", "Agendada"));
        consultaDAO.inserir(novaConsulta(1, "2026-12-02", "08:00", "Agendada"));
        consultaDAO.inserir(novaConsulta(2, "2026-12-01", "08:00", "Agendada"));

        //Act
        List<Consulta> consultasMedicoUm = consultaDAO.listarPorMedico(1);

        //Assert
        assertEquals(2, consultasMedicoUm.size());
    }

    private Consulta novaConsulta(int medicoId, String data, String hora, String status) {
        Paciente paciente = new Paciente();
        paciente.setId(1);
        Medico medico = new Medico();
        medico.setId(medicoId);
        medico.setEspecialidade("Cardiologia");
        return new ConsultaBuilder()
                .comPaciente(paciente)
                .comMedico(medico)
                .naData(data)
                .noHorario(hora)
                .comStatus(status)
                .comValor(300.0)
                .comExames("Hemograma")
                .comValorTotal(345.0)
                .constroi();
    }
}
