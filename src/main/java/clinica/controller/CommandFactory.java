package clinica.controller;

import clinica.command.ICommand;

/**
 * PADRAO FACTORY METHOD (com Reflection)
 * Recebe o nome da acao (ex.: "ListarConsultas") e cria o objeto
 * da classe Command correspondente, sem precisar de varios if/else.
 */
public class CommandFactory {

    private static final String PACOTE = "clinica.command.";

    public static ICommand criarCommand(String acao) throws Exception {
        try {
            Class<?> classe = Class.forName(PACOTE + acao);
            return (ICommand) classe.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new Exception("Ação inválida: " + acao);
        }
    }
}
