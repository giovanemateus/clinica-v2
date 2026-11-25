package clinica.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * PADRAO COMMAND
 * Cada acao do sistema e uma classe que implementa esta interface.
 * O metodo executar devolve a pagina JSP que deve ser exibida
 * (ou "redirect:..." para redirecionar).
 */
public interface ICommand {

    String executar(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
