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
    @Mapping(target = "cartaoSus", source = "cartaoSus")
    @Mapping(target = "nomeSocial", source = "nomeSocial")
    @Mapping(target = "paisNascimento", source = "paisNascimento")
    @Mapping(target = "ufNascimento", source = "ufNascimento")
    @Mapping(target = "municipioNascimento", source = "municipioNascimento")
    @Mapping(target = "raca", source = "raca")
    @Mapping(target = "etnia", source = "etnia")
    @Mapping(target = "telefoneContato", source = "telefoneContato")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "paisEndereco", source = "paisEndereco")
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
