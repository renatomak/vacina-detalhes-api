package br.gov.saude.vacinadetalhesapi.controller;

import br.gov.saude.vacinadetalhesapi.dto.PacienteDTO;
import br.gov.saude.vacinadetalhesapi.dto.PacienteResumoDTO;
import br.gov.saude.vacinadetalhesapi.service.PacienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping("/search/cpf")
    public ResponseEntity<PacienteDTO> buscarPorCpf(@RequestParam("cpf") String cpf) {
        PacienteDTO paciente = pacienteService.buscarPorCpf(cpf);
        return ResponseEntity.ok(paciente);
    }

    @GetMapping("/search/nome")
    public ResponseEntity<List<PacienteResumoDTO>> buscarPorNome(@RequestParam("nome") String nome) {
        List<PacienteResumoDTO> pacientes = pacienteService.buscarPorNome(nome);
        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.buscarPorId(id));
    }
}

