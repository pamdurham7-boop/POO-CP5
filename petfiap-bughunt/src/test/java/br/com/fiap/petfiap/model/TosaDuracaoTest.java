package br.com.fiap.petfiap.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TosaDuracaoTest {

    @Test
    public void deveDurar60MinutosQuandoAtendimentoForTosa() {
        // Arrange: a chamada usa o tipo abstrato, como no resumo do controller.
        Atendimento atendimento = new Tosa(1, "Rex", "PEQUENO", "Ana",
                LocalDateTime.of(2026, 12, 1, 10, 0));

        // Act
        int duracao = atendimento.getDuracaoMinutos();

        // Assert
        assertEquals(60, duracao);
    }
}
