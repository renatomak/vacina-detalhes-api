package br.gov.saude.vacinadetalhesapi.service.impl;

import br.gov.saude.vacinadetalhesapi.port.ProntuarioRepositoryPort;
import br.gov.saude.vacinadetalhesapi.service.ProntuarioService;
import org.springframework.stereotype.Service;

import br.gov.saude.vacinadetalhesapi.service.PacienteService;
import br.gov.saude.vacinadetalhesapi.dto.ProntuarioEstruturadoResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProntuarioServiceImpl implements ProntuarioService {
    private final ProntuarioRepositoryPort prontuarioRepositoryPort;
    private final PacienteService pacienteService;
    private static final Logger log = LoggerFactory.getLogger(ProntuarioServiceImpl.class);

    public ProntuarioServiceImpl(ProntuarioRepositoryPort prontuarioRepositoryPort, PacienteService pacienteService) {
        this.prontuarioRepositoryPort = prontuarioRepositoryPort;
        this.pacienteService = pacienteService;
    }

    @Override
    public ProntuarioEstruturadoResponse buscarProntuarioEstruturado(Long pacienteId) {
        try {
            var paciente = pacienteService.buscarPorId(pacienteId);
            var atendimentos = prontuarioRepositoryPort.buscarAtendimentosComRegistrosPorPaciente(pacienteId);
            if (atendimentos == null) atendimentos = java.util.Collections.emptyList();
            return new ProntuarioEstruturadoResponse(paciente, atendimentos);
        } catch (Exception e) {
            log.error("Erro ao buscar prontuário estruturado para paciente {}: {}", pacienteId, e.getMessage(), e);
            throw e;
        }
    }
}
