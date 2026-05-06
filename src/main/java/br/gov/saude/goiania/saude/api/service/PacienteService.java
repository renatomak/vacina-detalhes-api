package br.gov.saude.goiania.saude.api.service;

import br.gov.saude.goiania.saude.api.dto.PacienteDTO;
import br.gov.saude.goiania.saude.api.dto.PacienteResumoDTO;

public interface PacienteService {


    PacienteSearchResult buscarPorQuery(String query);

    PacienteDTO buscarPorCpf(String cpf);

    java.util.List<PacienteResumoDTO> buscarPorNome(String nome);

    PacienteDTO buscarPorId(Long id);
}


