package br.gov.saude.goiania.saude.api.repository;

import br.gov.saude.goiania.saude.api.dto.VacinaDetalheDTO;
import br.gov.saude.goiania.saude.api.dto.VacinaResumoDTO;

import java.util.List;
import java.util.Optional;

public interface VacinaRepository {

    List<VacinaResumoDTO> listarPorPacienteId(Long pacienteId);

    Optional<VacinaDetalheDTO> buscarDetalhePorAplicacaoId(Long idAplicacao);
}

