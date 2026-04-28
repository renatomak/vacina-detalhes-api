package br.gov.saude.vacinadetalhesapi.mapper;

import br.gov.saude.vacinadetalhesapi.dto.PacienteDTO;
import br.gov.saude.vacinadetalhesapi.dto.EnderecoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import br.gov.saude.vacinadetalhesapi.util.FormatadorUtil;

@Mapper(componentModel = "spring")
public interface PacienteMapper {
    PacienteMapper INSTANCE = Mappers.getMapper(PacienteMapper.class);

    @Mapping(target = "sexo", source = "sexo", qualifiedByName = "converterSexo")
    @Mapping(target = "endereco", expression = "java(toEnderecoDTO(raw))")
    @Mapping(target = "dataNascimento", expression = "java(br.gov.saude.vacinadetalhesapi.util.FormatadorUtil.formatarData(raw.dataNascimento))")
    @Mapping(target = "cpf", expression = "java(br.gov.saude.vacinadetalhesapi.util.FormatadorUtil.formatarCpf(raw.cpf))")
    @Mapping(target = "telefone", expression = "java(br.gov.saude.vacinadetalhesapi.util.FormatadorUtil.formatarTelefone(raw.telefone))")
    PacienteDTO toPacienteDTO(PacienteRaw raw);

    // ...

    @Named("converterSexo")
    static String converterSexo(String sexo) {
        if (sexo == null) return "-";
        return switch (sexo.trim().toUpperCase()) {
            case "F" -> "FEMININO";
            case "M" -> "MASCULINO";
            default -> "-";
        };
    }

    default EnderecoDTO toEnderecoDTO(PacienteRaw raw) {
        return new EnderecoDTO(
                raw.keyword,
                raw.tipoLogradouro,
                raw.logradouro,
                raw.complemento,
                raw.numero,
                br.gov.saude.vacinadetalhesapi.util.FormatadorUtil.formatarCep(raw.cep),
                raw.bairro,
                raw.cidadeId,
                raw.cidade,
                raw.uf
        );
    }
}
