package clinica.command;

import clinica.dao.DAOFactory;
import clinica.dao.IMedicoDAO;
import clinica.model.Medico;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Inserir ou atualizar medico. */
public class SalvarMedico implements ICommand {

    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Medico medico = new Medico();
        String id = request.getParameter("id");
        medico.setId(id == null || id.isEmpty() ? 0 : Integer.parseInt(id));
        medico.setNome(request.getParameter("nome"));
        medico.setCrm(request.getParameter("crm"));
        medico.setEspecialidade(request.getParameter("especialidade"));
        medico.setTelefone(request.getParameter("telefone"));

        if (medico.getNome() == null || medico.getNome().trim().isEmpty()) {
            request.setAttribute("erro", "O nome é obrigatório.");
            request.setAttribute("medico", medico);
            return "medico-form.jsp";
        }

        IMedicoDAO dao = DAOFactory.criarMedicoDAO();
        if (medico.getId() == 0) {
            dao.inserir(medico);
            return "redirect:controller?acao=ListarMedicos&msg=salvo";
        }
        dao.atualizar(medico);
        return "redirect:controller?acao=ListarMedicos&msg=salvo";
    }
}
