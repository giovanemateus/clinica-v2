package clinica.model;

/**
 * PADRAO BUILDER
 * A Consulta tem 14 atributos. Em vez de um construtor enorme,
 * o Builder monta o objeto aos poucos (interface fluente) e no final
 * o metodo constroi() devolve a Consulta pronta.
 *
 * Exemplo:
 *   Consulta c = new ConsultaBuilder()
 *                   .comPaciente(p)
 *                   .comMedico(m)
 *                   .naData("2026-09-30")
 *                   .noHorario("09:00")
 *                   .constroi();
 */
public class ConsultaBuilder {

    private Consulta consulta;

    public ConsultaBuilder() {
        consulta = new Consulta();
        consulta.setStatus("Agendada");        // valor padrao
        consulta.setTipoAtendimento("Presencial");
    }

    public ConsultaBuilder comId(int id) {
        consulta.setId(id);
        return this;
    }

    public ConsultaBuilder comPaciente(Paciente paciente) {
        consulta.setPaciente(paciente);
        return this;
    }

    public ConsultaBuilder comMedico(Medico medico) {
        consulta.setMedico(medico);
        if (medico != null) {
            consulta.setEspecialidade(medico.getEspecialidade()); // copia a especialidade do medico
        }
        return this;
    }

    public ConsultaBuilder naData(String data) {
        consulta.setData(data);
        return this;
    }

    public ConsultaBuilder noHorario(String hora) {
        consulta.setHora(hora);
        return this;
    }

    public ConsultaBuilder comEspecialidade(String especialidade) {
        consulta.setEspecialidade(especialidade);
        return this;
    }

    public ConsultaBuilder comTipoAtendimento(String tipo) {
        consulta.setTipoAtendimento(tipo);
        return this;
    }

    public ConsultaBuilder comStatus(String status) {
        consulta.setStatus(status);
        return this;
    }

    public ConsultaBuilder comValor(double valor) {
        consulta.setValor(valor);
        return this;
    }

    public ConsultaBuilder comFormaPagamento(String forma) {
        consulta.setFormaPagamento(forma);
        return this;
    }

    public ConsultaBuilder comObservacoes(String observacoes) {
        consulta.setObservacoes(observacoes);
        return this;
    }

    public ConsultaBuilder criadaEm(String dataCriacao) {
        consulta.setDataCriacao(dataCriacao);
        return this;
    }

    public ConsultaBuilder comExames(String exames) {
        consulta.setExames(exames);
        return this;
    }

    public ConsultaBuilder comValorTotal(double valorTotal) {
        consulta.setValorTotal(valorTotal);
        return this;
    }

    public Consulta constroi() {
        return consulta;
    }
}
