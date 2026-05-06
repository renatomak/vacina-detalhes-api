package br.gov.saude.goiania.saude.api.dto;

import java.time.LocalDate;

public record VacinaResumoDTO(
        Long idAplicacao,
        LocalDate dataAplicacao,
        String vacina,
        String dose,
        String estrategia,
        String laboratorio,
        String estabelecimento,
        String profissional
) {
}
