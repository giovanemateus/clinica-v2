package clinica.command;

import clinica.dao.ConsultaDAO;
import clinica.dao.MedicoDAO;
import clinica.dao.PacienteDAO;
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
            request.setAttribute("consulta", new ConsultaDAO().buscarPorId(Integer.parseInt(id)));
        }
        carregarListas(request);
        return "consulta-form.jsp";
    }

    /** Listas usadas nos campos de selecao do formulario. */
    public static void carregarListas(HttpServletRequest request) throws Exception {
        request.setAttribute("pacientes", new PacienteDAO().listarTodos());
        request.setAttribute("medicos", new MedicoDAO().listarTodos());
        request.setAttribute("horarios", AgendaService.HORARIOS);
        request.setAttribute("exames", ExameFactory.EXAMES);
    }
}
