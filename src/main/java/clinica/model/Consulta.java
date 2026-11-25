package clinica.model;

/**
 * Entidade principal do sistema (14 atributos).
 * Relacionamentos:
 *  - N:1 com Paciente e N:1 com Medico (um medico tem varias consultas)
 *  - 1:1 com RegistroAtendimento
 * Os objetos sao criados pelo ConsultaBuilder (padrao Builder).
 */
public class Consulta {

    private int id;
    private Paciente paciente;
    private Medico medico;
    private String data;             // formato yyyy-MM-dd
    private String hora;             // formato HH:mm
    private String especialidade;
    private String tipoAtendimento;  // Presencial, Telemedicina, Retorno
    private String status;           // Agendada, Confirmada, Realizada, Cancelada
    private double valor;
    private String formaPagamento;   // Dinheiro, PIX, Cartao, Convenio
    private String observacoes;
    private String dataCriacao;
    private String exames;           // V2: exames pedidos, ex.: "Hemograma,RaioX"
    private double valorTotal;       // V2: consulta + exames (calculado pelo Decorator)

    private RegistroAtendimento registro; // relacionamento 1:1

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }

    public String getTipoAtendimento() { return tipoAtendimento; }
    public void setTipoAtendimento(String tipoAtendimento) { this.tipoAtendimento = tipoAtendimento; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public String getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public String getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(String dataCriacao) { this.dataCriacao = dataCriacao; }

    public String getExames() { return exames; }
    public void setExames(String exames) { this.exames = exames; }

    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }

    public RegistroAtendimento getRegistro() { return registro; }
    public void setRegistro(RegistroAtendimento registro) { this.registro = registro; }
}
