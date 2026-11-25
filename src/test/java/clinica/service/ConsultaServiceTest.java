package clinica.service;

import clinica.dao.IConsultaDAO;
import clinica.model.Consulta;
import clinica.model.ConsultaBuilder;
import clinica.model.Medico;
import clinica.model.Paciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Testes do ConsultaService (salvar, Decorator do valor total e PATCH de status). */
class ConsultaServiceTest {

    @Mock
    IConsultaDAO consultaDAO;

    @Mock
    AgendaService agendaService;

    ConsultaService consultaService;

    @BeforeEach
    void initializeMocks() {
        MockitoAnnotations.openMocks(this);
        consultaService = new ConsultaService(consultaDAO, agendaService);
    }

    @Test
    @DisplayName("Deve inserir a consulta nova com o valor total calculado pelos exames")
    void salvarConsultaNova() throws Exception {
        //Arrange
        Consulta consulta = consultaValida(0, "Hemograma,RaioX");
        when(agendaService.verificarDisponibilidade(consulta)).thenReturn(null);
        when(consultaDAO.inserir(consulta)).thenReturn(10);

        //Act
        String problema = consultaService.salvar(consulta);

        //Assert
        assertNull(problema);
        assertEquals(10, consulta.getId());
        assertEquals(435.0, consulta.getValorTotal()); // 300 + 45 + 90
        verify(consultaDAO, times(1)).inserir(consulta);
        verify(consultaDAO, never()).atualizar(any());
    }

    @Test
    @DisplayName("Deve atualizar quando a consulta ja tem id")
    void salvarConsultaExistente() throws Exception {
        //Arrange
        Consulta consulta = consultaValida(5, "");
        when(agendaService.verificarDisponibilidade(consulta)).thenReturn(null);

        //Act
        String problema = consultaService.salvar(consulta);

        //Assert
        assertNull(problema);
        assertEquals(300.0, consulta.getValorTotal());
        verify(consultaDAO, times(1)).atualizar(consulta);
        verify(consultaDAO, never()).inserir(any());
    }

    @Test
    @DisplayName("Nao deve salvar quando o horario esta ocupado")
    void naoSalvarHorarioOcupado() throws Exception {
        //Arrange
        Consulta consulta = consultaValida(0, "");
        when(agendaService.verificarDisponibilidade(consulta)).thenReturn("O médico já possui uma consulta neste dia e horário.");

        //Act
        String problema = consultaService.salvar(consulta);

        //Assert
        assertEquals("O médico já possui uma consulta neste dia e horário.", problema);
        verify(consultaDAO, never()).inserir(any());
    }

    @Test
    @DisplayName("Nao deve salvar sem paciente")
    void naoSalvarSemPaciente() throws Exception {
        //Arrange
        Consulta consulta = consultaValida(0, "");
        consulta.setPaciente(null);

        //Act
        String problema = consultaService.salvar(consulta);

        //Assert
        assertEquals("Preencha paciente, médico, data e horário.", problema);
        verify(agendaService, never()).verificarDisponibilidade(any());
        verify(consultaDAO, never()).inserir(any());
    }

    @Test
    @DisplayName("Consulta cancelada nao passa pela verificacao da agenda")
    void canceladaNaoVerificaAgenda() throws Exception {
        //Arrange
        Consulta consulta = consultaValida(5, "");
        consulta.setStatus("Cancelada");

        //Act
        String problema = consultaService.salvar(consulta);

        //Assert
        assertNull(problema);
        verify(agendaService, never()).verificarDisponibilidade(any());
        verify(consultaDAO, times(1)).atualizar(consulta);
    }

    @Test
    @DisplayName("Deve alterar o status com um valor valido")
    void alterarStatusValido() throws Exception {
        //Arrange
        Consulta consulta = consultaValida(5, "");

        //Act
        String problema = consultaService.alterarStatus(consulta, "Confirmada");

        //Assert
        assertNull(problema);
        assertEquals("Confirmada", consulta.getStatus());
        verify(consultaDAO, times(1)).atualizar(consulta);
    }

    @Test
    @DisplayName("Nao deve alterar o status com um valor invalido")
    void alterarStatusInvalido() throws Exception {
        //Arrange
        Consulta consulta = consultaValida(5, "");

        //Act
        String problema = consultaService.alterarStatus(consulta, "Sumiu");

        //Assert
        assertEquals("Status inválido. Use: [Agendada, Confirmada, Realizada, Cancelada]", problema);
        assertEquals("Agendada", consulta.getStatus());
        verify(consultaDAO, never()).atualizar(any());
    }

    private Consulta consultaValida(int id, String exames) {
        Paciente paciente = new Paciente();
        paciente.setId(1);
        Medico medico = new Medico();
        medico.setId(1);
        medico.setEspecialidade("Cardiologia");
        return new ConsultaBuilder()
                .comId(id)
                .comPaciente(paciente)
                .comMedico(medico)
                .naData("2026-12-01")
                .noHorario("09:00")
                .comValor(300.0)
                .comExames(exames)
                .constroi();
    }
}
