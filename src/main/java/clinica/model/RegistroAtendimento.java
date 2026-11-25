package clinica.model;

/** Registro do atendimento. Relacionamento 1:1: cada consulta tem no maximo um registro. */
public class RegistroAtendimento {

    private int id;
    private int consultaId;
    private String diagnostico;
    private String prescricao;
    private String dataRegistro;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getConsultaId() { return consultaId; }
    public void setConsultaId(int consultaId) { this.consultaId = consultaId; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getPrescricao() { return prescricao; }
    public void setPrescricao(String prescricao) { this.prescricao = prescricao; }

    public String getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(String dataRegistro) { this.dataRegistro = dataRegistro; }
}
