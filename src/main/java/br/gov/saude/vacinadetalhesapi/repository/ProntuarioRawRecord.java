package br.gov.saude.vacinadetalhesapi.repository;

import java.time.LocalDateTime;

public record ProntuarioRawRecord(
    Long nrAtendimento,
    LocalDateTime dtChegada,
    String unidadeNome,
    String unidadeTelefone,
    String tipoAtendimento,
    String profissionalNome,
    String profissionalRegistro,
    String profissionalTipoConselho,
    String profissionalCbo,
    String profissionalCboDescricao,
    LocalDateTime registroData,
    Integer registroTipoId,
    String registroConteudo,
    String classificacaoRiscoNome,
    // Novos campos da AIH
    LocalDateTime aihDataCadastro,
    String aihPrincipaisSinais,
    String aihCondicoesInternacao,
    String aihPrincipaisResultados,
    String aihDiagnosticoInicial,
    Boolean possuiAih
) implements ProntuarioRaw {
    @Override public Long getNrAtendimento() { return nrAtendimento; }
    @Override public LocalDateTime getDtChegada() { return dtChegada; }
    @Override public String getUnidadeNome() { return unidadeNome; }
    @Override public String getUnidadeTelefone() { return unidadeTelefone; }
    @Override public String getTipoAtendimento() { return tipoAtendimento; }
    @Override public String getProfissionalNome() { return profissionalNome; }
    @Override public String getProfissionalRegistro() { return profissionalRegistro; }
    @Override public String getProfissionalTipoConselho() { return profissionalTipoConselho; }
    @Override public String getProfissionalCbo() { return profissionalCbo; }
    @Override public String getProfissionalCboDescricao() { return profissionalCboDescricao; }
    @Override public LocalDateTime getRegistroData() { return registroData; }
    @Override public Integer getRegistroTipoId() { return registroTipoId; }
    @Override public String getRegistroConteudo() { return registroConteudo; }
    @Override public String getClassificacaoRiscoNome() { return classificacaoRiscoNome; }
    @Override public LocalDateTime getAihDataCadastro() { return aihDataCadastro; }
    @Override public String getAihPrincipaisSinais() { return aihPrincipaisSinais; }
    @Override public String getAihCondicoesInternacao() { return aihCondicoesInternacao; }
    @Override public String getAihPrincipaisResultados() { return aihPrincipaisResultados; }
    @Override public String getAihDiagnosticoInicial() { return aihDiagnosticoInicial; }
    @Override public Boolean getPossuiAih() { return possuiAih; }
}
