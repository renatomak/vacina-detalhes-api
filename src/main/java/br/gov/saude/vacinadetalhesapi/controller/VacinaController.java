package br.gov.saude.vacinadetalhesapi.controller;

import br.gov.saude.vacinadetalhesapi.dto.VacinaDetalheDTO;
import br.gov.saude.vacinadetalhesapi.dto.VacinaResumoDTO;
import br.gov.saude.vacinadetalhesapi.service.VacinaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class VacinaController {

    private final VacinaService vacinaService;

    public VacinaController(VacinaService vacinaService) {
        this.vacinaService = vacinaService;
    }

    @GetMapping("/pacientes/{id}/vacinas")
    public ResponseEntity<List<VacinaResumoDTO>> listarVacinasPorPaciente(@PathVariable("id") Long pacienteId) {
        return ResponseEntity.ok(vacinaService.listarPorPacienteId(pacienteId));
    }

    @GetMapping("/vacinas/aplicacoes/{idAplicacao}")
    public ResponseEntity<VacinaDetalheDTO> detalharAplicacao(@PathVariable Long idAplicacao) {
        return ResponseEntity.ok(vacinaService.buscarDetalhePorAplicacaoId(idAplicacao));
    }
}

