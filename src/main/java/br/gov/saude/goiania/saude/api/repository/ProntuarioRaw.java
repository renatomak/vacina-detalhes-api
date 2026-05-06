package br.gov.saude.goiania.saude.api.repository;

import java.time.LocalDateTime;

public interface ProntuarioRaw {
    // Dados do Atendimento
    Long getNrAtendimento();
    LocalDateTime getDtChegada();
    String getUnidadeNome();
    String getUnidadeTelefone();
    String getTipoAtendimento();
    String getClassificacaoRiscoNome();

    // Dados do Profissional (Já vêm com o Fallback da AIH aplicado pela Query)
    String getProfissionalNome();
    String getProfissionalRegistro();
    String getProfissionalTipoConselho();
    String getProfissionalCbo();
    String getProfissionalCboDescricao();

    // Dados do Registro Clínico (Evoluções/Avaliações)
    LocalDateTime getRegistroData();
    Integer getRegistroTipoId();
    String getRegistroConteudo();

    // --- NOVOS CAMPOS PARA SUPORTE À AIH ---
    LocalDateTime getAihDataCadastro();
    String getAihPrincipaisSinais();
    String getAihCondicoesInternacao();
    String getAihPrincipaisResultados();
    String getAihDiagnosticoInicial();
    Boolean getPossuiAih();
}
