package clinica.command;

import clinica.dao.MedicoDAO;
import clinica.dao.PacienteDAO;
import clinica.model.Consulta;
import clinica.model.ConsultaBuilder;
import clinica.model.Medico;
import clinica.model.Paciente;
import clinica.service.ConsultaService;
import clinica.service.ServiceFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Inserir ou atualizar uma consulta.
 * A regra de salvar fica no ConsultaService (automacao da agenda + Decorator dos exames).
 * Se o horario nao estiver livre, volta para o formulario mostrando
 * o motivo e os proximos horarios disponiveis.
 */
public class SalvarConsulta implements ICommand {

    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Consulta consulta = montarConsulta(request);

        ConsultaService service = ServiceFactory.criarConsultaService();
        String problema = service.salvar(consulta);
        if (problema != null) {
            request.setAttribute("sugestoes", service.sugerirHorarios(consulta));
            return voltarComErro(request, consulta, problema);
        }
        return "redirect:controller?acao=ListarConsultas&msg=salvo";
    }

    /** Monta a consulta com o BUILDER a partir dos campos do formulario. */
    private Consulta montarConsulta(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        String valor = request.getParameter("valor");

        return new ConsultaBuilder()
                .comId(vazio(id) ? 0 : Integer.parseInt(id))
                .comPaciente(buscarPaciente(request.getParameter("pacienteId")))
                .comMedico(buscarMedico(request.getParameter("medicoId")))
                .naData(request.getParameter("data"))
                .noHorario(request.getParameter("hora"))
                .comTipoAtendimento(request.getParameter("tipoAtendimento"))
                .comStatus(request.getParameter("status"))
                .comValor(vazio(valor) ? 0 : Double.parseDouble(valor))
                .comFormaPagamento(request.getParameter("formaPagamento"))
                .comObservacoes(request.getParameter("observacoes"))
                .comExames(juntarExames(request.getParameterValues("exames")))
                .constroi();
    }

    private Paciente buscarPaciente(String pacienteId) throws Exception {
        if (vazio(pacienteId)) {
            return null;
        }
        return new PacienteDAO().buscarPorId(Integer.parseInt(pacienteId));
    }

    private Medico buscarMedico(String medicoId) throws Exception {
        if (vazio(medicoId)) {
            return null;
        }
        return new MedicoDAO().buscarPorId(Integer.parseInt(medicoId));
    }

    /** Os checkboxes chegam como lista; no banco fica "Hemograma,RaioX". */
    private String juntarExames(String[] exames) {
        if (exames == null) {
            return "";
        }
        return String.join(",", exames);
    }

    private boolean vazio(String texto) {
        return texto == null || texto.isEmpty();
    }

    private String voltarComErro(HttpServletRequest request, Consulta consulta, String mensagem) throws Exception {
        request.setAttribute("erro", mensagem);
        request.setAttribute("consulta", consulta);
        FormConsulta.carregarListas(request);
        return "consulta-form.jsp";
    }
}
