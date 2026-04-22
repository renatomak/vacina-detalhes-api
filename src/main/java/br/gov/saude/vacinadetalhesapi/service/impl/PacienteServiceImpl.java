package br.gov.saude.vacinadetalhesapi.service.impl;

import br.gov.saude.vacinadetalhesapi.dto.PacienteDTO;
import br.gov.saude.vacinadetalhesapi.dto.PacienteResumoDTO;
import br.gov.saude.vacinadetalhesapi.repository.PacienteRepository;
import br.gov.saude.vacinadetalhesapi.service.PacienteSearchResult;
import br.gov.saude.vacinadetalhesapi.service.PacienteService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    private final int limiteBuscaNome;

    public PacienteServiceImpl(PacienteRepository pacienteRepository,
                               @Value("${app.search.nome-limit:50}") int limiteBuscaNome) {
        this.pacienteRepository = pacienteRepository;
        this.limiteBuscaNome = limiteBuscaNome;
    }

    @Override
    public PacienteSearchResult buscarPorQuery(String query) {
        if (query == null || query.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O parametro query e obrigatorio.");
        }

        String queryNormalizada = query.trim();
        String cpfSemMascara = queryNormalizada.replaceAll("\\D", "");

        if (cpfSemMascara.length() == 11) {
            PacienteDTO paciente = pacienteRepository.buscarDetalhePorCpf(cpfSemMascara)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Paciente nao encontrado para o CPF informado."));
            return PacienteSearchResult.resultadoCpf(paciente);
        }

        List<PacienteResumoDTO> pacientes = pacienteRepository.buscarResumoPorNome(queryNormalizada, limiteBuscaNome);
        return PacienteSearchResult.resultadoNome(pacientes);
    }

    @Override
    public PacienteDTO buscarPorId(Long id) {
        return pacienteRepository.buscarDetalhePorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Paciente nao encontrado para o id informado."));
    }
}

