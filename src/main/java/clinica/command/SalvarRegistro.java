package clinica.command;

import clinica.dao.DAOFactory;
import clinica.dao.IRegistroAtendimentoDAO;
import clinica.model.RegistroAtendimento;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Salva o registro de atendimento de uma consulta (relacionamento 1:1). */
public class SalvarRegistro implements ICommand {

    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int consultaId = Integer.parseInt(request.getParameter("consultaId"));
        IRegistroAtendimentoDAO dao = DAOFactory.criarRegistroAtendimentoDAO();

        if (dao.buscarPorConsulta(consultaId) != null) {
            throw new Exception("Esta consulta já possui um registro de atendimento.");
        }

        RegistroAtendimento registro = new RegistroAtendimento();
        registro.setConsultaId(consultaId);
        registro.setDiagnostico(request.getParameter("diagnostico"));
        registro.setPrescricao(request.getParameter("prescricao"));
        dao.inserir(registro);

        return "redirect:controller?acao=VerConsulta&id=" + consultaId;
    }
}
