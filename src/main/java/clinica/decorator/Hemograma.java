package clinica.decorator;

/** PADRAO DECORATOR - ConcreteDecorator: Hemograma (+ R$ 45.0). */
public class Hemograma extends ExameDecorator {

    public Hemograma(Atendimento atendimento) {
        super(atendimento);
    }

    @Override
    public String getDescricao() {
        return atendimentodecorator.getDescricao() + ", Hemograma";
    }

    @Override
    public double custo() {
        return atendimentodecorator.custo() + 45.0;
    }
}
