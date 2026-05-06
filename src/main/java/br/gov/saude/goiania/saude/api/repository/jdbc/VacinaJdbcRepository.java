package br.gov.saude.goiania.saude.api.repository.jdbc;

import br.gov.saude.goiania.saude.api.dto.VacinaDetalheDTO;
import br.gov.saude.goiania.saude.api.dto.VacinaResumoDTO;
import br.gov.saude.goiania.saude.api.mapper.VacinaRawRowMapper;
import br.gov.saude.goiania.saude.api.mapper.VacinaMapper;
import br.gov.saude.goiania.saude.api.mapper.VacinaRaw;
import br.gov.saude.goiania.saude.api.repository.VacinaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class VacinaJdbcRepository implements VacinaRepository {

    private static final String VACINAS_RESUMO_POR_PACIENTE_SQL = """
        SELECT
            va.cd_vac_aplicacao AS id_aplicacao,
            va.dt_aplicacao::date AS data_aplicacao,
            COALESCE(tv.ds_vacina, va.ds_vacina) AS vacina,
            CASE 
                WHEN va.cd_doses IN (1,2,3,4) THEN va.cd_doses::text || 'ª Dose'
                WHEN va.cd_doses IN (5,9)     THEN 'Dose Única'
                WHEN va.cd_doses IN (6,38)    THEN 'Reforço'
                WHEN va.cd_doses = 7          THEN '1º Reforço'
                WHEN va.cd_doses = 8          THEN '2º Reforço'
                ELSE COALESCE(va.cd_doses::text, '-')
            END AS dose,
            cale.ds_calendario AS estrategia,
            COALESCE(fab.ds_fabricante, prod.fabricante_esus) AS laboratorio,
            emp.fantasia AS estabelecimento,
            prof.nm_profissional AS profissional
        FROM public.vac_aplicacao va
        LEFT JOIN public.tipo_vacina tv ON tv.cd_vacina = va.cd_vacina
        LEFT JOIN public.calendario cale ON cale.cd_calendario = va.cd_calendario
        LEFT JOIN public.produto_vacina pv ON pv.cd_produto_vacina = va.cd_produto_vacina
        LEFT JOIN public.produtos prod ON prod.cod_pro = pv.cod_pro
        LEFT JOIN public.fabricante_medicamento fab ON fab.cd_fabricante = prod.cd_fabricante
        LEFT JOIN public.empresa emp ON emp.empresa = va.empresa
        LEFT JOIN public.profissional prof ON prof.cd_profissional = va.cd_profissional_aplicacao
        WHERE va.cd_usu_cadsus = ?
          AND va.status IN (0, 1, 3)
        ORDER BY va.dt_aplicacao DESC
        """;

    private static final String VACINA_DETALHE_POR_APLICACAO_SQL = """
            SELECT
                va.cd_vac_aplicacao AS id_aplicacao,
                va.nr_atendimento AS nr_atendimento,
                va.cd_doses AS dose_codigo,
                CASE va.cd_doses
                    WHEN 1 THEN '1a Dose'
                    WHEN 2 THEN '2a Dose'
                    WHEN 3 THEN '3a Dose'
                    WHEN 4 THEN '4a Dose'
                    WHEN 5 THEN 'Dose Unica'
                    WHEN 6 THEN 'Reforco'
                    WHEN 7 THEN '1o Reforco'
                    WHEN 8 THEN '2o Reforco'
                    WHEN 9 THEN 'Dose Unica'
                    WHEN 10 THEN 'Dose Inicial'
                    WHEN 38 THEN 'Reforco'
                    ELSE va.cd_doses::text
                END AS dose,
                cale.ds_calendario AS estrategia,
                tv.ds_vacina AS nome_vacina,
                va.ds_vacina AS descricao_vacina,
                va.lote AS lote,
                va.dt_validade AS validade_lote,
                fm.ds_fabricante AS fabricante_nome,
                fm.cnpj AS fabricante_cnpj,
                va.dt_aplicacao::date AS data_aplicacao,
                CASE va.local_atendimento
                    WHEN 1 THEN 'UBS / Unidade de Saude'
                    WHEN 2 THEN 'Domicilio'
                    WHEN 3 THEN 'Escola'
                    WHEN 4 THEN 'Outros'
                    ELSE va.local_atendimento::text
                END AS local_atendimento,
                CASE va.turno
                    WHEN 1 THEN 'Manha'
                    WHEN 2 THEN 'Tarde'
                    WHEN 3 THEN 'Noite'
                    ELSE va.turno::text
                END AS turno,
                COALESCE(gave.descricao, '') AS grupo_atendimento,
                va.observacao AS observacao,
                CASE va.status
                    WHEN 0 THEN 'Aplicada'
                    WHEN 1 THEN 'Aprazada'
                    WHEN 2 THEN 'Cancelada'
                    ELSE va.status::text
                END AS status,
                (va.flag_gestante = 1) AS gestante,
                (va.flag_puerpera = 1) AS puerpera,
                (va.flag_historico = 1) AS historico,
                (va.flag_fora_esquema_vacinal = 1) AS fora_esquema,
                (va.viajante = 1) AS viajante,
                (va.novo_frasco = 1) AS novo_frasco,
                via.descricao AS via_administracao,
                la.descricao AS local_aplicacao,
                p.nm_profissional AS profissional_nome,
                oe.sg_orgao_emissor AS profissional_conselho,
                p.nr_registro AS profissional_registro,
                p.cd_cns AS profissional_cns,
                e.descricao AS unidade_nome,
                e.cnes::text AS unidade_cnes,
                ri.situacao::text AS rnds_situacao,
                ri.uuid_rnds AS rnds_uuid
            FROM vac_aplicacao va
            LEFT JOIN tipo_vacina tv ON tv.cd_vacina = va.cd_vacina
            LEFT JOIN calendario cale ON cale.cd_calendario = va.cd_estrategia
            LEFT JOIN produto_vacina pv ON pv.cd_produto_vacina = va.cd_produto_vacina
            LEFT JOIN produtos prod ON prod.cod_pro = pv.cod_pro
            LEFT JOIN fabricante_medicamento fm ON fm.cd_fabricante = prod.cd_fabricante
            LEFT JOIN profissional p ON p.cd_profissional = va.cd_profissional_aplicacao
            LEFT JOIN orgao_emissor oe ON oe.cd_orgao_emissor = p.cd_con_classe
            LEFT JOIN empresa e ON e.empresa = va.empresa
            LEFT JOIN local_aplicacao la ON la.cd_local_aplicacao = va.cd_local_aplicacao
            LEFT JOIN via_administracao via ON via.cd_via_administracao = va.cd_via_administracao
            LEFT JOIN grupo_atendimento_vacinacao_esus gave ON gave.cd_grupo_atendimento_vac_esus = va.grupo_atendimento
            LEFT JOIN rnds_integracao_vacina ri ON ri.cd_vac_aplicacao = va.cd_vac_aplicacao
            WHERE va.cd_vac_aplicacao = ?
            """;

    private final JdbcTemplate jdbcTemplate;
    private final VacinaRawRowMapper rawRowMapper;
    private final VacinaMapper vacinaMapper;

    public VacinaJdbcRepository(JdbcTemplate jdbcTemplate, VacinaRawRowMapper rawRowMapper, VacinaMapper vacinaMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rawRowMapper = rawRowMapper;
        this.vacinaMapper = vacinaMapper;
    }

    @Override
    public List<VacinaResumoDTO> listarPorPacienteId(Long pacienteId) {
        return jdbcTemplate.query(VACINAS_RESUMO_POR_PACIENTE_SQL, rawRowMapper.resumo(), pacienteId)
                .stream()
                .map(vacinaMapper::toVacinaResumoDTO)
                .toList();
    }

    @Override
    public Optional<VacinaDetalheDTO> buscarDetalhePorAplicacaoId(Long idAplicacao) {
        List<VacinaRaw> resultado = jdbcTemplate.query(
                VACINA_DETALHE_POR_APLICACAO_SQL,
                rawRowMapper.detalhe(),
                idAplicacao
        );
        return resultado.stream().findFirst().map(vacinaMapper::toVacinaDetalheDTO);
    }
}


