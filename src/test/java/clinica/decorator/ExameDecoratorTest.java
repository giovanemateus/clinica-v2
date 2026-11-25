package clinica.decorator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Testes do padrao Decorator (consulta + exames). */
class ExameDecoratorTest {

    @Test
    @DisplayName("Consulta sem exames deve custar somente o valor da consulta")
    void consultaSemExames() {
        //Arrange
        Atendimento atendimento = new ConsultaSimples("Cardiologia", 300.0);

        //Act
        double total = atendimento.custo();

        //Assert
        assertEquals(300.0, total);
        assertEquals("Consulta de Cardiologia", atendimento.getDescricao());
    }

    @Test
    @DisplayName("Cada exame decorado deve somar o seu valor na consulta")
    void consultaComExames() {
        //Arrange
        Atendimento atendimento = new ConsultaSimples("Cardiologia", 300.0);
        atendimento = new Hemograma(atendimento);
        atendimento = new Eletrocardiograma(atendimento);

        //Act
        double total = atendimento.custo();

        //Assert
        assertEquals(465.0, total); // 300 + 45 + 120
        assertEquals("Consulta de Cardiologia, Hemograma, Eletrocardiograma", atendimento.getDescricao());
    }

    @Test
    @DisplayName("A fabrica deve montar os decorators a partir do texto salvo no banco")
    void fabricaMontaExames() {
        //Arrange
        String exames = "RaioX,Ultrassom";

        //Act
        Atendimento atendimento = ExameFactory.montar("Pediatria", 200.0, exames);

        //Assert
        assertEquals(440.0, atendimento.custo()); // 200 + 90 + 150
        assertEquals("Consulta de Pediatria, Raio-X, Ultrassom", atendimento.getDescricao());
    }

    @Test
    @DisplayName("A fabrica deve recusar um exame que nao existe")
    void fabricaRecusaExameInvalido() {
        //Arrange
        String exames = "Tomografia";

        //Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ExameFactory.montar("Pediatria", 200.0, exames);
        });

        //Assert
        assertEquals("Exame inválido: Tomografia", exception.getMessage());
    }
}
