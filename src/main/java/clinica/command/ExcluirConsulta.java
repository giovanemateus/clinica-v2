package clinica.command;

import clinica.dao.DAOFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Deletar consulta (o registro de atendimento e apagado junto pelo banco). */
public class ExcluirConsulta implements ICommand {

    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!"POST".equals(request.getMethod())) {
            throw new Exception("Exclusão permitida somente pelo botão Excluir (POST).");
        }
        int id = Integer.parseInt(request.getParameter("id"));
        DAOFactory.criarConsultaDAO().deletar(id);
        return "redirect:controller?acao=ListarConsultas&msg=excluido";
    }
}
