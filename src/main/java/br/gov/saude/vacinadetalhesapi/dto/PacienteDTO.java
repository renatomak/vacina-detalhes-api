package br.gov.saude.vacinadetalhesapi.dto;

import java.time.LocalDate;

public record PacienteDTO(
        Long id,
        String nome,
        String cpf,
        String sexo,
        String nomeMae,
        String nomePai,
        LocalDate dataNascimento,
        String telefone,
        EnderecoDTO endereco
) {
}

