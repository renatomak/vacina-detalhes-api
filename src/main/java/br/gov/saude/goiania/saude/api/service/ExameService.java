package br.gov.saude.goiania.saude.api.service;

import br.gov.saude.goiania.saude.api.dto.ExameResponseDTO;
import br.gov.saude.goiania.saude.api.repository.ExameRepository;
import org.springframework.stereotype.Service;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ExameService {
    private final ExameRepository exameRepository;

    public ExameService(ExameRepository exameRepository) {
        this.exameRepository = exameRepository;
    }

    public List<ExameResponseDTO> buscarExamesPorAtendimento(Long nrAtendimento) {
        List<Object[]> results = exameRepository.buscarExamesPorAtendimento(nrAtendimento);
        List<ExameResponseDTO> exames = new ArrayList<>();
        for (Object[] row : results) {
            String grupo = (String) row[0];
            List<String> itens = new ArrayList<>();
            if (row[1] instanceof Array arr) {
                try {
                    Object[] arrObj = (Object[]) arr.getArray();
                    for (Object o : arrObj) {
                        itens.add(o != null ? o.toString() : null);
                    }
                } catch (Exception e) {
                    // fallback
                }
            } else if (row[1] instanceof String[] arrStr) {
                itens = Arrays.asList(arrStr);
            }
            exames.add(new ExameResponseDTO(grupo, itens));
        }
        return exames;
    }
}

