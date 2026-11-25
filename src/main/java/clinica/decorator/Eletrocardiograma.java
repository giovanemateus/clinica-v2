package clinica.decorator;

/** PADRAO DECORATOR - ConcreteDecorator: Eletrocardiograma (+ R$ 120.0). */
public class Eletrocardiograma extends ExameDecorator {

    public Eletrocardiograma(Atendimento atendimento) {
        super(atendimento);
    }

    @Override
    public String getDescricao() {
        return atendimentodecorator.getDescricao() + ", Eletrocardiograma";
    }

    @Override
    public double custo() {
        return atendimentodecorator.custo() + 120.0;
    }
}
