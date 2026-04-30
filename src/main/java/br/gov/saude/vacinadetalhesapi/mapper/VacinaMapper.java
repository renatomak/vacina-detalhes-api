package br.gov.saude.vacinadetalhesapi.mapper;

import br.gov.saude.vacinadetalhesapi.dto.VacinaDetalheDTO;
import br.gov.saude.vacinadetalhesapi.dto.VacinaResumoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface VacinaMapper {
    @Mapping(target = "nrAtendimento", source = "nrAtendimento", qualifiedByName = "toLong")
    @Mapping(target = "doseCodigo", source = "doseCodigo", qualifiedByName = "toInteger")
    @Mapping(target = "validadeLote", source = "validadeLote", qualifiedByName = "toLocalDate")
    @Mapping(target = "dataAplicacao", source = "dataAplicacao", qualifiedByName = "toLocalDate")
    VacinaDetalheDTO toVacinaDetalheDTO(VacinaRaw raw);

    @Mapping(target = "dataAplicacao", source = "dataAplicacao", qualifiedByName = "toLocalDate")
    @Mapping(target = "vacina", source = "vacina")
    @Mapping(target = "dose", source = "dose")
    @Mapping(target = "estrategia", source = "estrategia")
    @Mapping(target = "laboratorio", source = "laboratorio")
    @Mapping(target = "estabelecimento", source = "estabelecimento")
    @Mapping(target = "profissional", source = "profissional")
    VacinaResumoDTO toVacinaResumoDTO(VacinaRaw raw);

    @Named("toLocalDate")
    static LocalDate toLocalDate(Object value) {
        if (value instanceof LocalDate localDate) {
            return localDate;
        }
        if (value instanceof java.sql.Date date) {
            return date.toLocalDate();
        }
        return null;
    }

    @Named("toLong")
    static Long toLong(Object value) {
        if (value == null) return null;
        return ((Number) value).longValue();
    }

    @Named("toInteger")
    static Integer toInteger(Object value) {
        if (value == null) return null;
        return ((Number) value).intValue();
    }
}
