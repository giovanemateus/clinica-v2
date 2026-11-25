package clinica.decorator;

/**
 * PADRAO DECORATOR - Component
 * Tudo o que pode ser cobrado no atendimento (a consulta e os exames)
 * tem uma descricao e um custo.
 */
public interface Atendimento {

    String getDescricao();

    double custo();
}
