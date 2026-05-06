package br.gov.saude.goiania.saude.api.service.impl;

import br.gov.saude.goiania.saude.api.port.ProntuarioRepositoryPort;
import br.gov.saude.goiania.saude.api.service.PacienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
