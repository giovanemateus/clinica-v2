package clinica.command;

import clinica.dao.PacienteDAO;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Deletar paciente. Nao deixa excluir se tiver consultas (chave estrangeira). */
public class ExcluirPaciente implements ICommand {

    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!"POST".equals(request.getMethod())) {
            throw new Exception("Exclusão permitida somente pelo botão Excluir (POST).");
        }
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            new PacienteDAO().deletar(id);
        } catch (SQLException e) {
            throw new Exception("Não é possível excluir: existem consultas cadastradas para este paciente.");
        }
        return "redirect:controller?acao=ListarPacientes&msg=excluido";
    }
}
