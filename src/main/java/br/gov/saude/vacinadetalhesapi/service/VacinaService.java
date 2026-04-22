package br.gov.saude.vacinadetalhesapi.service;

import br.gov.saude.vacinadetalhesapi.dto.VacinaDetalheDTO;
import br.gov.saude.vacinadetalhesapi.dto.VacinaResumoDTO;

import java.util.List;

public interface VacinaService {

    List<VacinaResumoDTO> listarPorPacienteId(Long pacienteId);

    VacinaDetalheDTO buscarDetalhePorAplicacaoId(Long idAplicacao);
}


