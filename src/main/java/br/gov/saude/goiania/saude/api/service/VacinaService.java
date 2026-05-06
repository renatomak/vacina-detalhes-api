package br.gov.saude.goiania.saude.api.service;

import br.gov.saude.goiania.saude.api.dto.VacinaDetalheDTO;
import br.gov.saude.goiania.saude.api.dto.VacinaResumoDTO;

import java.util.List;

public interface VacinaService {

    List<VacinaResumoDTO> listarPorPacienteId(Long pacienteId);

    VacinaDetalheDTO buscarDetalhePorAplicacaoId(Long idAplicacao);
}


