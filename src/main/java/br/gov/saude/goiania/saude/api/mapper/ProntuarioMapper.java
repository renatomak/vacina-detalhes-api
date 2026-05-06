package br.gov.saude.goiania.saude.api.mapper;

import br.gov.saude.goiania.saude.api.dto.*;
import br.gov.saude.vacinadetalhesapi.dto.*;
import br.gov.saude.goiania.saude.api.repository.ProntuarioRaw;
import org.mapstruct.*;
import java.util.*;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface ProntuarioMapper {

    @Mapping(target = "numeroAtendimento", source = "nrAtendimento")
    @Mapping(target = "dataChegada", expression = "java(formatarDataIso(raw.getDtChegada()))")
    @Mapping(target = "unidade", expression = "java(new UnidadeDTO(raw.getUnidadeNome(), raw.getUnidadeTelefone()))")
    @Mapping(target = "tipoAtendimento", source = "tipoAtendimento")
    @Mapping(target = "profissional", expression = "java(new ProfissionalDTO(raw.getProfissionalNome(), raw.getProfissionalRegistro(), raw.getProfissionalTipoConselho(), raw.getProfissionalCbo(), raw.getProfissionalCboDescricao()))")
    @Mapping(target = "classificacaoRisco", source = "classificacaoRiscoNome")
    @Mapping(target = "possuiAih", source = "possuiAih")
    @Mapping(target = "aihDetalhes", expression = "java(mapAihDetalhes(raw))")
    @Mapping(target = "registros", ignore = true)
    AtendimentoDTO toAtendimentoDTO(ProntuarioRaw raw);

    @Mapping(target = "data", expression = "java(formatarDataIso(raw.getRegistroData()))")
    @Mapping(target = "tipo", expression = "java(mapTipoRegistro(raw.getRegistroTipoId()))")
    @Mapping(target = "conteudo", expression = "java(mapConteudo(raw.getRegistroTipoId(), raw.getRegistroConteudo()))")
    RegistroDTO toRegistroDTO(ProntuarioRaw raw);

    default ProntuarioDTO toProntuarioDTO(Long pacienteId, List<ProntuarioRaw> raws) {
        Map<Long, AtendimentoDTO> atendimentoMap = new LinkedHashMap<>();
        Map<Long, List<RegistroDTO>> registrosMap = new HashMap<>();

        for (ProntuarioRaw raw : raws) {
            Long nrAtendimento = raw.getNrAtendimento();

            if (!atendimentoMap.containsKey(nrAtendimento)) {
                atendimentoMap.put(nrAtendimento, toAtendimentoDTO(raw));
                registrosMap.put(nrAtendimento, new ArrayList<>());
            }

            if (isRegistroValido(raw)) {
                registrosMap.get(nrAtendimento).add(toRegistroDTO(raw));
            }
        }

        List<AtendimentoDTO> atendimentosFinal = atendimentoMap.values().stream()
            .map(atend -> new AtendimentoDTO(
                atend.numeroAtendimento(),
                atend.dataChegada(),
                atend.unidade(),
                atend.tipoAtendimento(),
                atend.profissional(),
                atend.classificacaoRisco(),
                atend.possuiAih(),
                atend.aihDetalhes(),
                registrosMap.get(atend.numeroAtendimento())
            ))
            .toList();

        return new ProntuarioDTO(pacienteId, atendimentosFinal);
    }

    default AihDetalhesDTO mapAihDetalhes(ProntuarioRaw raw) {
        if (raw.getPossuiAih() == null || !raw.getPossuiAih()) {
            return null;
        }
        return new AihDetalhesDTO(
            formatarDataIso(raw.getAihDataCadastro()),
            raw.getAihPrincipaisSinais(),
            raw.getAihCondicoesInternacao(),
            raw.getAihPrincipaisResultados(),
            raw.getAihDiagnosticoInicial()
        );
    }

    default String formatarDataIso(java.time.LocalDateTime data) {
        if (data == null) return null;
        return data.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    default String mapTipoRegistro(Integer tipoId) {
        if (tipoId == null) return "Indefinido";
        return switch (tipoId) {
            case 1 -> "Avaliacao";
            case 2 -> "Evolucao";
            case 6 -> "Exame";
            default -> null;
        };
    }

    default ConteudoDTO mapConteudo(Integer tipoId, String conteudo) {
        if (tipoId == null) return new ConteudoDTO(null, null, null);
        return switch (tipoId) {
            case 1 -> new ConteudoDTO(conteudo, null, null);
            case 2 -> new ConteudoDTO(null, conteudo, null);
            case 6 -> new ConteudoDTO(null, null, conteudo);
            default -> null;
        };
    }

    default boolean isRegistroValido(ProntuarioRaw raw) {
        if (raw == null) return false;

        String tipo = mapTipoRegistro(raw.getRegistroTipoId());
        return tipo != null && raw.getRegistroConteudo() != null && !raw.getRegistroConteudo().isBlank();
    }
}
