package clinica.command;

import clinica.dao.DAOFactory;
import clinica.decorator.ExameFactory;
import clinica.model.Consulta;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Consultar por id: mostra os dados da consulta e o registro de atendimento (1:1). */
public class VerConsulta implements ICommand {

    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        Consulta consulta = DAOFactory.criarConsultaDAO().buscarPorId(id);
        if (consulta == null) {
            throw new Exception("Consulta não encontrada.");
        }
        consulta.setRegistro(DAOFactory.criarRegistroAtendimentoDAO().buscarPorConsulta(id));
        request.setAttribute("consulta", consulta);
        // DECORATOR: monta a descricao "Consulta + exames" para mostrar na tela
        request.setAttribute("atendimento",
                ExameFactory.montar(consulta.getEspecialidade(), consulta.getValor(), consulta.getExames()));
        return "consulta-ver.jsp";
    }
}
