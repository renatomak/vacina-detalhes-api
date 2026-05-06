package br.gov.saude.goiania.saude.api.controller;

import br.gov.saude.goiania.saude.api.dto.ProntuarioEstruturadoResponse;
import br.gov.saude.goiania.saude.api.service.ProntuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prontuario")
public class ProntuarioController {
    private final ProntuarioService prontuarioService;

    public ProntuarioController(ProntuarioService prontuarioService) {
        this.prontuarioService = prontuarioService;
    }

    @GetMapping("/{pacienteId}")
    public ResponseEntity<ProntuarioEstruturadoResponse> buscarProntuarioEstruturado(@PathVariable Long pacienteId) {
        ProntuarioEstruturadoResponse response = prontuarioService.buscarProntuarioEstruturado(pacienteId);
        return ResponseEntity.ok(response);
    }
}
