package clinica.api;

import clinica.dao.ConsultaDAO;
import clinica.dao.IConsultaDAO;
import clinica.dao.IMedicoDAO;
import clinica.dao.MedicoDAO;
import clinica.model.Medico;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * API REST do recurso "medicos" (somente leitura).
 *
 * GET /api/medicos                 -> 200 lista
 * GET /api/medicos/{id}            -> 200 | 404
 * GET /api/medicos/{id}/consultas  -> 200 consultas do medico (relacionamento 1:N) | 404
 */
@WebServlet("/api/medicos/*")
public class MedicoApi extends ApiBase {

    private IMedicoDAO medicoDAO = new MedicoDAO();
    private IConsultaDAO consultaDAO = new ConsultaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String[] partes = partesDaUrl(request);
            if (partes.length == 0) {
                enviarJson(response, HttpServletResponse.SC_OK, medicoDAO.listarTodos());
                return;
            }
            int id = Integer.parseInt(partes[0]);
            Medico medico = medicoDAO.buscarPorId(id);
            if (medico == null) {
                enviarErro(response, HttpServletResponse.SC_NOT_FOUND, "Médico " + id + " não encontrado.");
                return;
            }
            if (partes.length > 1 && partes[1].equals("consultas")) {
                enviarJson(response, HttpServletResponse.SC_OK, consultaDAO.listarPorMedico(id));
                return;
            }
            enviarJson(response, HttpServletResponse.SC_OK, medico);
        } catch (SQLException e) {
            enviarErro(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
