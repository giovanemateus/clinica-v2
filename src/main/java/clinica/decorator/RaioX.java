package clinica.decorator;

/** PADRAO DECORATOR - ConcreteDecorator: Raio-X (+ R$ 90.0). */
public class RaioX extends ExameDecorator {

    public RaioX(Atendimento atendimento) {
        super(atendimento);
    }

    @Override
    public String getDescricao() {
        return atendimentodecorator.getDescricao() + ", Raio-X";
    }

    @Override
    public double custo() {
        return atendimentodecorator.custo() + 90.0;
    }
}
