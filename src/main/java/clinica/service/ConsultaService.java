package clinica.service;

import clinica.dao.IConsultaDAO;
import clinica.decorator.Atendimento;
import clinica.decorator.ExameFactory;
import clinica.model.Consulta;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Regras para salvar uma consulta. E usado tanto pelas telas (Command)
 * quanto pela API REST, assim a regra fica em um lugar so.
 *
 * DIP: depende de IConsultaDAO (abstracao), recebida pelo construtor.
 */
public class ConsultaService {

    public static final List<String> STATUS = Arrays.asList("Agendada", "Confirmada", "Realizada", "Cancelada");

    private IConsultaDAO consultaDAO;
    private AgendaService agendaService;

    public ConsultaService(IConsultaDAO consultaDAO, AgendaService agendaService) {
        this.consultaDAO = consultaDAO;
        this.agendaService = agendaService;
    }

    /**
     * Insere (id = 0) ou atualiza a consulta.
     * Retorna null se salvou, ou a mensagem do problema (igual ao AgendaService).
     */
    public String salvar(Consulta c) throws SQLException {
        String problema = validar(c);
        if (problema != null) {
            return problema;
        }
        calcularValorTotal(c);
        if (c.getId() == 0) {
            c.setId(consultaDAO.inserir(c));
            return null;
        }
        consultaDAO.atualizar(c);
        return null;
    }

    /** DECORATOR: valor da consulta + exames escolhidos. */
    public void calcularValorTotal(Consulta c) {
        Atendimento atendimento = ExameFactory.montar(c.getEspecialidade(), c.getValor(), c.getExames());
        c.setValorTotal(atendimento.custo());
    }

    /** Muda so o status (usado no PATCH da API). Retorna o problema ou null. */
    public String alterarStatus(Consulta c, String status) throws SQLException {
        if (!STATUS.contains(status)) {
            return "Status inválido. Use: " + STATUS;
        }
        c.setStatus(status);
        consultaDAO.atualizar(c);
        return null;
    }

    /** Proximos horarios livres do medico (quando o horario escolhido esta ocupado). */
    public List<String> sugerirHorarios(Consulta c) throws SQLException {
        if (c.getMedico() == null || c.getData() == null || c.getData().isEmpty()) {
            return new ArrayList<>();
        }
        return agendaService.sugerirHorarios(c.getMedico().getId(), c.getData(), c.getId());
    }

    private String validar(Consulta c) throws SQLException {
        if (camposObrigatoriosVazios(c)) {
            return "Preencha paciente, médico, data e horário.";
        }
        if (c.getValor() < 0) {
            return "O valor da consulta não pode ser negativo.";
        }
        if ("Cancelada".equals(c.getStatus())) {
            return null; // consulta cancelada nao ocupa horario
        }
        return agendaService.verificarDisponibilidade(c);
    }

    private boolean camposObrigatoriosVazios(Consulta c) {
        return c.getPaciente() == null || c.getMedico() == null
                || vazio(c.getData()) || vazio(c.getHora());
    }

    private boolean vazio(String texto) {
        return texto == null || texto.isEmpty();
    }
}
