package clinica.command;

import clinica.dao.DAOFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Consultar todos os pacientes. */
public class ListarPacientes implements ICommand {

    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("pacientes", DAOFactory.criarPacienteDAO().listarTodos());
        return "pacientes.jsp";
    }
}
