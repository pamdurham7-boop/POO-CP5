package br.com.fiap.petfiap.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConsultaVeterinariaPrecoTest {

    @Test
    public void deveCobrar150ReaisQuandoPorteDoPetVariar() {
        // Arrange
        String[] portes = {"PEQUENO", "MEDIO", "GRANDE"};

        for (String porte : portes) {
            ConsultaVeterinaria consulta = new ConsultaVeterinaria(1, "Mimi", porte, "Bruno",
                    LocalDateTime.of(2026, 12, 1, 14, 0));

            // Act
            double preco = consulta.calcularPreco();

            // Assert
            assertEquals(150.0, preco, 0.001, porte);
        }
    }
}
