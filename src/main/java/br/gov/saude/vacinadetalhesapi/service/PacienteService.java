package br.gov.saude.vacinadetalhesapi.service;

import br.gov.saude.vacinadetalhesapi.dto.PacienteDTO;

public interface PacienteService {


    PacienteSearchResult buscarPorQuery(String query);

    PacienteDTO buscarPorCpf(String cpf);

    java.util.List<br.gov.saude.vacinadetalhesapi.dto.PacienteResumoDTO> buscarPorNome(String nome);

    PacienteDTO buscarPorId(Long id);
}


