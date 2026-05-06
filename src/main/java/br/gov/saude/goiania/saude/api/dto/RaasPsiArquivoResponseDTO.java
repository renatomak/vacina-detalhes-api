package br.gov.saude.goiania.saude.api.dto;


import java.time.format.DateTimeFormatter;

public record RaasPsiArquivoResponseDTO(
    String mes,
    int ano,
    String dataGeracao,
    Long unidadeId,
    String unidade,
    String situacao,
    long folhas
) {
    public static RaasPsiArquivoResponseDTO from(RaasPsiArquivoDTO dto) {
        String dataFormatada = dto.dataGeracao() != null
            ? dto.dataGeracao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            : null;
        return new RaasPsiArquivoResponseDTO(
            dto.mes(),
            dto.ano(),
            dataFormatada,
            dto.unidadeId(),
            dto.unidade(),
            dto.situacao(),
            dto.folhas()
        );
    }
}

