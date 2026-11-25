package clinica.service;

import clinica.dao.IConsultaDAO;
import clinica.model.Consulta;
import clinica.model.ConsultaBuilder;
import clinica.model.Medico;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testes da automacao da agenda.
 * O banco e simulado com o Mockito (o DAO "mockado" entra no lugar do ConsultaDAO).
 */
class AgendaServiceTest {

    @Mock
    IConsultaDAO consultaDAO;

    @InjectMocks
    AgendaService agendaService;

    @BeforeEach
    void initializeMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve liberar o horario quando o medico esta livre")
    void horarioLivre() throws Exception {
        //Arrange
        Consulta consulta = novaConsulta(proximoDiaUtil(), "09:00");
        when(consultaDAO.listarHorariosOcupados(1, consulta.getData(), 0)).thenReturn(new ArrayList<>());

        //Act
        String problema = agendaService.verificarDisponibilidade(consulta);

        //Assert
        assertNull(problema);
        verify(consultaDAO).listarHorariosOcupados(1, consulta.getData(), 0);
    }

    @Test
    @DisplayName("Deve recusar quando o medico ja tem consulta no mesmo horario")
    void horarioOcupado() throws Exception {
        //Arrange
        Consulta consulta = novaConsulta(proximoDiaUtil(), "08:00");
        when(consultaDAO.listarHorariosOcupados(1, consulta.getData(), 0)).thenReturn(Arrays.asList("08:00", "08:30"));

        //Act
        String problema = agendaService.verificarDisponibilidade(consulta);

        //Assert
        assertEquals("O médico já possui uma consulta neste dia e horário.", problema);
    }

    @Test
    @DisplayName("Deve recusar agendamento no fim de semana sem consultar o banco")
    void fimDeSemana() throws Exception {
        //Arrange
        Consulta consulta = novaConsulta(proximo(DayOfWeek.SATURDAY), "09:00");

        //Act
        String problema = agendaService.verificarDisponibilidade(consulta);

        //Assert
        assertEquals("A clínica não atende aos sábados e domingos.", problema);
        verify(consultaDAO, never()).listarHorariosOcupados(anyInt(), anyString(), anyInt());
    }

    @Test
    @DisplayName("Deve recusar agendamento em data que ja passou")
    void dataNoPassado() throws Exception {
        //Arrange
        Consulta consulta = novaConsulta(LocalDate.now().minusDays(7), "09:00");

        //Act
        String problema = agendaService.verificarDisponibilidade(consulta);

        //Assert
        assertEquals("Não é possível agendar em uma data/horário que já passou.", problema);
    }

    @Test
    @DisplayName("Deve sugerir 5 horarios livres sem repetir os ocupados")
    void sugerirHorarios() throws Exception {
        //Arrange
        LocalDate dia = proximoDiaUtil();
        when(consultaDAO.listarHorariosOcupados(anyInt(), anyString(), anyInt())).thenReturn(Arrays.asList("08:00", "08:30"));

        //Act
        List<String> sugestoes = agendaService.sugerirHorarios(1, dia.toString(), 0);

        //Assert
        assertEquals(5, sugestoes.size());
        assertFalse(sugestoes.stream().anyMatch(s -> s.endsWith("08:00") || s.endsWith("08:30")));
    }

    @Test
    @DisplayName("Nao deve sugerir horario quando a agenda do medico esta toda ocupada")
    void sugerirHorariosAgendaLotada() throws Exception {
        //Arrange
        LocalDate dia = proximoDiaUtil();
        when(consultaDAO.listarHorariosOcupados(anyInt(), anyString(), anyInt())).thenReturn(Arrays.asList(AgendaService.HORARIOS));

        //Act
        List<String> sugestoes = agendaService.sugerirHorarios(1, dia.toString(), 0);

        //Assert
        assertTrue(sugestoes.isEmpty());
    }

    // ----------------- auxiliares -----------------

    private Consulta novaConsulta(LocalDate dia, String hora) {
        Medico medico = new Medico();
        medico.setId(1);
        return new ConsultaBuilder().comMedico(medico).naData(dia.toString()).noHorario(hora).constroi();
    }

    private LocalDate proximoDiaUtil() {
        LocalDate dia = LocalDate.now().plusDays(1);
        while (dia.getDayOfWeek() == DayOfWeek.SATURDAY || dia.getDayOfWeek() == DayOfWeek.SUNDAY) {
            dia = dia.plusDays(1);
        }
        return dia;
    }

    private LocalDate proximo(DayOfWeek diaDaSemana) {
        LocalDate dia = LocalDate.now().plusDays(1);
        while (dia.getDayOfWeek() != diaDaSemana) {
            dia = dia.plusDays(1);
        }
        return dia;
    }
}
