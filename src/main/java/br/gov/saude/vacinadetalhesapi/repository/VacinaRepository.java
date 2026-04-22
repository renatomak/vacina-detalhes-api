package br.gov.saude.vacinadetalhesapi.repository;

import br.gov.saude.vacinadetalhesapi.dto.VacinaDetalheDTO;
import br.gov.saude.vacinadetalhesapi.dto.VacinaResumoDTO;

import java.util.List;
import java.util.Optional;

public interface VacinaRepository {

    List<VacinaResumoDTO> listarPorPacienteId(Long pacienteId);

    Optional<VacinaDetalheDTO> buscarDetalhePorAplicacaoId(Long idAplicacao);
}

