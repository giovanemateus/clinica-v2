package clinica.decorator;

/** PADRAO DECORATOR - ConcreteDecorator: Ultrassom (+ R$ 150.0). */
public class Ultrassom extends ExameDecorator {

    public Ultrassom(Atendimento atendimento) {
        super(atendimento);
    }

    @Override
    public String getDescricao() {
        return atendimentodecorator.getDescricao() + ", Ultrassom";
    }

    @Override
    public double custo() {
        return atendimentodecorator.custo() + 150.0;
    }
}
