package clinica.api;

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
}
