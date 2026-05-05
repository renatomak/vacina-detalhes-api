package br.gov.saude.vacinadetalhesapi.dto;

import java.time.LocalDate;

public record RaasPsiArquivoDTO(
        String mes,
        int ano,
        LocalDate dataGeracao,
        Long unidadeId,
        String unidade,
        String situacao,
        long folhas
) {}
