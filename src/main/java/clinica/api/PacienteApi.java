package clinica.api;

import clinica.dao.DAOFactory;
import clinica.dao.IPacienteDAO;
import clinica.model.Paciente;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * API REST do recurso "pacientes" (somente leitura).
 *
 * GET /api/pacientes       -> 200 lista
 * GET /api/pacientes/{id}  -> 200 | 404
 */
@WebServlet("/api/pacientes/*")
public class PacienteApi extends ApiBase {

    private IPacienteDAO pacienteDAO = DAOFactory.criarPacienteDAO();

    @Override
    protected void get(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = pegarId(request);
        if (id == SEM_ID) {
            enviarJson(response, HttpServletResponse.SC_OK, pacienteDAO.listarTodos());
            return;
        }
        Paciente paciente = pacienteDAO.buscarPorId(id);
        if (paciente == null) {
            enviarErro(response, HttpServletResponse.SC_NOT_FOUND, "Paciente " + id + " não encontrado.");
            return;
        }
        enviarJson(response, HttpServletResponse.SC_OK, paciente);
    }
}
