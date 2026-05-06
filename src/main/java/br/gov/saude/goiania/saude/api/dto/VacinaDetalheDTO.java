package br.gov.saude.goiania.saude.api.dto;

import java.time.LocalDate;

public record VacinaDetalheDTO(
        Long idAplicacao,
        Long nrAtendimento,
        Integer doseCodigo,
        String dose,
        String estrategia,
        String nomeVacina,
        String descricaoVacina,
        String lote,
        LocalDate validadeLote,
        String fabricanteNome,
        String fabricanteCnpj,
        LocalDate dataAplicacao,
        String localAtendimento,
        String turno,
        String grupoAtendimento,
        String observacao,
        String status,
        Boolean gestante,
        Boolean puerpera,
        Boolean historico,
        Boolean foraEsquema,
        Boolean viajante,
        Boolean novoFrasco,
        String viaAdministracao,
        String localAplicacao,
        String profissionalNome,
        String profissionalConselho,
        String profissionalRegistro,
        String profissionalCns,
        String unidadeNome,
        String unidadeCnes,
        String rndsSituacao,
        String rndsUuid
) {
}


