package clinica.command;

import clinica.dao.DAOFactory;
import clinica.dao.IPacienteDAO;
import clinica.model.Paciente;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Inserir ou atualizar paciente. */
public class SalvarPaciente implements ICommand {

    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Paciente paciente = new Paciente();
        String id = request.getParameter("id");
        paciente.setId(id == null || id.isEmpty() ? 0 : Integer.parseInt(id));
        paciente.setNome(request.getParameter("nome"));
        paciente.setCpf(request.getParameter("cpf"));
        paciente.setTelefone(request.getParameter("telefone"));
        paciente.setEmail(request.getParameter("email"));

        if (paciente.getNome() == null || paciente.getNome().trim().isEmpty()) {
            request.setAttribute("erro", "O nome é obrigatório.");
            request.setAttribute("paciente", paciente);
            return "paciente-form.jsp";
        }

        IPacienteDAO dao = DAOFactory.criarPacienteDAO();
        if (paciente.getId() == 0) {
            dao.inserir(paciente);
            return "redirect:controller?acao=ListarPacientes&msg=salvo";
        }
        dao.atualizar(paciente);
        return "redirect:controller?acao=ListarPacientes&msg=salvo";
    }
}
