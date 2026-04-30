package br.gov.saude.vacinadetalhesapi.dto;


public record PacienteDTO(
        Long id,
        String nome,
        String cpf,
        String sexo,
        String nomeMae,
        String nomePai,
        String dataNascimento,
        String telefone,
        String cartaoSus,
        String nomeSocial,
        String paisNascimento,
        String ufNascimento,
        String municipioNascimento,
        String raca,
        String etnia,
        String telefoneContato,
        String email,
        EnderecoDTO endereco,
        String paisEndereco
) {}

