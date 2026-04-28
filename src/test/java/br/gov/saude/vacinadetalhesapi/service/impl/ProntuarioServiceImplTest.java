package br.gov.saude.vacinadetalhesapi.service.impl;

import br.gov.saude.vacinadetalhesapi.port.ProntuarioRepositoryPort;
import br.gov.saude.vacinadetalhesapi.service.PacienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProntuarioServiceImplTest {
    private ProntuarioRepositoryPort prontuarioRepositoryPort;
    private PacienteService pacienteService;
    private ProntuarioServiceImpl prontuarioService;

    @BeforeEach
    void setUp() {
        prontuarioRepositoryPort = mock(ProntuarioRepositoryPort.class);
        pacienteService = mock(PacienteService.class);
        prontuarioService = new ProntuarioServiceImpl(prontuarioRepositoryPort, pacienteService);
    }

    @Test
    void deveRetornarConteudoSemHtml() {

    }
}
