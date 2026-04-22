package br.gov.saude.vacinadetalhesapi.service.impl;

import br.gov.saude.vacinadetalhesapi.dto.VacinaDetalheDTO;
import br.gov.saude.vacinadetalhesapi.dto.VacinaResumoDTO;
import br.gov.saude.vacinadetalhesapi.repository.VacinaRepository;
import br.gov.saude.vacinadetalhesapi.service.VacinaService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class VacinaServiceImpl implements VacinaService {

    private final VacinaRepository vacinaRepository;

    public VacinaServiceImpl(VacinaRepository vacinaRepository) {
        this.vacinaRepository = vacinaRepository;
    }

    @Override
    public List<VacinaResumoDTO> listarPorPacienteId(Long pacienteId) {
        return vacinaRepository.listarPorPacienteId(pacienteId);
    }

    @Override
    public VacinaDetalheDTO buscarDetalhePorAplicacaoId(Long idAplicacao) {
        return vacinaRepository.buscarDetalhePorAplicacaoId(idAplicacao)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Aplicacao de vacina nao encontrada."));
    }
}

