package clinica.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Classe base (abstrata) das APIs REST.
 * Tem o que todas as APIs usam: ler/escrever JSON, pegar o id da URL
 * e tratar o verbo PATCH (o HttpServlet nao tem doPatch).
 */
public abstract class ApiBase extends HttpServlet {

    protected static final int SEM_ID = -1;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        try {
            if ("PATCH".equals(request.getMethod())) {
                doPatch(request, response);
                return;
            }
            super.service(request, response);
        } catch (NumberFormatException e) {
            enviarErro(response, HttpServletResponse.SC_BAD_REQUEST, "Id inválido na URL.");
        } catch (JsonSyntaxException e) {
            enviarErro(response, HttpServletResponse.SC_BAD_REQUEST, "JSON inválido no corpo da requisição.");
        } catch (Exception e) {
            enviarErro(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro no servidor: " + e.getMessage());
        }
    }

    /** Por padrao o PATCH nao e permitido (405). A API que aceitar sobrescreve. */
    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        enviarErro(response, HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Método PATCH não permitido aqui.");
    }

    /** "/5" -> 5 | null ou "/" -> SEM_ID */
    protected int pegarId(HttpServletRequest request) {
        String[] partes = partesDaUrl(request);
        if (partes.length == 0) {
            return SEM_ID;
        }
        return Integer.parseInt(partes[0]);
    }

    /** "/5/consultas" -> ["5", "consultas"] */
    protected String[] partesDaUrl(HttpServletRequest request) {
        String caminho = request.getPathInfo();
        if (caminho == null || caminho.equals("/")) {
            return new String[0];
        }
        return caminho.substring(1).split("/");
    }

    protected <T> T lerJson(HttpServletRequest request, Class<T> classe) throws IOException {
        return gson.fromJson(request.getReader(), classe);
    }

    protected void enviarJson(HttpServletResponse response, int status, Object objeto) throws IOException {
        response.setStatus(status);
        response.getWriter().write(gson.toJson(objeto));
    }

    protected void enviarErro(HttpServletResponse response, int status, String mensagem) throws IOException {
        Map<String, Object> erro = new LinkedHashMap<>();
        erro.put("status", status);
        erro.put("erro", mensagem);
        enviarJson(response, status, erro);
    }
}
