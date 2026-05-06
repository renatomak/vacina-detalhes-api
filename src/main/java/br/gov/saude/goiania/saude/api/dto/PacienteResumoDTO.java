package br.gov.saude.goiania.saude.api.dto;

import java.time.LocalDate;

public record PacienteResumoDTO(
        Long id,
        String nome,
        String cpf,
        LocalDate dataNascimento
) {
}


