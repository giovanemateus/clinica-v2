package clinica.command;

import clinica.dao.DAOFactory;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Deletar medico. Nao deixa excluir se tiver consultas (chave estrangeira). */
public class ExcluirMedico implements ICommand {

    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!"POST".equals(request.getMethod())) {
            throw new Exception("Exclusão permitida somente pelo botão Excluir (POST).");
        }
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            DAOFactory.criarMedicoDAO().deletar(id);
        } catch (SQLException e) {
            throw new Exception("Não é possível excluir: existem consultas cadastradas para este médico.");
        }
        return "redirect:controller?acao=ListarMedicos&msg=excluido";
    }
}
