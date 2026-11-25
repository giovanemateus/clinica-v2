package clinica.api;

import clinica.dao.ConsultaDAO;
import clinica.dao.IConsultaDAO;
import clinica.dao.IMedicoDAO;
import clinica.dao.IPacienteDAO;
import clinica.dao.IRegistroAtendimentoDAO;
import clinica.dao.MedicoDAO;
import clinica.dao.PacienteDAO;
import clinica.dao.RegistroAtendimentoDAO;
import clinica.model.Consulta;
import clinica.model.ConsultaBuilder;
import clinica.model.Medico;
import clinica.model.Paciente;
import clinica.service.ConsultaService;
import clinica.service.ServiceFactory;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * API REST do recurso "consultas" (respostas em JSON).
 *
 * GET    /api/consultas        -> 200 lista todas
 * GET    /api/consultas/{id}   -> 200 consulta + registro (1:1) | 404
 * POST   /api/consultas        -> 201 criada | 400 | 409 horario indisponivel
 * PUT    /api/consultas/{id}   -> 200 atualizada | 404 | 409
 * PATCH  /api/consultas/{id}   -> 200 status alterado | 400 | 404
 * DELETE /api/consultas/{id}   -> 204 sem conteudo | 404
 */
@WebServlet("/api/consultas/*")
public class ConsultaApi extends ApiBase {

    private IConsultaDAO consultaDAO = new ConsultaDAO();
    private IPacienteDAO pacienteDAO = new PacienteDAO();
    private IMedicoDAO medicoDAO = new MedicoDAO();
    private IRegistroAtendimentoDAO registroDAO = new RegistroAtendimentoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = pegarId(request);
            if (id == SEM_ID) {
                enviarJson(response, HttpServletResponse.SC_OK, consultaDAO.listarTodos());
                return;
            }
            Consulta consulta = consultaDAO.buscarPorId(id);
            if (consulta == null) {
                enviarErro(response, HttpServletResponse.SC_NOT_FOUND, "Consulta " + id + " não encontrada.");
                return;
            }
            consulta.setRegistro(registroDAO.buscarPorConsulta(id));
            enviarJson(response, HttpServletResponse.SC_OK, consulta);
        } catch (SQLException e) {
            enviarErro(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            ConsultaRequest dados = lerJson(request, ConsultaRequest.class);
            salvar(request, response, dados, 0, HttpServletResponse.SC_CREATED);
        } catch (SQLException e) {
            enviarErro(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = pegarId(request);
            if (id == SEM_ID || consultaDAO.buscarPorId(id) == null) {
                enviarErro(response, HttpServletResponse.SC_NOT_FOUND, "Consulta " + id + " não encontrada.");
                return;
            }
            ConsultaRequest dados = lerJson(request, ConsultaRequest.class);
            salvar(request, response, dados, id, HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            enviarErro(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = pegarId(request);
        Consulta consulta = consultaDAO.buscarPorId(id);
        if (consulta == null) {
            enviarErro(response, HttpServletResponse.SC_NOT_FOUND, "Consulta " + id + " não encontrada.");
            return;
        }
        StatusRequest dados = lerJson(request, StatusRequest.class);
        String problema = ServiceFactory.criarConsultaService().alterarStatus(consulta, dados == null ? null : dados.status());
        if (problema != null) {
            enviarErro(response, HttpServletResponse.SC_BAD_REQUEST, problema);
            return;
        }
        enviarJson(response, HttpServletResponse.SC_OK, consulta);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = pegarId(request);
            if (id == SEM_ID || consultaDAO.buscarPorId(id) == null) {
                enviarErro(response, HttpServletResponse.SC_NOT_FOUND, "Consulta " + id + " não encontrada.");
                return;
            }
            consultaDAO.deletar(id);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (SQLException e) {
            enviarErro(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /** Usado pelo POST (id 0) e pelo PUT (id da URL). */
    private void salvar(HttpServletRequest request, HttpServletResponse response, ConsultaRequest dados, int id, int statusSucesso)
            throws SQLException, IOException {
        if (dados == null || dados.pacienteId() == null || dados.medicoId() == null) {
            enviarErro(response, HttpServletResponse.SC_BAD_REQUEST, "Informe pacienteId e medicoId.");
            return;
        }
        Paciente paciente = pacienteDAO.buscarPorId(dados.pacienteId());
        Medico medico = medicoDAO.buscarPorId(dados.medicoId());
        if (paciente == null || medico == null) {
            enviarErro(response, HttpServletResponse.SC_BAD_REQUEST, "Paciente ou médico não encontrado.");
            return;
        }
        Consulta consulta = montarConsulta(dados, id, paciente, medico);

        ConsultaService service = ServiceFactory.criarConsultaService();
        String problema = service.salvar(consulta);
        if (problema != null) {
            Map<String, Object> erro = new LinkedHashMap<>();
            erro.put("status", HttpServletResponse.SC_CONFLICT);
            erro.put("erro", problema);
            erro.put("sugestoes", service.sugerirHorarios(consulta));
            enviarJson(response, HttpServletResponse.SC_CONFLICT, erro);
            return;
        }
        response.setHeader("Location", request.getContextPath() + "/api/consultas/" + consulta.getId());
        enviarJson(response, statusSucesso, consultaDAO.buscarPorId(consulta.getId()));
    }

    /** BUILDER montando a consulta a partir do DTO. */
    private Consulta montarConsulta(ConsultaRequest dados, int id, Paciente paciente, Medico medico) {
        return new ConsultaBuilder()
                .comId(id)
                .comPaciente(paciente)
                .comMedico(medico)
                .naData(dados.data())
                .noHorario(dados.hora())
                .comTipoAtendimento(valorOuPadrao(dados.tipoAtendimento(), "Presencial"))
                .comStatus(valorOuPadrao(dados.status(), "Agendada"))
                .comValor(dados.valor() == null ? 0 : dados.valor())
                .comFormaPagamento(dados.formaPagamento())
                .comObservacoes(dados.observacoes())
                .comExames(dados.exames())
                .constroi();
    }

    private String valorOuPadrao(String valor, String padrao) {
        return valor == null || valor.isEmpty() ? padrao : valor;
    }
}
