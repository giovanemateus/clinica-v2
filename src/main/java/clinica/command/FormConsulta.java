package clinica.command;

import clinica.dao.DAOFactory;
import clinica.decorator.ExameFactory;
import clinica.service.AgendaService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Abre o formulario de consulta: vazio (nova) ou preenchido (editar, com id). */
public class FormConsulta implements ICommand {

    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        if (id != null && !id.isEmpty()) {
            request.setAttribute("consulta", DAOFactory.criarConsultaDAO().buscarPorId(Integer.parseInt(id)));
        }
        carregarListas(request);
        return "consulta-form.jsp";
    }

    /** Listas usadas nos campos de selecao do formulario. */
    public static void carregarListas(HttpServletRequest request) throws Exception {
        request.setAttribute("pacientes", DAOFactory.criarPacienteDAO().listarTodos());
        request.setAttribute("medicos", DAOFactory.criarMedicoDAO().listarTodos());
        request.setAttribute("horarios", AgendaService.HORARIOS);
        request.setAttribute("exames", ExameFactory.EXAMES);
    }
}
