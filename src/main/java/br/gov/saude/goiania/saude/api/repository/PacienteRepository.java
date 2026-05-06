package br.gov.saude.goiania.saude.api.repository;

import br.gov.saude.goiania.saude.api.dto.PacienteDTO;
import br.gov.saude.goiania.saude.api.dto.PacienteResumoDTO;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository {

    Optional<PacienteDTO> buscarDetalhePorCpf(String cpfSemMascara);

    Optional<PacienteDTO> buscarDetalhePorId(Long id);

    List<PacienteResumoDTO> buscarResumoPorNome(String nomeParcial, int limite);
}

