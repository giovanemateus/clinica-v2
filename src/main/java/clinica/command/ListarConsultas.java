package clinica.command;

import clinica.dao.ConsultaDAO;
import clinica.dao.MedicoDAO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Consultar todas as consultas.
 * Se vier o parametro medicoId, mostra so as consultas daquele medico (relacionamento 1:N).
 */
public class ListarConsultas implements ICommand {

    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ConsultaDAO dao = new ConsultaDAO();
        String medicoId = request.getParameter("medicoId");

        if (medicoId != null && !medicoId.isEmpty()) {
            int id = Integer.parseInt(medicoId);
            request.setAttribute("consultas", dao.listarPorMedico(id));
            request.setAttribute("medico", new MedicoDAO().buscarPorId(id));
        } else {
            request.setAttribute("consultas", dao.listarTodos());
        }
        return "consultas.jsp";
    }
}
