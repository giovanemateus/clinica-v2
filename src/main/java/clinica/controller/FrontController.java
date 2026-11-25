package clinica.controller;

import clinica.command.ICommand;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CONTROLLER do MVC (Front Controller).
 * Todas as telas chamam: controller?acao=NomeDaAcao
 * 1) a CommandFactory cria o Command da acao
 * 2) o Command executa (usando os DAOs = Model)
 * 3) o controller mostra a JSP (View) devolvida pelo Command
 */
@WebServlet("/controller")
public class FrontController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String acao = request.getParameter("acao");
        if (acao == null || acao.isEmpty()) {
            acao = "ListarConsultas";
        }

        try {
            ICommand command = CommandFactory.criarCommand(acao);
            String pagina = command.executar(request, response);

            if (pagina.startsWith("redirect:")) {
                response.sendRedirect(pagina.substring("redirect:".length()));
            } else {
                request.getRequestDispatcher("/WEB-INF/views/" + pagina).forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("erro", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(request, response);
        }
    }
}
