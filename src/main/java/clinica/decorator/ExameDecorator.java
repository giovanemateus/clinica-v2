package clinica.decorator;

/**
 * PADRAO DECORATOR - Decorator
 * Guarda uma referencia para o Atendimento que esta sendo "decorado"
 * e segue a mesma interface. Os exames estendem esta classe.
 */
public class ExameDecorator implements Atendimento {

    protected Atendimento atendimentodecorator;

    public ExameDecorator(Atendimento atendimentodecorator) {
        this.atendimentodecorator = atendimentodecorator;
    }

    @Override
    public String getDescricao() {
        return atendimentodecorator.getDescricao();
    }

    @Override
    public double custo() {
        return atendimentodecorator.custo();
    }
}
