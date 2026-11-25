package clinica.command;

import clinica.dao.DAOFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Abre o formulario de medico: vazio (novo) ou preenchido (editar). */
public class FormMedico implements ICommand {

    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        if (id != null && !id.isEmpty()) {
            request.setAttribute("medico", DAOFactory.criarMedicoDAO().buscarPorId(Integer.parseInt(id)));
        }
        return "medico-form.jsp";
    }
}
