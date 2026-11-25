package clinica.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Classe base (abstrata) das APIs REST.
 * Recebe a requisicao, chama o metodo do verbo HTTP (get, post, put, patch, delete)
 * e trata os erros num lugar so. Tambem le/escreve JSON e pega o id da URL.
 * Verbo que a API nao sobrescrever responde 405.
 */
public abstract class ApiBase extends HttpServlet {

    protected static final int SEM_ID = -1;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        try {
            executarVerbo(request.getMethod(), request, response);
        } catch (NumberFormatException e) {
            enviarErro(response, HttpServletResponse.SC_BAD_REQUEST, "Id inválido na URL.");
        } catch (JsonSyntaxException e) {
            enviarErro(response, HttpServletResponse.SC_BAD_REQUEST, "JSON inválido no corpo da requisição.");
        } catch (Exception e) {
            enviarErro(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro no servidor: " + e.getMessage());
        }
    }

    private void executarVerbo(String verbo, HttpServletRequest request, HttpServletResponse response) throws Exception {
        switch (verbo) {
            case "GET": get(request, response); return;
            case "POST": post(request, response); return;
            case "PUT": put(request, response); return;
            case "PATCH": patch(request, response); return;
            case "DELETE": delete(request, response); return;
            default: naoPermitido(response);
        }
    }

    protected void get(HttpServletRequest request, HttpServletResponse response) throws Exception {
        naoPermitido(response);
    }

    protected void post(HttpServletRequest request, HttpServletResponse response) throws Exception {
        naoPermitido(response);
    }

    protected void put(HttpServletRequest request, HttpServletResponse response) throws Exception {
        naoPermitido(response);
    }

    protected void patch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        naoPermitido(response);
    }

    protected void delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
        naoPermitido(response);
    }

    private void naoPermitido(HttpServletResponse response) throws IOException {
        enviarErro(response, HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Método não permitido neste recurso.");
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

    /** 409: a regra de negocio nao deixou salvar (ex.: horario ocupado), com as sugestoes. */
    protected void enviarConflito(HttpServletResponse response, String mensagem, List<String> sugestoes)
            throws IOException {
        Map<String, Object> erro = new LinkedHashMap<>();
        erro.put("status", HttpServletResponse.SC_CONFLICT);
        erro.put("erro", mensagem);
        erro.put("sugestoes", sugestoes);
        enviarJson(response, HttpServletResponse.SC_CONFLICT, erro);
    }
}
