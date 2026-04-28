package br.gov.saude.vacinadetalhesapi.repository;

import java.time.LocalDateTime;

public interface ProntuarioRaw {
    Long getNrAtendimento();
    LocalDateTime getDtChegada();
    String getUnidadeNome();
    String getUnidadeTelefone();
    String getTipoAtendimento();
    String getProfissionalNome();
    String getProfissionalRegistro();
    String getProfissionalTipoConselho();
    String getProfissionalCbo();
    String getProfissionalCboDescricao();
    LocalDateTime getRegistroData();
    Integer getRegistroTipoId();
    String getRegistroConteudo();
    String getClassificacaoRiscoNome();
}

