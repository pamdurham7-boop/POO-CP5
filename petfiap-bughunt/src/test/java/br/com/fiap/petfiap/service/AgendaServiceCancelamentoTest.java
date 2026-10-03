package br.com.fiap.petfiap.service;

import br.com.fiap.petfiap.exception.StatusInvalidoException;
import br.com.fiap.petfiap.model.Atendimento;
import br.com.fiap.petfiap.model.Banho;
import br.com.fiap.petfiap.repository.AtendimentoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AgendaServiceCancelamentoTest {

    @Mock
    private AtendimentoRepository repository;

    @InjectMocks
    private AgendaService service;

    @Test
    public void devePermitirCancelamentoSomenteQuandoAtendimentoEstiverAgendado() {
        // Arrange: os tres estados da tabela de cancelamento do contrato.
        String[] estados = {"AGENDADO", "CONCLUIDO", "CANCELADO"};

        for (int indice = 0; indice < estados.length; indice++) {
            Long id = (long) indice + 1;
            Banho banho = new Banho(indice + 1, "Rex", "PEQUENO", "Ana",
                    LocalDateTime.now().plusDays(1));
            banho.setStatus(estados[indice]);
            when(repository.findById(id)).thenReturn(Optional.of(banho));

            if ("AGENDADO".equals(estados[indice])) {
                when(repository.save(banho)).thenReturn(banho);

                // Act
                Atendimento cancelado = service.cancelar(id);

                // Assert
                assertSame(banho, cancelado);
                assertEquals("CANCELADO", cancelado.getStatus());
                verify(repository).save(banho);
            } else {
                // Act + Assert: recusar preserva o estado e nao salva.
                assertThrows(StatusInvalidoException.class, () -> service.cancelar(id));
                assertEquals(estados[indice], banho.getStatus());
                verify(repository, never()).save(banho);
            }
        }
    }
}
