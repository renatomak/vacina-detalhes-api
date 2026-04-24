package br.gov.saude.vacinadetalhesapi.service.impl;

import br.gov.saude.vacinadetalhesapi.domain.ProntuarioItem;
import br.gov.saude.vacinadetalhesapi.port.ProntuarioRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProntuarioServiceImplTest {
    private ProntuarioRepositoryPort prontuarioRepositoryPort;
    private ProntuarioServiceImpl prontuarioService;

    @BeforeEach
    void setUp() {
        prontuarioRepositoryPort = mock(ProntuarioRepositoryPort.class);
        prontuarioService = new ProntuarioServiceImpl(prontuarioRepositoryPort);
    }

    @Test
    void deveRetornarConteudoSemHtml() {
        ProntuarioItem itemComHtml = new ProntuarioItem("01/01/2024 10:00:00", "Profissional", "Unidade", "Evolução", "Baixo", "<b>Texto</b> com <i>HTML</i>");
        when(prontuarioRepositoryPort.buscarHistoricoPorPaciente(1L)).thenReturn(List.of(itemComHtml));
        List<ProntuarioItem> result = prontuarioService.buscarHistoricoPorPaciente(1L);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).conteudo()).isEqualTo("Texto com HTML");
    }
}

