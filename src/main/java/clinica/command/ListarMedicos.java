package clinica.command;

import clinica.dao.DAOFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Consultar todos os medicos. */
public class ListarMedicos implements ICommand {

    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("medicos", DAOFactory.criarMedicoDAO().listarTodos());
        return "medicos.jsp";
    }
}
