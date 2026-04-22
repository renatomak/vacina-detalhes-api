package br.gov.saude.vacinadetalhesapi.dto;

import java.time.LocalDate;

public record PacienteResumoDTO(
        Long id,
        String nome,
        String cpf,
        LocalDate dataNascimento
) {
}


