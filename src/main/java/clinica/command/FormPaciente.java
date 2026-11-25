package clinica.command;

import clinica.dao.PacienteDAO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Abre o formulario de paciente: vazio (novo) ou preenchido (editar). */
public class FormPaciente implements ICommand {

    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        if (id != null && !id.isEmpty()) {
            request.setAttribute("paciente", new PacienteDAO().buscarPorId(Integer.parseInt(id)));
        }
        return "paciente-form.jsp";
    }
}
