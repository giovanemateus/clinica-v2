package clinica.decorator;

/**
 * PADRAO DECORATOR - ConcreteComponent
 * A consulta sem nenhum exame. O preco base e o valor informado na consulta.
 */
public class ConsultaSimples implements Atendimento {

    private String descricao;
    private double precobase;

    public ConsultaSimples(String especialidade, double precobase) {
        this.descricao = "Consulta de " + especialidade;
        this.precobase = precobase;
    }

    @Override
    public String getDescricao() {
        return descricao;
    }

    @Override
    public double custo() {
        return precobase;
    }
}
