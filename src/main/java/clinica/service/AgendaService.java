package clinica.service;

import clinica.dao.IConsultaDAO;
import clinica.model.Consulta;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * AUTOMACAO DO PROCESSO DE NEGOCIO:
 * verificacao de disponibilidade da agenda do medico e sugestao de horarios livres.
 *
 * V2 (DIP): o service nao cria mais o ConsultaDAO. Ele recebe a abstracao
 * IConsultaDAO pelo construtor (injecao de dependencia). Assim nos testes
 * unitarios da para passar um DAO "falso" (Mockito) no lugar do banco.
 */
public class AgendaService {

    // Horarios de atendimento da clinica (consultas de 30 minutos)
    public static final String[] HORARIOS = {
        "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
        "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30"
    };

    private IConsultaDAO consultaDAO;

    public AgendaService(IConsultaDAO consultaDAO) {
        this.consultaDAO = consultaDAO;
    }

    /**
     * Verifica se a consulta pode ser agendada.
     * Retorna null se estiver tudo certo, ou a mensagem com o problema.
     */
    public String verificarDisponibilidade(Consulta c) throws SQLException {
        LocalDate dia = LocalDate.parse(c.getData());
        LocalTime hora = LocalTime.parse(c.getHora());

        if (c.getId() == 0 && LocalDateTime.of(dia, hora).isBefore(LocalDateTime.now())) {
            return "Não é possível agendar em uma data/horário que já passou.";
        }
        if (ehFimDeSemana(dia)) {
            return "A clínica não atende aos sábados e domingos.";
        }
        List<String> ocupados = consultaDAO.listarHorariosOcupados(c.getMedico().getId(), c.getData(), c.getId());
        if (ocupados.contains(c.getHora())) {
            return "O médico já possui uma consulta neste dia e horário.";
        }
        return null; // horario livre
    }

    /** Procura os proximos 5 horarios livres do medico a partir da data informada. */
    public List<String> sugerirHorarios(int medicoId, String data, int idConsulta) throws SQLException {
        List<String> sugestoes = new ArrayList<>();
        LocalDate dia = LocalDate.parse(data);
        if (dia.isBefore(LocalDate.now())) {
            dia = LocalDate.now();
        }
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        int diasPesquisados = 0;
        while (sugestoes.size() < 5 && diasPesquisados < 30) {
            if (!ehFimDeSemana(dia)) {
                List<String> ocupados = consultaDAO.listarHorariosOcupados(medicoId, dia.toString(), idConsulta);
                for (String horario : HORARIOS) {
                    boolean livre = !ocupados.contains(horario);
                    boolean futuro = LocalDateTime.of(dia, LocalTime.parse(horario)).isAfter(LocalDateTime.now());
                    if (livre && futuro && sugestoes.size() < 5) {
                        sugestoes.add(dia.format(formato) + " às " + horario);
                    }
                }
            }
            dia = dia.plusDays(1);
            diasPesquisados++;
        }
        return sugestoes;
    }

    private boolean ehFimDeSemana(LocalDate dia) {
        return dia.getDayOfWeek() == DayOfWeek.SATURDAY || dia.getDayOfWeek() == DayOfWeek.SUNDAY;
    }
}
