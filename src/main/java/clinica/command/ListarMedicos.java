package clinica.command;

import clinica.dao.MedicoDAO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Consultar todos os medicos. */
public class ListarMedicos implements ICommand {

    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("medicos", new MedicoDAO().listarTodos());
        return "medicos.jsp";
    }
}
