package clinica.api;

import clinica.model.Consulta;
import clinica.model.ConsultaBuilder;
import clinica.model.Medico;
import clinica.model.Paciente;

/**
 * DTO (record) com os dados que chegam no corpo JSON do POST e do PUT.
 * Exemplo:
 * { "pacienteId": 1, "medicoId": 2, "data": "2026-12-01", "hora": "09:00",
 *   "tipoAtendimento": "Presencial", "status": "Agendada", "valor": 300.0,
 *   "formaPagamento": "PIX", "observacoes": "", "exames": "Hemograma,RaioX" }
 */
public record ConsultaRequest(Integer pacienteId,
                              Integer medicoId,
                              String data,
                              String hora,
                              String tipoAtendimento,
                              String status,
                              Double valor,
                              String formaPagamento,
                              String observacoes,
                              String exames) {

    /** BUILDER montando a consulta a partir do DTO. */
    public Consulta paraConsulta(int id, Paciente paciente, Medico medico) {
        return new ConsultaBuilder()
                .comId(id)
                .comPaciente(paciente)
                .comMedico(medico)
                .naData(data)
                .noHorario(hora)
                .comTipoAtendimento(valorOuPadrao(tipoAtendimento, "Presencial"))
                .comStatus(valorOuPadrao(status, "Agendada"))
                .comValor(valor == null ? 0 : valor)
                .comFormaPagamento(formaPagamento)
                .comObservacoes(observacoes)
                .comExames(exames)
                .constroi();
    }

    private static String valorOuPadrao(String texto, String padrao) {
        if (texto == null || texto.isEmpty()) {
            return padrao;
        }
        return texto;
    }
}
