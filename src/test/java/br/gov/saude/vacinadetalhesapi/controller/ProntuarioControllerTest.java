package br.gov.saude.vacinadetalhesapi.controller;

import br.gov.saude.vacinadetalhesapi.domain.ProntuarioItem;
import br.gov.saude.vacinadetalhesapi.service.ProntuarioService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProntuarioControllerTest {
    @Test
    void deveRetornarHistoricoDoPaciente() {
        ProntuarioService prontuarioService = mock(ProntuarioService.class);
        ProntuarioItem item = new ProntuarioItem("01/01/2024 10:00:00", "Profissional", "Unidade", "Evolução", "Baixo", "Conteudo");
        when(prontuarioService.buscarHistoricoPorPaciente(1L)).thenReturn(List.of(item));
        ProntuarioController controller = new ProntuarioController(prontuarioService);
        ResponseEntity<List<ProntuarioItem>> response = controller.buscarHistoricoPorPaciente(1L);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).conteudo()).isEqualTo("Conteudo");
    }
}

