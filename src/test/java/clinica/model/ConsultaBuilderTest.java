package clinica.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Testes do padrao Builder da Consulta. */
class ConsultaBuilderTest {

    @Test
    @DisplayName("Builder deve preencher status e tipo de atendimento padrao")
    void valoresPadrao() {
        //Arrange
        ConsultaBuilder builder = new ConsultaBuilder();

        //Act
        Consulta consulta = builder.naData("2026-12-01").noHorario("09:00").constroi();

        //Assert
        assertEquals("Agendada", consulta.getStatus());
        assertEquals("Presencial", consulta.getTipoAtendimento());
    }

    @Test
    @DisplayName("Builder deve copiar a especialidade do medico para a consulta")
    void especialidadeDoMedico() {
        //Arrange
        Medico medico = new Medico();
        medico.setId(1);
        medico.setEspecialidade("Dermatologia");

        //Act
        Consulta consulta = new ConsultaBuilder().comMedico(medico).constroi();

        //Assert
        assertEquals("Dermatologia", consulta.getEspecialidade());
    }
}
