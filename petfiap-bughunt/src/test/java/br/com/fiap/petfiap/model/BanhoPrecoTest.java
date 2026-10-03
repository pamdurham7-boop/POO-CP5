package br.com.fiap.petfiap.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BanhoPrecoTest {

    @Test
    public void deveCobrarPrecoDoContratoQuandoPorteDoPetVariar() {
        // Arrange
        String[] portes = {"PEQUENO", "MEDIO", "GRANDE"};
        double[] precosEsperados = {60.0, 80.0, 100.0};

        for (int indice = 0; indice < portes.length; indice++) {
            Banho banho = new Banho(1, "Rex", portes[indice], "Ana",
                    LocalDateTime.of(2026, 12, 1, 10, 0));

            // Act
            double preco = banho.calcularPreco();

            // Assert
            assertEquals(precosEsperados[indice], preco, 0.001, portes[indice]);
        }
    }
}
