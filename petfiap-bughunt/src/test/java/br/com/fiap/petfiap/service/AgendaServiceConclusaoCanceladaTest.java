package br.com.fiap.petfiap.service;

import br.com.fiap.petfiap.exception.StatusInvalidoException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AgendaServiceConclusaoCanceladaTest {

    @Mock
    private AtendimentoRepository repository;

    @InjectMocks
    private AgendaService service;

    @Test
    public void deveRecusarConclusaoQuandoAtendimentoEstiverCancelado() {
        // Arrange
        Banho banho = new Banho(1, "Rex", "PEQUENO", "Ana", LocalDateTime.now().plusDays(1));
        banho.setStatus("CANCELADO");
        when(repository.findById(1L)).thenReturn(Optional.of(banho));

        // Act + Assert
        assertThrows(StatusInvalidoException.class, () -> service.concluir(1L));
        assertEquals("CANCELADO", banho.getStatus());
        verify(repository, never()).save(any());
    }
}
