package clinica.decorator;

/**
 * PADRAO FACTORY METHOD para os exames.
 * Recebe a consulta e os exames escolhidos na tela e vai "embrulhando"
 * a consulta com um Decorator para cada exame.
 */
public class ExameFactory {

    // exames que a clinica oferece (valores usados nos checkboxes da tela)
    public static final String[] EXAMES = {"Hemograma", "Eletrocardiograma", "RaioX", "Ultrassom"};

    public static Atendimento montar(String especialidade, double valorConsulta, String exames) {
        Atendimento atendimento = new ConsultaSimples(especialidade, valorConsulta);
        if (exames == null || exames.isEmpty()) {
            return atendimento;
        }
        for (String exame : exames.split(",")) {
            atendimento = adicionarExame(atendimento, exame.trim());
        }
        return atendimento;
    }

    public static Atendimento adicionarExame(Atendimento atendimento, String exame) {
        switch (exame) {
            case "Hemograma":
                return new Hemograma(atendimento);
            case "Eletrocardiograma":
                return new Eletrocardiograma(atendimento);
            case "RaioX":
                return new RaioX(atendimento);
            case "Ultrassom":
                return new Ultrassom(atendimento);
            default:
                throw new IllegalArgumentException("Exame inválido: " + exame);
        }
    }
}
