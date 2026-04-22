package br.gov.saude.vacinadetalhesapi.repository;

import br.gov.saude.vacinadetalhesapi.dto.PacienteDTO;
import br.gov.saude.vacinadetalhesapi.dto.PacienteResumoDTO;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository {

    Optional<PacienteDTO> buscarDetalhePorCpf(String cpfSemMascara);

    Optional<PacienteDTO> buscarDetalhePorId(Long id);

    List<PacienteResumoDTO> buscarResumoPorNome(String nomeParcial, int limite);
}

