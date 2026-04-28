package br.gov.saude.vacinadetalhesapi.mapper;

import br.gov.saude.vacinadetalhesapi.dto.*;
import br.gov.saude.vacinadetalhesapi.repository.ProntuarioRaw;
import org.mapstruct.*;
import java.util.*;

@Mapper(componentModel = "spring")
public interface ProntuarioMapper {
    @Mapping(target = "numeroAtendimento", source = "nrAtendimento")
    @Mapping(target = "dataChegada", expression = "java(formatarData(raw.getDtChegada()))")
    @Mapping(target = "unidade", expression = "java(new UnidadeDTO(raw.getUnidadeNome(), raw.getUnidadeTelefone()))")
    @Mapping(target = "tipoAtendimento", source = "tipoAtendimento")
    @Mapping(target = "profissional", expression = "java(new ProfissionalDTO(raw.getProfissionalNome(), raw.getProfissionalRegistro(), raw.getProfissionalTipoConselho(), raw.getProfissionalCbo(), raw.getProfissionalCboDescricao()))")
    @Mapping(target = "classificacaoRisco", source = "classificacaoRiscoNome")
    @Mapping(target = "registros", ignore = true)
    AtendimentoDTO toAtendimentoDTO(ProntuarioRaw raw);

    @Mapping(target = "data", expression = "java(formatarData(raw.getRegistroData()))")
    @Mapping(target = "tipo", expression = "java(mapTipoRegistro(raw.getRegistroTipoId()))")
    @Mapping(target = "conteudo", expression = "java(mapConteudo(raw.getRegistroTipoId(), raw.getRegistroConteudo()))")
    RegistroDTO toRegistroDTO(ProntuarioRaw raw);

    default ProntuarioDTO toProntuarioDTO(Long pacienteId, List<ProntuarioRaw> raws) {
        Map<Long, AtendimentoDTO> atendimentoMap = new LinkedHashMap<>();
        Map<Long, List<RegistroDTO>> registrosMap = new HashMap<>();
        for (ProntuarioRaw raw : raws) {
            Long nrAtendimento = raw.getNrAtendimento();
            if (!atendimentoMap.containsKey(nrAtendimento)) {
                AtendimentoDTO atendimento = toAtendimentoDTO(raw);
                atendimentoMap.put(nrAtendimento, atendimento);
                registrosMap.put(nrAtendimento, new ArrayList<>());
            }
            registrosMap.get(nrAtendimento).add(toRegistroDTO(raw));
        }
        List<AtendimentoDTO> atendimentos = new ArrayList<>();
        for (Long nrAtendimento : atendimentoMap.keySet()) {
            AtendimentoDTO atendimento = atendimentoMap.get(nrAtendimento);
            List<RegistroDTO> registros = registrosMap.get(nrAtendimento);
            atendimentos.add(new AtendimentoDTO(
                atendimento.numeroAtendimento(),
                atendimento.dataChegada(),
                atendimento.unidade(),
                atendimento.tipoAtendimento(),
                atendimento.profissional(),
                atendimento.classificacaoRisco(),
                registros
            ));
        }
        return new ProntuarioDTO(pacienteId, atendimentos);
    }


    default String formatarData(java.time.LocalDateTime data) {
        if (data == null) return null;
        return data.toLocalDate().toString();
    }
    default String mapTipoRegistro(Integer tipoId) {
        if (tipoId == null) return null;
        return switch (tipoId) {
            case 1 -> "Avaliacao";
            case 2 -> "Evolucao";
            case 6 -> "Exame";
            default -> "Outro";
        };
    }
    default ConteudoDTO mapConteudo(Integer tipoId, String conteudo) {
        if (tipoId == null) return new ConteudoDTO(null, null, null);
        return switch (tipoId) {
            case 1 -> new ConteudoDTO(conteudo, null, null);
            case 2 -> new ConteudoDTO(null, conteudo, null);
            case 6 -> new ConteudoDTO(null, null, conteudo);
            default -> new ConteudoDTO(null, null, null);
        };
    }


}

