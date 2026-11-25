package clinica.api;

import clinica.dao.IPacienteDAO;
import clinica.dao.PacienteDAO;
import clinica.model.Paciente;
import java.io.IOException;
import java.sql.SQLException;
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

    private IPacienteDAO pacienteDAO = new PacienteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
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
        } catch (SQLException e) {
            enviarErro(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
