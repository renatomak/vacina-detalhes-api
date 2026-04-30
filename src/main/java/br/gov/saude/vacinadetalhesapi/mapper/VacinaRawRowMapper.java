package br.gov.saude.vacinadetalhesapi.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class VacinaRawRowMapper {
    public RowMapper<VacinaRaw> resumo() {
        return (rs, rowNum) -> {
            VacinaRaw raw = new VacinaRaw();
            raw.idAplicacao = rs.getLong("id_aplicacao");
            raw.dataAplicacao = rs.getObject("data_aplicacao");
            raw.vacina = rs.getString("vacina");
            raw.dose = rs.getString("dose");
            raw.estrategia = rs.getString("estrategia");
            raw.laboratorio = rs.getString("laboratorio");
            raw.estabelecimento = rs.getString("estabelecimento");
            raw.profissional = rs.getString("profissional");
            return raw;
        };
    }

    public RowMapper<VacinaRaw> detalhe() {
        return (rs, rowNum) -> {
            VacinaRaw raw = new VacinaRaw();
            raw.idAplicacao = rs.getLong("id_aplicacao");
            raw.nrAtendimento = rs.getObject("nr_atendimento");
            raw.doseCodigo = rs.getObject("dose_codigo");
            raw.dose = rs.getString("dose");
            raw.estrategia = rs.getString("estrategia");
            raw.nomeVacina = rs.getString("nome_vacina");
            raw.descricaoVacina = rs.getString("descricao_vacina");
            raw.lote = rs.getString("lote");
            raw.validadeLote = rs.getObject("validade_lote");
            raw.fabricanteNome = rs.getString("fabricante_nome");
            raw.fabricanteCnpj = rs.getString("fabricante_cnpj");
            raw.dataAplicacao = rs.getObject("data_aplicacao");
            raw.localAtendimento = rs.getString("local_atendimento");
            raw.turno = rs.getString("turno");
            raw.grupoAtendimento = rs.getString("grupo_atendimento");
            raw.observacao = rs.getString("observacao");
            raw.status = rs.getString("status");
            raw.gestante = rs.getBoolean("gestante");
            raw.puerpera = rs.getBoolean("puerpera");
            raw.historico = rs.getBoolean("historico");
            raw.foraEsquema = rs.getBoolean("fora_esquema");
            raw.viajante = rs.getBoolean("viajante");
            raw.novoFrasco = rs.getBoolean("novo_frasco");
            raw.viaAdministracao = rs.getString("via_administracao");
            raw.localAplicacao = rs.getString("local_aplicacao");
            raw.profissionalNome = rs.getString("profissional_nome");
            raw.profissionalConselho = rs.getString("profissional_conselho");
            raw.profissionalRegistro = rs.getString("profissional_registro");
            raw.profissionalCns = rs.getString("profissional_cns");
            raw.unidadeNome = rs.getString("unidade_nome");
            raw.unidadeCnes = rs.getString("unidade_cnes");
            raw.rndsSituacao = rs.getString("rnds_situacao");
            raw.rndsUuid = rs.getString("rnds_uuid");
            return raw;
        };
    }
}
