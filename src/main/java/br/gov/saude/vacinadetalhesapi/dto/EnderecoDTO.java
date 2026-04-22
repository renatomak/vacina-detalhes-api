package br.gov.saude.vacinadetalhesapi.dto;

public record EnderecoDTO(
        String keyword,
        String tipoLogradouro,
        String logradouro,
        String complemento,
        String numero,
        String cep,
        String bairro,
        Long cidadeId,
        String cidade,
        String uf
) {
}


