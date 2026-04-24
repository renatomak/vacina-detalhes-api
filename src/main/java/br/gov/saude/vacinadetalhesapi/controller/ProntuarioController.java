package br.gov.saude.vacinadetalhesapi.controller;

import br.gov.saude.vacinadetalhesapi.domain.ProntuarioItem;
import br.gov.saude.vacinadetalhesapi.service.ProntuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/prontuario")
public class ProntuarioController {
    private final ProntuarioService prontuarioService;

    public ProntuarioController(ProntuarioService prontuarioService) {
        this.prontuarioService = prontuarioService;
    }

    @GetMapping("/{pacienteId}")
    public ResponseEntity<List<ProntuarioItem>> buscarHistoricoPorPaciente(@PathVariable Long pacienteId) {
        List<ProntuarioItem> historico = prontuarioService.buscarHistoricoPorPaciente(pacienteId);
        return ResponseEntity.ok(historico);
    }
}

