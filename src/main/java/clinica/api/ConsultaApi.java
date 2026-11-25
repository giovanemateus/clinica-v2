package clinica.api;

import clinica.dao.DAOFactory;
import clinica.dao.IConsultaDAO;
import clinica.model.Consulta;
import clinica.model.Medico;
import clinica.model.Paciente;
import clinica.service.ConsultaService;
import clinica.service.ServiceFactory;
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

    private IConsultaDAO consultaDAO = DAOFactory.criarConsultaDAO();

    @Override
    protected void get(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = pegarId(request);
        if (id == SEM_ID) {
            enviarJson(response, HttpServletResponse.SC_OK, consultaDAO.listarTodos());
            return;
        }
        Consulta consulta = consultaDAO.buscarPorId(id);
        if (consulta == null) {
            naoEncontrada(response, id);
            return;
        }
        consulta.setRegistro(DAOFactory.criarRegistroAtendimentoDAO().buscarPorConsulta(id));
        enviarJson(response, HttpServletResponse.SC_OK, consulta);
    }

    @Override
    protected void post(HttpServletRequest request, HttpServletResponse response) throws Exception {
        salvar(request, response, 0, HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void put(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = pegarId(request);
        if (id == SEM_ID || consultaDAO.buscarPorId(id) == null) {
            naoEncontrada(response, id);
            return;
        }
        salvar(request, response, id, HttpServletResponse.SC_OK);
    }

    @Override
    protected void patch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = pegarId(request);
        Consulta consulta = consultaDAO.buscarPorId(id);
        if (consulta == null) {
            naoEncontrada(response, id);
            return;
        }
        StatusRequest dados = lerJson(request, StatusRequest.class);
        String status = dados == null ? null : dados.status();
        String problema = ServiceFactory.criarConsultaService().alterarStatus(consulta, status);
        if (problema != null) {
            enviarErro(response, HttpServletResponse.SC_BAD_REQUEST, problema);
            return;
        }
        enviarJson(response, HttpServletResponse.SC_OK, consulta);
    }

    @Override
    protected void delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = pegarId(request);
        if (id == SEM_ID || consultaDAO.buscarPorId(id) == null) {
            naoEncontrada(response, id);
            return;
        }
        consultaDAO.deletar(id);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    /** Usado pelo POST (id 0) e pelo PUT (id da URL). */
    private void salvar(HttpServletRequest request, HttpServletResponse response, int id, int statusSucesso)
            throws Exception {
        ConsultaRequest dados = lerJson(request, ConsultaRequest.class);
        Consulta consulta = montarConsulta(dados, id);
        if (consulta == null) {
            enviarErro(response, HttpServletResponse.SC_BAD_REQUEST, "Informe pacienteId e medicoId válidos.");
            return;
        }
        ConsultaService service = ServiceFactory.criarConsultaService();
        String problema = service.salvar(consulta);
        if (problema != null) {
            enviarConflito(response, problema, service.sugerirHorarios(consulta));
            return;
        }
        response.setHeader("Location", request.getContextPath() + "/api/consultas/" + consulta.getId());
        enviarJson(response, statusSucesso, consultaDAO.buscarPorId(consulta.getId()));
    }

    /** Devolve null se o paciente ou o medico nao existirem. */
    private Consulta montarConsulta(ConsultaRequest dados, int id) throws Exception {
        if (dados == null || dados.pacienteId() == null || dados.medicoId() == null) {
            return null;
        }
        Paciente paciente = DAOFactory.criarPacienteDAO().buscarPorId(dados.pacienteId());
        Medico medico = DAOFactory.criarMedicoDAO().buscarPorId(dados.medicoId());
        if (paciente == null || medico == null) {
            return null;
        }
        return dados.paraConsulta(id, paciente, medico);
    }

    private void naoEncontrada(HttpServletResponse response, int id) throws Exception {
        enviarErro(response, HttpServletResponse.SC_NOT_FOUND, "Consulta " + id + " não encontrada.");
    }
}
